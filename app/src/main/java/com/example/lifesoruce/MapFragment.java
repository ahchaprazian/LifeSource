package com.example.lifesoruce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lifesoruce.databinding.FragmentMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;

    private static final String TAG = "MapFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean myLocationPermissionGranted = false;
    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap myMap;
    private FusedLocationProviderClient myFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 10f;
    private static final LatLng DEFAULT_LOCATION = new LatLng(42.6502, -71.3239);

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            myMap = googleMap;

            if (myLocationPermissionGranted) {
                getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                myMap.setMyLocationEnabled(true);
                myMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
            }

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.6502, -71.3152))
                            .title("HemaCare Donor Center")
                            .snippet("672 Suffolk St 3rd floor, Lowell, MA 01854\n(978) 364-2130\nhttps://donor.hemacaredonorcenter.com/\n$300 for White Blood Cell"))
                    .setTag("https://donor.hemacaredonorcenter.com/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7436, -71.1584))
                            .title("BioLife Plasma Services")
                            .snippet("90 Pleasant Valley St #102, Methuen, MA 01844\n(978) 552-3058\nhttps://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\nBioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7057, -71.1532))
                            .title("American Red Cross")
                            .snippet("60 Island St, Lawrence, MA 01840\n(978) 922-2224\nhttps://www.redcrossblood.org/give.html/find-drive\nVarious Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/find-drive");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.5651, -70.9859))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("99 Rosewood Dr, Danvers, MA 01923\n(800) 733-2767\nhttps://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10\nVarious Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4516, -71.3760))
                            .title("Blood Donor Center at Emerson Hospital")
                            .snippet("131 Old Rd to 9 Acre Corner, Concord, MA 01742\n(978) 287-3778\nhttps://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing\nGift shop on-site"))
                    .setTag("https://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4157, -71.1267))
                            .title("Biological Specialty Company")
                            .snippet("200 Boston Ave #1550, Medford, MA 02155\n(833) 464-2873\nhttps://www.biospecialty.com/schedule-appointment\nCompensation Based on Donation Type. Raffle For $50 Walmart Gift Card. $25 For Friend Referral"))
                    .setTag("https://www.biospecialty.com/schedule-appointment");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4074, -71.0841))
                            .title("BioLife Plasma Services")
                            .snippet("640 Fellsway, Medford, MA 02155\n(781) 219-9476\nhttps://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\nBioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.6553, -71.3247), 10f));

            myMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity()));

            myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Uri uri = Uri.parse(String.valueOf(marker.getTag()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    };

    private void getDeviceLocation() {
        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (myLocationPermissionGranted) {
                Task location = myFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLing, float zoom) {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLing, zoom));
    }

    private void initMAp() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getLocationPermission() {
        //String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                myLocationPermissionGranted = true;
                initMAp();
            } else {
                //ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
                requestPermissionLauncher.launch(COARSE_LOCATION);
                //initMAp();
            }
        } else {
            //ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            requestPermissionLauncher.launch(FINE_LOCATION);
            //initMAp();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        myLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    myLocationPermissionGranted = true;
                    //initialize our map
                    initMAp();
                }
            }
        }
    }*/

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                // permission granted
                myLocationPermissionGranted = true;
                initMAp();
            } else {
                // permission not granted
                myLocationPermissionGranted = false;
                initMAp();
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getLocationPermission();

        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}