package com.example.crudbarangapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudbarangapp.adapter.BarangAdapter;
import com.example.crudbarangapp.api.ApiService;
import com.example.crudbarangapp.api.RetrofitClient;
import com.example.crudbarangapp.model.Barang;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btnTambah;
    LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerBarang);
        btnTambah = findViewById(R.id.btnTambah);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        btnTambah.setOnClickListener(v -> showDialogTambah());

        loadData();
    }

    // ================= LOAD DATA =================
    private void loadData() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getBarang().enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Barang> list = response.body();

                    // EMPTY STATE
                    if (list.isEmpty()) {
                        layoutEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        layoutEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.setAdapter(new BarangAdapter(list, new BarangAdapter.OnItemClickListener() {
                            @Override
                            public void onEdit(Barang barang) {
                                showDialogEdit(barang);
                            }

                            @Override
                            public void onDelete(Barang barang) {
                                confirmDelete(barang);
                            }
                        }));
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal konek API", Toast.LENGTH_LONG).show();
            }
        });
    }

    // ================= TAMBAH =================
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
            try {
                Barang barang = new Barang(
                        edtNama.getText().toString(),
                        Integer.parseInt(edtHarga.getText().toString()),
                        Integer.parseInt(edtStok.getText().toString()),
                        edtDeskripsi.getText().toString()
                );

                ApiService api = RetrofitClient.getClient().create(ApiService.class);
                api.tambahBarang(barang).enqueue(new Callback<Barang>() {
                    @Override
                    public void onResponse(Call<Barang> call, Response<Barang> response) {
                        Toast.makeText(MainActivity.this, "Berhasil tambah", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onFailure(Call<Barang> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Gagal tambah", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Input tidak valid", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    // ================= EDIT =================
    private void showDialogEdit(Barang barang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_barang, null);
        builder.setView(view);

        EditText edtNama = view.findViewById(R.id.edtNama);
        EditText edtHarga = view.findViewById(R.id.edtHarga);
        EditText edtStok = view.findViewById(R.id.edtStok);
        EditText edtDeskripsi = view.findViewById(R.id.edtDeskripsi);

        edtNama.setText(barang.getNama_barang());
        edtHarga.setText(String.valueOf(barang.getHarga()));
        edtStok.setText(String.valueOf(barang.getStok()));
        edtDeskripsi.setText(barang.getDeskripsi());

        builder.setTitle("Edit Barang");

        builder.setPositiveButton("Update", (dialog, which) -> {
            try {
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
                        Toast.makeText(MainActivity.this, "Berhasil update", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onFailure(Call<Barang> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Input tidak valid", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    // ================= DELETE =================
    private void confirmDelete(Barang barang) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Barang")
                .setMessage("Yakin mau hapus " + barang.getNama_barang() + "?")
                .setPositiveButton("Hapus", (dialog, which) -> deleteBarang(barang))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deleteBarang(Barang barang) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.deleteBarang(barang.get_id()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Berhasil hapus", Toast.LENGTH_SHORT).show();
                loadData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal hapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}