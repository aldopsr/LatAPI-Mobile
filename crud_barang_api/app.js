require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

app.use("/barang", require("./routes/barangRoutes"));

async function startServer() {
  try {
    await mongoose.connect(process.env.MONGO_URI);
    console.log("MongoDB Connected");

    app.listen(process.env.PORT, () => {
      console.log("Server running on port " + process.env.PORT);
    });

  } catch (error) {
    console.error("MongoDB connection failed:", error);
  }
}

startServer();