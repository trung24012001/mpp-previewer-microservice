# App start java server and table service

### Project 
Add project into this folder to start
- preview-mpp-java-server
- table-renderer-service

### Build image docker
```
docker build -t app-start .
```

### Run app
```
docker run -p8080:8080 -p8081:8081 app-start
```
