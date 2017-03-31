package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.LoginActivity;
import dim.mobilecomputing.findme.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    TextView text_password;
    TextView text_retypepassword;
    Button change;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        text_password = (EditText) rootView.findViewById(R.id.input_settings_password);
        text_retypepassword = (EditText) rootView.findViewById(R.id.input_settings_retypepassword);
        change = (Button) rootView.findViewById(R.id.button_settings_change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        return rootView;
    }

    private void changePassword() {
        if(checkFields()){
            ((MainActivity) getActivity()).mDialog.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = text_password.getText().toString();

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Changed Successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                            ((MainActivity) getActivity()).mDialog.hide();
                        }
                    });
        }
    }

    private boolean checkFields() {
        if (text_password.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), "Use at least 6 characters for Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (text_retypepassword.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Re-Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!((text_password.getText().toString()).equals(text_retypepassword.getText().toString()))) {
            Toast.makeText(getActivity(), "Enter Same Password", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

}
