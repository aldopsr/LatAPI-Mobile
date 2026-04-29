const express = require("express");
const router = express.Router();
const Barang = require("../models/Barang");

// GET ALL
router.get("/", async (req, res) => {
  const data = await Barang.find();
  res.json(data);
});

// GET BY ID
router.get("/:id", async (req, res) => {
  const data = await Barang.findById(req.params.id);
  res.json(data);
});

// CREATE
router.post("/", async (req, res) => {
  const barang = new Barang(req.body);
  const result = await barang.save();
  res.json(result);
});

// UPDATE
router.put("/:id", async (req, res) => {
  const result = await Barang.findByIdAndUpdate(req.params.id, req.body, {
    new: true,
  });
  res.json(result);
});

// DELETE
router.delete("/:id", async (req, res) => {
  await Barang.findByIdAndDelete(req.params.id);
  res.json({ message: "Barang deleted" });
});

module.exports = router;
