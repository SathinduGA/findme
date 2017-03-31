package dim.mobilecomputing.findme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.LoginActivity;


public class LoginFragment extends Fragment {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    Button login;
    Button register;
    TextView change;

    EditText email;
    EditText password;

    public LoginFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        login = (Button) rootView.findViewById(R.id.button_login_login);
        register = (Button) rootView.findViewById(R.id.button_login_register);
        change= (TextView) rootView.findViewById(R.id.text_login_forget_password);

        email = (EditText) rootView.findViewById(R.id.input_login_email);
        password = (EditText) rootView.findViewById(R.id.input_login_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    ((LoginActivity) getActivity()).mDialog.show();
                    ((LoginActivity) getActivity()).login(email.getText().toString().trim(),password.getText().toString());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).changeFragmentRegister();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).changeFragmentForgerPassword();
            }
        });

        return rootView;
    }


    private boolean checkFields() {
        if (email.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (validateEmail(email.getText().toString().trim())) {
                return true;
            } else {
                Toast.makeText(getActivity(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public boolean validateEmail(String passedEmail) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(passedEmail);
        return matcher.find();
    }
}
