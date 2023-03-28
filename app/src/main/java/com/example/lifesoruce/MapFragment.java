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
                            .snippet("90 Pleasant Valley St #102, Methuen, MA 01844\n(978) 552-3058\nhttps://www.biolifeplasma.com/registration\nBioLife Card Mastercard for Plasma"))
                    .setTag("https://www.biolifeplasma.com/registration");

            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(42.7057, -71.1532))
                            .title("American Red Cross")
                            .snippet("60 Island St, Lawrence, MA 01840\n(978) 922-2224\nhttps://www.redcrossblood.org/give.html/find-drive\nVarious Merch per Number of Donations Yearly"))
                    .setTag("https://www.redcrossblood.org/give.html/find-drive");

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