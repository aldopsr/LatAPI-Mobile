const mongoose = require("mongoose");

const BarangSchema = new mongoose.Schema({
  nama_barang: String,
  harga: Number,
  stok: Number,
  deskripsi: String
}, {
  collection: "barang" 
});

module.exports = mongoose.model("Barang", BarangSchema);