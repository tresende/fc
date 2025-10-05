## MySQL setup

https://debezium.io/documentation/reference/2.6/connectors/mysql.html#setting-up-mysql

```
mysql> CREATE USER 'debezium'@'localhost' IDENTIFIED BY 'debezium';

mysql> GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium' IDENTIFIED BY 'debezium';

mysql> FLUSH PRIVILEGES;
```