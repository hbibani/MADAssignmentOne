package my.tutorial.assignment;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import my.tutorial.assignment.databinding.ActivityMaps2Binding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    String streetAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        //get location from intent and produce string for location
        streetAddress = intent.getStringExtra("location") + " ,New South Wales, Australia";

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //use string from intent to get LatLng position from the address
        LatLng position = getLocationURLFromAddress(MapsActivity.this, streetAddress);

        //get the markers from the position
        if (position != null) {

            //add markers
            mMap.addMarker(new MarkerOptions().position(position).title(streetAddress));

            //move camera to location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

            //zoom in camera
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
        }
        else
        {
            //if no position can be found set the address to default of the city of sydney
            LatLng sydney = new LatLng(-34, 151);

            //place the marker in sydney
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Toast.makeText(getApplicationContext(),"Could not get address.", Toast.LENGTH_SHORT).show();
        }

    }


    //this is a function which gets the LatLng value using the street address so the map can process it
    public static LatLng getLocationURLFromAddress(Context context,
                                                   String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        //get the lattitude and longitude from location name
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            //get location latitude and longiture and place them in LtLng return value
            LatLng ret = new LatLng(location.getLatitude(), location.getLongitude());


            //return LtLNg
            return ret;

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }
}