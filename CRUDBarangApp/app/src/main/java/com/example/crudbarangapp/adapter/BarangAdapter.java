package com.example.crudbarangapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudbarangapp.R;
import com.example.crudbarangapp.model.Barang;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {

    private List<Barang> listBarang;
    private OnItemClickListener listener;

    public BarangAdapter(List<Barang> listBarang, OnItemClickListener listener) {
        this.listBarang = listBarang;
        this.listener = listener;
    }

    // ✅ UPDATE INTERFACE (2 METHOD)
    public interface OnItemClickListener {
        void onEdit(Barang barang);
        void onDelete(Barang barang);
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

        // ✅ tombol EDIT
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(barang);
            }
        });

        // ✅ tombol HAPUS
        holder.btnHapus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(barang);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNama, txtHarga, txtStok, txtDeskripsi;
        MaterialButton btnEdit, btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.txtNama);
            txtHarga = itemView.findViewById(R.id.txtHarga);
            txtStok = itemView.findViewById(R.id.txtStok);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsi);

            // ✅ ambil tombol dari XML kamu
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}