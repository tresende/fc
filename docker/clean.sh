podman stop $(podman ps -a -q)
podman rm $(podman ps -a -q)
podman rmi $(podman images -q)
#podman network rm consul
podman network rm node-network
podman volume prune