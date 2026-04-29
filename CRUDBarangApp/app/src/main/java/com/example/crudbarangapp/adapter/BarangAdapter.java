package com.example.crudbarangapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudbarangapp.R;
import com.example.crudbarangapp.model.Barang;

import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {

    private List<Barang> listBarang;
    private OnItemClickListener listener;

    // Constructor WAJIB pakai listener
    public BarangAdapter(List<Barang> listBarang, OnItemClickListener listener) {
        this.listBarang = listBarang;
        this.listener = listener;
    }

    // Interface klik item
    public interface OnItemClickListener {
        void onClick(Barang barang);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barang barang = listBarang.get(position);

        holder.txtNama.setText(barang.getNama_barang());
        holder.txtHarga.setText("Rp " + barang.getHarga());
        holder.txtStok.setText("Stok : " + barang.getStok());
        holder.txtDeskripsi.setText(barang.getDeskripsi());

        // klik item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(barang);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNama, txtHarga, txtStok, txtDeskripsi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNama);
            txtHarga = itemView.findViewById(R.id.txtHarga);
            txtStok = itemView.findViewById(R.id.txtStok);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsi);
        }
    }
}