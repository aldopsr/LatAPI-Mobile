package com.example.crudbarangapp.model;

public class Barang {

    private String _id;
    private String nama_barang;
    private int harga;
    private int stok;
    private String deskripsi;

    public String get_id() { return _id; }
    public String getNama_barang() { return nama_barang; }
    public int getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getDeskripsi() { return deskripsi;}

    public Barang(String nama_barang, int harga, int stok, String deskripsi) {
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.stok = stok;
        this.deskripsi = deskripsi;
    }
}

