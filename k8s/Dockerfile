FROM golang:1.15
COPY . .
RUN go build -o server .
CMD ["./server"]
# podman build -t tresende/hello-go .
# podman run --rm -p 8000:8000 tresende/hello-go