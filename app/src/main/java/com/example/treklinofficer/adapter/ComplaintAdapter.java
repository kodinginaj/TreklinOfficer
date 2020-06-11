package com.example.treklinofficer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.treklinofficer.R;
import com.example.treklinofficer.model.ComplaintModel;
import com.example.treklinofficer.model.UserModel;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintAdapter  extends RecyclerView.Adapter<ComplaintAdapter.TampungData>{

    private Context ctx;
    private List<ComplaintModel> listComplaint;


    public ComplaintAdapter(Context ctx, List<ComplaintModel> listComplaint){
        this.ctx = ctx;
        this.listComplaint = listComplaint;
    }

    @NonNull
    @Override
    public TampungData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.laporan_item,parent,false);
        TampungData tampungData = new TampungData(layout);
        return tampungData;
    }

    @Override
    public void onBindViewHolder(@NonNull TampungData holder, int position) {
        ComplaintModel dataComplaint = listComplaint.get(position);
        UserModel dataUser = dataComplaint.getUser();

        String alamat = getAddress(Double.parseDouble(dataUser.getLatitude()),Double.parseDouble(dataUser.getLongitude()));

        holder.tvNama.setText(dataUser.getNama());
        holder.tvAlamat.setText(alamat);
        holder.dataComplaint = dataComplaint;
    }

    @Override
    public int getItemCount() {
        return listComplaint.size();
    }

    class TampungData extends RecyclerView.ViewHolder {

        TextView tvNama, tvAlamat;
        ComplaintModel dataComplaint;
        Button btnBaca;

        private Button btnKomplain;
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;

        private TampungData(View v) {
            super(v);

            tvNama = v.findViewById(R.id.tvNama);
            tvAlamat = v.findViewById(R.id.tvAlamat);
            btnBaca = v.findViewById(R.id.btnBaca);

            btnBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogForm();
                }
            });


        }

        private void DialogForm() {
            dialog = new AlertDialog.Builder(ctx);
            inflater = LayoutInflater.from(ctx);
            dialogView = inflater.inflate(R.layout.modal_dialog, null);
            dialog.setView(dialogView);
            dialog.setCancelable(true);

            TextView komplain = dialogView.findViewById(R.id.etKomplain);
            komplain.setText(dataComplaint.getComplaint());

            dialog.setPositiveButton("TUTUP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
            });
            dialog.show();
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
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
}
