package com.example.treklinofficer.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treklinofficer.LoginPetugas;
import com.example.treklinofficer.MainActivity;
import com.example.treklinofficer.R;
import com.example.treklinofficer.adapter.ComplaintAdapter;
import com.example.treklinofficer.api.ApiRequest;
import com.example.treklinofficer.api.Retroserver;
import com.example.treklinofficer.model.ComplaintModel;
import com.example.treklinofficer.model.ResponseModel;
import com.example.treklinofficer.model.UserModel;
import com.example.treklinofficer.util.Session;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    private static final int MY_MAPS_REQUEST_CODE = 100;

    private RecyclerView tampilComplaint;
    private RecyclerView.LayoutManager layoutComplaint;
    private RecyclerView.Adapter adapterComplaint;

    private List<ComplaintModel> listComplaint;


    Double latitude, longitude;
    TextView tvKoordinat, tvNama ;
    Session session;
    List<Marker> marker = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_MAPS_REQUEST_CODE);
        }

        session = new Session(getActivity());
        tvNama = root.findViewById(R.id.tvNama);
        tvNama.setText("Hallo, "+session.getNama());
        tvKoordinat = root.findViewById(R.id.tvKoordinat);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tampilComplaint = root.findViewById(R.id.rvKomplain);
        layoutComplaint = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        tampilComplaint.setLayoutManager(layoutComplaint);

        zoomMap();
        complaint();

        return root;
    }

    public void zoomMap(){
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    String alamat = getAddress(latitude,longitude);
                    session.setAlamat(alamat);
                    tvKoordinat.setText(alamat);

                    LatLng posisi = new LatLng(latitude, longitude);
                    float zoomLevel = 16.0f;

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, zoomLevel));

                    refresh(10000);
                }
            }
        });

    }

    public void complaint(){
        FusedLocationProviderClient mmFusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        mmFusedLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    if(marker.size()>0){
                        for(int i = 0; i<marker.size(); i++){
                            marker.get(i).remove();
                        }
                    }

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    String alamat = getAddress(latitude,longitude);
                    session.setAlamat(alamat);
                    tvKoordinat.setText(alamat);

                    session.setLatitude(latitude.toString());
                    session.setLongitude(longitude.toString());

                    String id = session.getId();

                    ApiRequest api = Retroserver.getClient().create(ApiRequest.class);
                    Call<ResponseModel> getComplaint = api.getComplaint(id);
                    getComplaint.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            listComplaint = response.body().getDataComplaint();


                            for (int i=0;i<listComplaint.size();i++) {
                                ComplaintModel complaint = listComplaint.get(i);
                                UserModel user = complaint.getUser();

                                double latitude = Double.parseDouble(complaint.getLatitude());
                                double longtitude = Double.parseDouble(complaint.getLongitude());
                                LatLng posisi = new LatLng(latitude, longtitude);

                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker2);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

//                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker2);

                                marker.add(mMap.addMarker(new MarkerOptions()
                                        .position(posisi)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .title(user.getNama())));

                            }

                            Log.d("TER",""+listComplaint);
                            adapterComplaint = new ComplaintAdapter(getContext(), listComplaint);
                            tampilComplaint.setAdapter(adapterComplaint);
                            adapterComplaint.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(getActivity(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_MAPS_REQUEST_CODE);
        }else {
            mMap.setMyLocationEnabled(true);
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getThoroughfare());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private void refresh(int milisecond){
        Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                complaint();
                refresh(milisecond);
            }
        };
        handler.postDelayed(runnable,milisecond);
    }

}