FROM golang:1.19
WORKDIR /app
RUN go mod init teste
COPY ./ci .
RUN pwd
RUN go build -o math
CMD ["./math"]