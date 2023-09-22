# Kind

## Create Cluster
`kind create cluster --config=./kind.yml --name=my-cluster`

## Change context
`kubectl cluster-info --context kind-my-cluster && kubectl get nodes`

## Delete cluster
`kind delete clusters my-cluster`