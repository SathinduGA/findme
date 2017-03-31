package dim.mobilecomputing.findme.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstActivity extends Activity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // FOUND USER
                    createMainActivity();
                } else {
                    // NO USER
                    createLoginActivity();
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
        Intent i = new Intent(FirstActivity.this, MainActivity.class);
        finish();
        FirstActivity.this.startActivity(i);
    }

    public void createLoginActivity() {
        Intent i = new Intent(FirstActivity.this, LoginActivity.class);
        finish();
        FirstActivity.this.startActivity(i);
    }
}
