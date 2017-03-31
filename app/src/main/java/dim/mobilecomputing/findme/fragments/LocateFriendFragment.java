package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.MainActivity;
import dim.mobilecomputing.findme.adapters.AddFriendAdapter;
import dim.mobilecomputing.findme.adapters.LocateFriendAdapter;
import dim.mobilecomputing.findme.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocateFriendFragment extends Fragment {

    private DatabaseReference mDatabase;

    RecyclerView rcv_users;
    List<User> users = new ArrayList<>();
    LocateFriendAdapter adapter;
    User currentUser;

    public LocateFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find_friend, container, false);

        currentUser = ((MainActivity) getActivity()).currentUser;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        rcv_users = (RecyclerView) rootView.findViewById(R.id.rcv_locate_friend);
        rcv_users.setLayoutManager(new LinearLayoutManager(getActivity()));
        showLocateFriends();

        adapter = new LocateFriendAdapter(getActivity(), users);
        rcv_users.setAdapter(adapter);

        return rootView;
    }

    private void showLocateFriends() {
        mDatabase.child("contacts-for").child(currentUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        users.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.d("CON FOR", data.getValue().toString());
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
