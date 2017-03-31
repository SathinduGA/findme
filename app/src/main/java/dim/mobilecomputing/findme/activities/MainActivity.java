package dim.mobilecomputing.findme.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Subscribe;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.application.FindMe;
import dim.mobilecomputing.findme.fragments.AboutFragment;
import dim.mobilecomputing.findme.fragments.AddFriendsFragment;
import dim.mobilecomputing.findme.fragments.LocateFriendFragment;
import dim.mobilecomputing.findme.fragments.LocationFragment;
import dim.mobilecomputing.findme.fragments.SettingsFragment;
import dim.mobilecomputing.findme.models.LocationData;
import dim.mobilecomputing.findme.models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public ProgressDialog mDialog;
    Fragment fragment = null;
    NavigationView navigationView;
    private static final int REQUEST_INVITE = 0;
    public User currentUser;
    public User mapUser;
    private DatabaseReference mDatabase;

    TextView nav_name;
    TextView nav_email;

    //FOR LOCATION UPDATES
    //===================================================================

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private android.location.Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // LocationData updates intervals in sec
    private static int UPDATE_INTERVAL = 60000; // 60 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    //===================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FindMe.bus.register(this);

        //ProcessDialog
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Processing...");
        mDialog.setCancelable(false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //GET CURRENT USER DATA
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = new User();
        currentUser.setUid(user.getUid());
        currentUser.setName(user.getDisplayName());
        currentUser.setEmail(user.getEmail());
        Log.d("USER =>", currentUser.getName() + ":" + currentUser.getEmail() + ":" + currentUser.getUid());

        View view = navigationView.getHeaderView(0);
        nav_name = (TextView) view.findViewById(R.id.text_nav_name);
        nav_name.setText(currentUser.getName());
        nav_email = (TextView) view.findViewById(R.id.text_nav_email);
        nav_email.setText(currentUser.getEmail());

        changeFragmentLocateFriend();

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatus("Online");

        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        changeStatus("Offline");
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.nav_locate_friend) {
            changeFragmentLocateFriend();
        } else if (id == R.id.nav_add_friend) {
            changeFragmentAddFriend();
        } else if (id == R.id.nav_invite) {
            onInviteClicked();
        } else if (id == R.id.nav_settings) {
            changeFragmentSettings();
        } else if (id == R.id.nav_about) {
            changeFragmentAbout();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //METHODS FOR DRAWER
    //===================================================================
    public void changeFragmentLocateFriend() {
        try {
            getSupportActionBar().setTitle("Locate Friend");
            fragment = new LocateFriendFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("Locate Friend", e.getMessage());
        }
    }

    public void changeFragmentAddFriend() {
        try {
            getSupportActionBar().setTitle("Add Friends");
            fragment = new AddFriendsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("Add Friend", e.getMessage());
        }
    }


    public void changeFragmentSettings() {
        try {
            getSupportActionBar().setTitle("Settings");
            fragment = new SettingsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("Settings", e.getMessage());
        }
    }

    public void changeFragmentAbout() {
        try {
            getSupportActionBar().setTitle("About");
            fragment = new AboutFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("About", e.getMessage());
        }
    }

    @Subscribe
    public void changeFragmentLocation(User u) {
        try {
            mapUser = u;
            getSupportActionBar().setTitle("Locate Friend");
            fragment = new LocationFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame_container, fragment).commit();
        } catch (Exception e) {
            Log.d("Map", e.getMessage());
        }
    }


    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        changeFragmentLocateFriend();
                        navigationView.setCheckedItem(R.id.nav_locate_friend);
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseAuth.getInstance().signOut();
                        Log.d("Logout", "Clicked");

                        //Goto Login
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        MainActivity.this.startActivity(i);

                    }
                }).create().show();
    }

    private void onInviteClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download FindMe : http://www.google.com");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //===================================================================


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "NOT SUPPORTED", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    public void updateLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            LocationData l = new LocationData();
            l.setLatitude(latitude);
            l.setLongitude(longitude);

            mDatabase.child("location").child(currentUser.getUid()).setValue(l);
            Log.d("LOCATION ==> ", latitude + "/" + longitude);

        } else {
            Log.d("LOCATION ==> ", "ERROR");
        }
    }

    //METHOD TO UPDATE  ONLINE STATUS
    public void changeStatus(String status) {
        mDatabase.child("online-status").child(currentUser.getUid()).setValue(status);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Log.d("LOCATION CHANGED", mLastLocation.toString());

        // Update the new Location
        updateLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
