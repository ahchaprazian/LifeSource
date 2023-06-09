// File to handle majority of map fragment's data and logic.
// Handles displaying map via Google Maps SDK
// with map pins and related controls.
// Uses relevant Google Maps libraries for
// maps functionalities, including location services.

package com.example.lifesoruce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.lifesoruce.databinding.FragmentMapBinding;
import com.example.lifesoruce.databinding.FragmentNewsBinding;
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
    private GoogleMap myMap;
    private FusedLocationProviderClient myFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 10f;
    private static final LatLng DEFAULT_LOCATION = new LatLng(42.6502, -71.3239);
    private Location myCurrentLocation;

    /**
     * Sets up the map with all the map pins to be displayed and navigation functions
     * once it ready for use. If user location permissions were granted, sets the map
     * to indicate the user's last known location and enable a location pinpoint button
     * to focus the map to that location. Configures a home button to reset the map focus
     * to be the default location, UCrossing, and enables zoom controls. Enables custom
     * info windows for map pins to display relevant information on those donation
     * centers, and these info windows can be clicked to redirect to their website.
     *
     * @param googleMap The Google Map object that is ready to be used
     */
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // Sets map object.
            myMap = googleMap;

            // Configures map's zoom based on whether
            // location permissions were granted.
            // Otherwise, use default zoom.
            if (myLocationPermissionGranted) {
                getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // Display user location.
                myMap.setMyLocationEnabled(true);
                // Sets user location button.
                myMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        getDeviceLocation();
                        return true;
                    }
                });
            } else {
                moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
            }
            myMap.getUiSettings().setZoomControlsEnabled(true);

            // Add map pins with relevant information.
            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.6502, -71.3152))
                            .title("HemaCare Donor Center")
                            .snippet("\u2022 672 Suffolk St 3rd floor, Lowell, MA 01854\n\n\u2022 (978) 364-2130\n\n\u2022 https://donor.hemacaredonorcenter.com/\n\n\u2022 $300 for White Blood Cell"))
                    .setTag("https://donor.hemacaredonorcenter.com/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7436, -71.1584))
                            .title("BioLife Plasma Services")
                            .snippet("\u2022 90 Pleasant Valley St #102, Methuen, MA 01844\n\n\u2022 (978) 552-3058\n\n\u2022 https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\n\n\u2022 BioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7057, -71.1532))
                            .title("American Red Cross")
                            .snippet("\u2022 60 Island St, Lawrence, MA 01840\n\n\u2022 (978) 922-2224\n\n\u2022 https://www.redcrossblood.org/give.html/find-drive\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/find-drive");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.5651, -70.9859))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 99 Rosewood Dr, Danvers, MA 01923\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4516, -71.3760))
                            .title("Blood Donor Center at Emerson Hospital")
                            .snippet("\u2022 131 Old Rd to 9 Acre Corner, Concord, MA 01742\n\n\u2022 (978) 287-3778\n\n\u2022 https://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing\n\n\u2022 Gift shop on-site"))
                    .setTag("https://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4157, -71.1267))
                            .title("Biological Specialty Company")
                            .snippet("\u2022 200 Boston Ave #1550, Medford, MA 02155\n\n\u2022 (833) 464-2873\n\n\u2022 https://www.biospecialty.com/schedule-appointment\n\n\u2022 Compensation Based on Donation Type. Raffle For $50 Walmart Gift Card. $25 For Friend Referral"))
                    .setTag("https://www.biospecialty.com/schedule-appointment");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4074, -71.0841))
                            .title("BioLife Plasma Services")
                            .snippet("\u2022 640 Fellsway, Medford, MA 02155\n\n\u2022 (781) 219-9476\n\n\u2022 https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\n\n\u2022 BioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.3630, -71.0673))
                            .title("Massachusetts General Hospital Blood Donor Center")
                            .snippet("\u2022 50 Blossom St, GRJ 120, Boston, MA 02114\n\n\u2022 (617) 724-9699\n\n\u2022 https://www.massgeneral.org/blood-donor/contact\n\n\u2022 Refreshments and Helping Save Lives"))
                    .setTag("https://www.massgeneral.org/blood-donor/contact");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.3497, -71.0650))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 274 Tremont St, Boston, MA 02116\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02116&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02116&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.3385, -71.1054))
                            .title("Blood Donor Center at Boston Children's Hospital")
                            .snippet("\u2022 333 Longwood Ave, Boston, MA 02115\n\n\u2022 (800) 490-2673\n\n\u2022 https://www.childrenshospital.org/ways-help/donate-blood\n\n\u2022 Refreshments and Help Save Young Patients"))
                    .setTag("https://www.childrenshospital.org/ways-help/donate-blood");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.3642, -71.1358))
                            .title("StemExpress Stem Cell Collection Center - BOSTON")
                            .snippet("\u2022 1230 Soldiers Field Rd Suite 2, Boston, MA 02135\n\n\u2022 (877) 900-7836\n\n\u2022 https://stemexpress.com/donate-blood/become-a-blood-donor/\n\n\u2022 Between $25 - $1000 Based on Donation Type"))
                    .setTag("https://stemexpress.com/donate-blood/become-a-blood-donor/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2522, -71.0033))
                            .title("LeukoLab")
                            .snippet("\u2022 1250 Hancock St Suite 120S, Quincy, MA 02169\n\n\u2022 (510) 671-8697\n\n\u2022 https://www.leukolab.com/\n\n\u2022 Visa Debit Card of $250 - $1650 Based on Donation Type"))
                    .setTag("https://www.leukolab.com/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2003, -70.9551))
                            .title("Weymouth Red Cross Blood and Plasma Donation Center")
                            .snippet("\u2022 208 Main St, Weymouth, MA 02188\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02188&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02188&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.232, -71.1689))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 180 Rustcraft Rd, Dedham, MA 02026\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02026&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02026&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2851, -71.4189))
                            .title("MetroWest Medical Center Blood Donation Center")
                            .snippet("\u2022 115 Lincoln St, Framingham, MA 01702\n\u2022 508) 383-1230\n\n\u2022 http://bloodbanker.com/banks/bloodbank.php?center=MetroWest+Medical+Center+Blood+Bank&ID=1817\n\n\u2022 Gift Cards and Shirts Based on Donnation Type"))
                    .setTag("http://bloodbanker.com/banks/bloodbank.php?center=MetroWest+Medical+Center+Blood+Bank&ID=1817");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.3553, -71.6402))
                            .title("Da-Mar Biological Inc")
                            .snippet("\u2022 24 Morse Circle Northborough, MA 01532\n\n\u2022 (508) 393-4789\n\n\u2022 https://www.manta.com/c/mm7yyyn/da-mar-biological-inc\n\n\u2022 Help Save a Life"))
                    .setTag("https://www.manta.com/c/mm7yyyn/da-mar-biological-inc");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2812, -71.7651))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 381 Plantation St, Worcester, MA 01605\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01605&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01605&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.0823, -70.9889))
                            .title("Octapharma Plasma")
                            .snippet("\u2022 752 Crescent St, Brockton, MA 02302\n\n\u2022 (508) 436-2587\n\n\u2022 https://www.octapharmaplasma.com/plasma-donation/brockton-ma-126/\n\n\u2022 Prepaid Card Payment From Plasma"))
                    .setTag("https://www.octapharmaplasma.com/plasma-donation/brockton-ma-126/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2413, -71.8419))
                            .title("BioLife Plasma Services")
                            .snippet("\u2022 68 Stafford St, Worcester, MA 01603\n\n\u2022 (508) 713-0133\n\n\u2022 https://www.biolifeplasma.com/locations/massachusetts/worcester?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::worcester,%20ma:08042021&gclid=Cj0KCQjwi46iBhDyARIsAE3nVra04uOqR_fPDmi8tNL059Q9_wspUVY1kRPDZ8UE-8Me_rh4eKF9LWkaAjUAEALw_wcB\n\n\u2022 BioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/worcester?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::worcester,%20ma:08042021&gclid=Cj0KCQjwi46iBhDyARIsAE3nVra04uOqR_fPDmi8tNL059Q9_wspUVY1kRPDZ8UE-8Me_rh4eKF9LWkaAjUAEALw_wcB");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.9051, -71.0551))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 275 New State Hwy, Raynham, MA 02767\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02767&order=DISTANCE&range=10\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=02767&order=DISTANCE&range=10");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.0076, -70.7326))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("\u2022 160 Summer St B, Kingston, MA 02364\n\n\u2022 (800) 733-2767\n\n\u2022 https://www.redcrossblood.org/give.html/drive-results?zipSponsor=KingstonMA\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=KingstonMA");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.6507, -70.2850))
                            .title("American Red Cross")
                            .snippet("\u2022 286 South St, Hyannis, MA 02601\n\n\u2022 (508) 775-1540\n\n\u2022 https://www.redcross.org/local/massachusetts/about-us/locations/southeastern-massachusetts.html\n\n\u2022 Various Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcross.org/local/massachusetts/about-us/locations/southeastern-massachusetts.html");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.657, -70.2696))
                            .title("Cape Cod Healthcare - Blood Mobile")
                            .snippet("\u2022 17 Main St, Hyannis, MA 02601\n\n\u2022 (508) 862-5663\n\n\u2022 https://www.capecodhealth.org/ways-to-give/blood-center/\n\n\u2022 Help Save a Life"))
                    .setTag("https://www.capecodhealth.org/ways-to-give/blood-center/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.9972, -71.2102))
                            .title("Precision For Medicine Donor Center")
                            .snippet("\u2022 800 S Main St #304, Mansfield, MA 02048\n\n\u2022 (774) 265-1685\n\n\u2022 https://www.precisionbiospecimens.com/study/\n\n\u2022 Help Disease Research"))
                    .setTag("https://www.precisionbiospecimens.com/study/");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.9225, -71.3611))
                            .title("BioLife Plasma Services")
                            .snippet("\u2022 287 Washington St #5, Attleboro, MA 02703\n\n\u2022 (508) 761-2902\n\n\u2022 https://www.biolifeplasma.com/locations/massachusetts/attleboro?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::attleboro,%20ma:02062023\n\n\u2022 BioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/attleboro?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::attleboro,%20ma:02062023");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(41.6782, -71.1604))
                            .title("Biomat USA")
                            .snippet("\u2022 370 Rhode Island Ave, Fall River, MA 02721\n\n\u2022 (508) 675-4945\n\n\u2022 https://www.grifolsplasma.com/en/-/fall-river-ma?utm_source=gmb&utm_medium=yext\n\n\u2022 Prepaid Grifolis Visa Debit Card Refilled per Donation. Up to $640 a Month for New Donors"))
                    .setTag("https://www.grifolsplasma.com/en/-/fall-river-ma?utm_source=gmb&utm_medium=yext");

            myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.2752, -71.7605))
                            .title("Biomat USA")
                            .snippet("\u2022 403 Belmont St Suite 400, Worcester, MA 01604\n\n\u2022 (508) 793-2905\n\n\u2022 https://www.grifolsplasma.com/en/-/belmontst-worcester-ma\n\n\u2022 Prepaid Grifolis Visa Debit Card Refilled per Donation. Up to $640 a Month for New Donors"))
                    .setTag("https://www.grifolsplasma.com/en/-/belmontst-worcester-ma");

            // Enables custom info windows for map pins.
            myMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity()));

            // Set listener for info windows to redirected to
            // donation center's website.
            myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Uri uri = Uri.parse(String.valueOf(marker.getTag()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            // Set listener for home button to reset map zoom.
            binding.homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
                }
            });
        }
    };

    /**
     * Retrieves user's last known location on the device. Attemps to do so
     * if location permission were previously granted. Handles whether the
     * retrieved location is valid or not. If valid, the map sets focus to
     * that location. Otherwise, the app alerts the user that no location
     * could be found (null), and instead focuses the map on the default
     * location, UCrossing.
     */
    private void getDeviceLocation() {
        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Try getting user location data if permission granted.
        try {
            if (myLocationPermissionGranted) {
                // Try getting user's last known location.
                Task location = myFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && (Location) task.getResult() != null) {
                            // If user location is known, set map zoom accordingly.
                            myCurrentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()), DEFAULT_ZOOM);
                            Log.d(TAG, "onComplete: found location!");
                            Log.d(TAG, String.valueOf(myCurrentLocation.getLatitude()) + "-" + String.valueOf(myCurrentLocation.getLongitude()));
                        } else {
                            // If user location is not known, use default map zoom.
                            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
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

    // Function to reset map camera based on argument
    // latitude longitude and zoom values.
    private void moveCamera(LatLng latLing, float zoom) {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLing, zoom));
    }

    // Function to initialize map for display.
    private void initMAp() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    /**
     * Checks whether user permissions are enabled for location services. If so, sets
     * a boolean variable as such and initializes the map. Otherwise, requests the
     * user for the necessary location permissions.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // If location permissions are granted, initialize map.
                myLocationPermissionGranted = true;
                initMAp();
            } else {
                // If coarse location permission not granted, request it.
                requestPermissionLauncher.launch(COARSE_LOCATION);
            }
        } else {
            // If fine location permission not granted, request it.
            requestPermissionLauncher.launch(FINE_LOCATION);
        }
    }

    // Function to handle user's response to app requesting user location permissions.
    /**
     * Handles the user's response to being requested user location permissions.
     * If permission was granted, sets a boolean variable as such and initializes
     * the map using that location data. Otherwise, sets the boolean variable
     * accordingly and initializes the map with a default location, UCrossing.
     */
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                // If permission granted.
                myLocationPermissionGranted = true;
                initMAp();
            } else {
                // If permission not granted.
                myLocationPermissionGranted = false;
                initMAp();
            }
        }
    });

    // Function for when fragment view is created.
    // Inflates fragment via view binding and sets up map.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getLocationPermission();

        return view;
    }

    // Function for when fragment view is destroyed.
    // Resets view binding.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}