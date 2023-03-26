FROM golang:1.16-alpine as build

WORKDIR /app
COPY src/main.go .

RUN go build -o hello main.go

FROM scratch

WORKDIR /app
COPY --from=build /app/hello hello

ENTRYPOINT ["/app/hello"]

# docker build -t tresende/go:prod . 
# docker run --rm -it --name go tresende/go:prod