```bash
curl --location --request PUT 'http://localhost:8083/connectors/mysql-debezium/config' \
--header 'Content-Type: application/json' \
--data '{
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "true",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable": "true",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "debezium",
    "database.server.id": "10000",
    "database.server.name": "adm_videos_mysql",
    "database.allowPublicKeyRetrieval": "true",
    "database.include.list": "adm_videos",
    "table.include.list": "adm_videos.categories,adm_videos.cast_members",
    "database.history.kafka.bootstrap.servers": "kafka:9092",
    "database.history.kafka.topic": "adm_videos.dbhistory",
    "include.schema.changes": "false",
    "schema.enable": "false"
}
'
```

```sql

ALTER USER 'debezium'@'%' IDENTIFIED WITH mysql_native_password BY 'debezium';
FLUSH PRIVILEGES;


```