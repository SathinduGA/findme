package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dim.mobilecomputing.findme.activities.LoginActivity;
import dim.mobilecomputing.findme.models.User;
import dim.mobilecomputing.findme.R;

public class RegisterFragment extends Fragment {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    User user;
    String email;
    String password;

    ImageView ok;
    ImageView cancel;

    TextView text_name;
    TextView text_email;
    TextView text_password;
    TextView text_retypepassword;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        ok = (ImageView) rootView.findViewById(R.id.image_signup_ok);
        cancel = (ImageView) rootView.findViewById(R.id.image_signup_cancel);

        text_name = (EditText) rootView.findViewById(R.id.input_signup_name);
        text_email = (EditText) rootView.findViewById(R.id.input_signup_email);
        text_password = (EditText) rootView.findViewById(R.id.input_signup_password);
        text_retypepassword = (EditText) rootView.findViewById(R.id.input_signup_retypepassword);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    ((LoginActivity) getActivity()).mDialog.show();
                    ((LoginActivity) getActivity()).register(email, password, user);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).changeFragmentLogin();
            }
        });

        return rootView;
    }

    private boolean checkFields() {

        if (text_name.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (text_email.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (text_password.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }else if (text_password.getText().toString().length() < 6) {
                Toast.makeText(getActivity(), "Use at least 6 characters for Password", Toast.LENGTH_SHORT).show();
                return false;
        } else if (text_retypepassword.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Re-Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if(validateEmail(text_email.getText().toString().trim())){
                if((text_password.getText().toString()).equals(text_retypepassword.getText().toString())){
                    user = new User();
                    user.setName(text_name.getText().toString().trim());
                    user.setEmail(text_email.getText().toString().trim());

                    email = text_email.getText().toString().trim();
                    password = text_password.getText().toString();

                    return true;
                }else{
                    Toast.makeText(getActivity(), "Enter Same Password", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(getActivity(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
    }

    public boolean validateEmail(String passedEmail){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(passedEmail);
        return matcher.find();
    }
}
