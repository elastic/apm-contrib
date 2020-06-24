"use strict";

const express = require("express");
const axios = require("axios");

const app = express();

app.get("/", (req, res) => {
  axios
    .get(`http://backend:8081/backend`)
    .then(result => {
      res.send(result.data);
    })
    .catch(err => {
      console.error(err);
      res.status(500).send();
    });
});

const PORT = 8080;
app.listen(PORT, () => {
  console.log(`Listening for requests on http://localhost:${PORT}`);
});
