package com.example.lifesoruce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lifesoruce.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(42.6502, -71.3152))
                    .title("HemaCare Donor Center")
                    .snippet("672 Suffolk St 3rd floor, Lowell, MA 01854\n(978) 364-2130\nhttps://donor.hemacaredonorcenter.com/\n$300 for White Blood Cell"))
                    .setTag("https://donor.hemacaredonorcenter.com/");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7436, -71.1584))
                            .title("BioLife Plasma Services")
                            .snippet("90 Pleasant Valley St #102, Methuen, MA 01844\n(978) 552-3058\nhttps://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\nBioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7057, -71.1532))
                            .title("American Red Cross")
                            .snippet("60 Island St, Lawrence, MA 01840\n(978) 922-2224\nhttps://www.redcrossblood.org/give.html/find-drive\nVarious Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/find-drive");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.5651, -70.9859))
                            .title("American Red Cross Blood Donation Center")
                            .snippet("99 Rosewood Dr, Danvers, MA 01923\n(800) 733-2767\nhttps://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10\nVarious Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/drive-results?zipSponsor=01923&order=DISTANCE&range=10");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4516, -71.3760))
                            .title("Blood Donor Center at Emerson Hospital")
                            .snippet("131 Old Rd to 9 Acre Corner, Concord, MA 01742\n(978) 287-3778\nhttps://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing\nGift shop on-site"))
                    .setTag("https://www.emersonhospital.org/locations/blood-donation-center?utm_source=gmb&utm_medium=organic&utm_campaign=reputation&utm_content=listing");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4157, -71.1267))
                            .title("Biological Specialty Company")
                            .snippet("200 Boston Ave #1550, Medford, MA 02155\n(833) 464-2873\nhttps://www.biospecialty.com/schedule-appointment\nCompensation Based on Donation Type. Raffle For $50 Walmart Gift Card. $25 For Friend Referral"))
                    .setTag("https://www.biospecialty.com/schedule-appointment");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.4074, -71.0841))
                            .title("BioLife Plasma Services")
                            .snippet("640 Fellsway, Medford, MA 02155\n(781) 219-9476\nhttps://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022\nBioLife Card Mastercard for Plasma. New Donors Earn $900 With 8 Plasma Donations. $100 Refer Friend For Plasma"))
                    .setTag("https://www.biolifeplasma.com/locations/massachusetts/medford?utm_source=google&utm_medium=organic&utm_campaign=tom:reputationdotcom::medford,%20ma:02142022");

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.6553, -71.3247), 10f));

            googleMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity()));

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Uri uri = Uri.parse(String.valueOf(marker.getTag()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}