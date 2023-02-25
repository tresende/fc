import express from "express";
import mysql from "mysql";

const app = express();

const config = {
  host: "db",
  user: "root",
  password: "root",
  database: "nodedb",
};

const connection = mysql.createConnection(config);

const sql = `INSERT INTO people(name) values('Thiago')`;
connection.query(sql);
connection.end();

app.get("/", (_, res) => {
  res.send("Hello World!");
});

app.listen(3000, () => {
  console.log("running at http://localhost:3000");
});
