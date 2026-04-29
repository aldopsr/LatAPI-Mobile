package com.example.crudbarangapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudbarangapp.adapter.BarangAdapter;
import com.example.crudbarangapp.api.ApiService;
import com.example.crudbarangapp.api.RetrofitClient;
import com.example.crudbarangapp.model.Barang;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btnTambah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTambah = findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(v -> showDialogTambah());
        recyclerView = findViewById(R.id.recyclerBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

    }

    private void loadData() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Barang>> call = api.getBarang();

        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                if (response.isSuccessful()) {
                    List<Barang> list = response.body();
                    recyclerView.setAdapter(new BarangAdapter(list, barang -> showDialogEdit(barang)));
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal konek API", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showDialogTambah() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_barang, null);
        builder.setView(view);

        EditText edtNama = view.findViewById(R.id.edtNama);
        EditText edtHarga = view.findViewById(R.id.edtHarga);
        EditText edtStok = view.findViewById(R.id.edtStok);
        EditText edtDeskripsi = view.findViewById(R.id.edtDeskripsi);

        builder.setTitle("Tambah Barang");

        builder.setPositiveButton("Simpan", (dialog, which) -> {

            String nama = edtNama.getText().toString();
            int harga = Integer.parseInt(edtHarga.getText().toString());
            int stok = Integer.parseInt(edtStok.getText().toString());
            String deskripsi = edtDeskripsi.getText().toString();

            Barang barang = new Barang(nama, harga, stok, deskripsi);

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.tambahBarang(barang).enqueue(new Callback<Barang>() {
                @Override
                public void onResponse(Call<Barang> call, Response<Barang> response) {
                    Toast.makeText(MainActivity.this,"Berhasil tambah",Toast.LENGTH_SHORT).show();
                    loadData(); 
                }

                @Override
                public void onFailure(Call<Barang> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"Gagal tambah",Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }
    private void showDialogEdit(Barang barang) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_barang, null);
        builder.setView(view);

        EditText edtNama = view.findViewById(R.id.edtNama);
        EditText edtHarga = view.findViewById(R.id.edtHarga);
        EditText edtStok = view.findViewById(R.id.edtStok);
        EditText edtDeskripsi = view.findViewById(R.id.edtDeskripsi);

        // isi data lama
        edtNama.setText(barang.getNama_barang());
        edtHarga.setText(String.valueOf(barang.getHarga()));
        edtStok.setText(String.valueOf(barang.getStok()));
        edtDeskripsi.setText(barang.getDeskripsi());

        builder.setTitle("Edit / Hapus Barang");

        // UPDATE
        builder.setPositiveButton("Update", (dialog, which) -> {

            Barang update = new Barang(
                    edtNama.getText().toString(),
                    Integer.parseInt(edtHarga.getText().toString()),
                    Integer.parseInt(edtStok.getText().toString()),
                    edtDeskripsi.getText().toString()
            );

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.updateBarang(barang.get_id(), update).enqueue(new Callback<Barang>() {
                @Override
                public void onResponse(Call<Barang> call, Response<Barang> response) {
                    Toast.makeText(MainActivity.this,"Berhasil update",Toast.LENGTH_SHORT).show();
                    loadData();
                }

                @Override
                public void onFailure(Call<Barang> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"Gagal update",Toast.LENGTH_SHORT).show();
                }
            });
        });

        // DELETE
        builder.setNeutralButton("Delete", (dialog, which) -> {
            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.deleteBarang(barang.get_id()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(MainActivity.this,"Berhasil hapus",Toast.LENGTH_SHORT).show();
                    loadData();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"Gagal hapus",Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}
