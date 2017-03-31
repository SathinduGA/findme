package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.MainActivity;
import dim.mobilecomputing.findme.adapters.AddFriendAdapter;
import dim.mobilecomputing.findme.application.FindMe;
import dim.mobilecomputing.findme.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendsFragment extends Fragment {

    private DatabaseReference mDatabase;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    TextView input_add_friend;
    ImageButton button_add_friend;
    RecyclerView rcv_users;
    List<User> users = new ArrayList<>();
    AddFriendAdapter adapter;
    User currentUser;
    User toUser;

    public AddFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_friends, container, false);
        FindMe.bus.register(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = ((MainActivity) getActivity()).currentUser;

        rcv_users = (RecyclerView) rootView.findViewById(R.id.rcv_add_friend);
        rcv_users.setLayoutManager(new LinearLayoutManager(getActivity()));
        input_add_friend = (EditText) rootView.findViewById(R.id.input_add_friend);
        button_add_friend = (ImageButton) rootView.findViewById(R.id.button_add_friend);
        showFriends();

        adapter = new AddFriendAdapter(getActivity(), users);
        rcv_users.setAdapter(adapter);

        button_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String for_user = input_add_friend.getText().toString();
                if (!for_user.equals(currentUser.getEmail())) {
                    if (validateEmail(for_user)) {
                        checkUser(for_user);
                    } else {
                        Toast.makeText(getActivity(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Cannot Enter Your Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;

    }

    public boolean validateEmail(String passedEmail) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(passedEmail);
        return matcher.find();
    }

    private void checkUser(final String email) {
        mDatabase.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            User user = data.getValue(User.class);
                            if (user.getEmail().equals(email)) {
                                flag = true;
                                Log.d("USER FOUND", "TRUE");
                                toUser = user;
                                FindMe.bus.post("");
                                break;
                            }
                        }

                        if (!flag) {
                            Toast.makeText(getActivity(), "No User Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    @Subscribe
    public void addFriend(String tag) {
        Log.d("SAVE FRIEND", toUser.getName());
        mDatabase.child("contacts-to").child(currentUser.getUid()).child(toUser.getUid()).child("to").setValue(toUser);
        mDatabase.child("contacts-for").child(toUser.getUid()).child(currentUser.getUid()).child("for").setValue(currentUser);
        input_add_friend.setText("");
        showFriends();
    }

    private void showFriends() {
        mDatabase.child("contacts-to").child(currentUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        users.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.d("CON TO", data.getValue().toString());
                            for (DataSnapshot d : data.getChildren()) {
                                Log.d("USER", d.getValue().toString());
                                User u = d.getValue(User.class);
                                Log.d("USER", u.getName());
                                Log.d("USER", u.getEmail());

                                users.add(u);
                            }
                        }

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

}
