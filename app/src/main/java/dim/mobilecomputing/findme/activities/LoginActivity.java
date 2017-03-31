package dim.mobilecomputing.findme.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.fragments.ForgetPasswordFragment;
import dim.mobilecomputing.findme.fragments.LoginFragment;
import dim.mobilecomputing.findme.fragments.RegisterFragment;
import dim.mobilecomputing.findme.models.User;

public class LoginActivity extends AppCompatActivity {

    Fragment fragment = null;
    public ProgressDialog mDialog;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        changeFragmentLogin();

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Processing...");
        mDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("USER => ", "USER " + user.getUid());
                    Log.d("USER => ", "USER " + user.getDisplayName());
                    createMainActivity();
                } else {
                    // User is signed out
                    Toast.makeText(getApplicationContext(), "Please Login / Register", Toast.LENGTH_SHORT).show();
                    Log.d("USER => ", "NO USER");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        mDialog.hide();
        LoginActivity.this.startActivity(i);
    }

    public void changeFragmentRegister() {
        try {
            fragment = new RegisterFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.login_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("SIGNUP", e.getMessage());
        }
    }

    public void changeFragmentLogin() {
        try {
            fragment = new LoginFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.login_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("LOGIN", e.getMessage());
        }
    }

    public void changeFragmentForgerPassword() {
        try {
            fragment = new ForgetPasswordFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.login_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("LOGIN", e.getMessage());
        }
    }

    public void register(String email, String password, final User u) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("REG", "STATUS " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("REG", "FAILED" + task.toString());
                    mDialog.hide();
                    Toast.makeText(getApplicationContext(), "Error in Registration", Toast.LENGTH_SHORT).show();
                } else {
                    u.setUid(task.getResult().getUser().getUid());
                    updateProfile(u, task.getResult().getUser());
                    mDialog.hide();
                    Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_SHORT).show();
                    changeFragmentLogin();
                }
            }
        });
    }

    private void updateProfile(User u, FirebaseUser fu) {
        Log.d("UID", u.getUid());
        Log.d("NAME", u.getName());
        Log.d("EMAIL", u.getEmail());

        UserProfileChangeRequest info = new UserProfileChangeRequest.Builder()
                .setDisplayName(u.getName())
                .setPhotoUri(Uri.parse("NULL"))
                .build();
        fu.updateProfile(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Update", "User profile updated.");
                }
            }
        });

        DatabaseReference reference = database.getReference("users");
        reference.child(u.getUid()).setValue(u);
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login Status", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Error In Login", "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.",Toast.LENGTH_SHORT).show();
                        }

                        mDialog.hide();
                    }
                });
    }
}
