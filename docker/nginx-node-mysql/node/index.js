import express, { json } from "express";
import mysql from "mysql";

const app = express();

const config = {
  host: "db",
  user: "root",
  password: "root",
  database: "nodedb",
};

const connection = mysql.createConnection(config);

app.get("/", (_, res) => {
  const sql = `select * from people`;
  connection.query(sql, (err, data) => {
    res.send(`<h1>Full Cycle Rocks!</h1><div>${JSON.stringify(data)}</div>`);
  });
});

app.listen(3000, () => {
  console.log("running at http://localhost:3000");
});
