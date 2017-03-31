package dim.mobilecomputing.findme.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.MainActivity;
import dim.mobilecomputing.findme.application.FindMe;
import dim.mobilecomputing.findme.models.LocationData;
import dim.mobilecomputing.findme.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    GoogleMap googleMap;
    User mapUser;
    TextView status;

    double latitude = 6.9344;
    double longitude = 79.8428;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        FindMe.bus.register(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mapUser = ((MainActivity) getActivity()).mapUser;

        TextView name = (TextView) rootView.findViewById(R.id.map_name);
        status = (TextView) rootView.findViewById(R.id.map_status);
        name.setText(mapUser.getName());
        loadStatus();

        Log.d("CALLED", "CALLED");
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) this.getChildFragmentManager().findFragmentById(R.id.location_fragment);
        mapFragment.getMapAsync(this);

        loadFriendLocation();

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        FindMe.bus.post(googleMap);
    }

    @Subscribe
    public void manualSetMap(GoogleMap googleMap) {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(mapUser.getName());
        Log.e("MAP SET", latitude + "/" + longitude);

        this.googleMap.clear();
        this.googleMap.addMarker(marker);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
        this.googleMap.animateCamera(update);
    }

    private void loadFriendLocation() {
        mDatabase.child("location").child(mapUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("LOCATION DATA",dataSnapshot.getValue().toString());
                        LocationData l = dataSnapshot.getValue(LocationData.class);

                        latitude = l.getLatitude();
                        longitude = l.getLongitude();

                        FindMe.bus.post(googleMap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void loadStatus(){
        mDatabase.child("online-status").child(mapUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        status.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
