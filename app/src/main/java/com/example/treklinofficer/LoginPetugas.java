package com.example.treklinofficer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.treklinofficer.api.ApiRequest;
import com.example.treklinofficer.api.Retroserver;
import com.example.treklinofficer.model.OfficerModel;
import com.example.treklinofficer.model.ResponseModel;
import com.example.treklinofficer.util.Session;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPetugas extends Activity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private Session session;
    private Double latitude, longitude;
    private static final int MY_MAPS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_petugas);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_MAPS_REQUEST_CODE);
        }

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(LoginPetugas.this);
        mFusedLocation.getLastLocation().addOnSuccessListener(LoginPetugas.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                session = new Session(LoginPetugas.this);

                if (email.isEmpty()) {
                    etEmail.setError("Email harus diisi!");
                }
                if (password.isEmpty()) {
                    etPassword.setError("Password harus diisi!");
                }
                if (!email.isEmpty() && !password.isEmpty()) {
                    ApiRequest api = Retroserver.getClient().create(ApiRequest.class);
                    Call<ResponseModel> login = api.loginOfficer(email, password, latitude, longitude);
                    login.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.body().getStatus().equals("1")) {
                                OfficerModel officerModel = response.body().getDataOfficer();
                                session.setId(officerModel.getId());
                                session.setNama(officerModel.getNama());
                                session.setEmail(officerModel.getEmail());
                                session.setLatitude(officerModel.getLatitude());
                                session.setLongitude(officerModel.getLongitude());
                                Intent pindah = new Intent(LoginPetugas.this, MainActivity.class);
                                startActivity(pindah);
                            }else{
                                Toast.makeText(LoginPetugas.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(LoginPetugas.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}