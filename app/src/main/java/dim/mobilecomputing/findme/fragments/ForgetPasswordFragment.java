package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {

    TextView email;
    Button send;
    ImageView back;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);

        email = (TextView) rootView.findViewById(R.id.input_login_forget_password);
        send = (Button) rootView.findViewById(R.id.button_login_forget_password);
        back = (ImageView) rootView.findViewById(R.id.button_login_forget_back);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).changeFragmentLogin();
            }
        });

        return rootView;
    }

    private void sendMail() {
        String emailAddress = email.getText().toString();
        if (validateEmail(emailAddress)) {
            ((LoginActivity) getActivity()).mDialog.show();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Email Sent", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Failed", Toast.LENGTH_SHORT).show();
                            }
                            ((LoginActivity) getActivity()).mDialog.hide();
                        }
                    });
        }else{
            Toast.makeText(getActivity(),"Enter Valid Email", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateEmail(String passedEmail) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(passedEmail);
        return matcher.find();
    }

}
