docker exec -it consulserver01 sh

mkdir /etc/consul.d
mkdir /var/lib/consul

consul agent -server -bootstrap-expect=3 -node=consulserver01 -bind=10.89.0.11 -data-dir=/var/lib/consul -config-dir=/etc/consul.d
consul agent -server -bootstrap-expect=3 -node=consulserver02 -bind=10.89.0.10 -data-dir=/var/lib/consul -config-dir=/etc/consul.d
consul agent -server -bootstrap-expect=3 -node=consulserver03 -bind=10.89.0.12 -data-dir=/var/lib/consul -config-dir=/etc/consul.d

docker exec -it consulclient02 sh
consul agent -node=consulclient02 -bind=10.89.0.13  -data-dir=/var/lib/consul -config-dir=/etc/consul.d -retry-join=10.89.0.11


apk -U add bind-tools
dig @localhost -p 8600 nginx.service.consul
curl localhost:8500/v1/catalog/services
consul catalog nodes -service nginx