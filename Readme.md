## Start service

### Setup
change directory to preview-mpp-java-server to reinstall maven wrapper
```
mvn -N io.takari:maven:0.7.7:wrapper
```
maven wrapper [See more](https://github.com/takari/maven-wrapper.git)

### Build image docker
```
docker build -t app-start .
```

### Run app
```
docker run -p8080:8080 -p8081:8081 app-start
```

### Project 
- [preview-mpp-java-server](https://github.com/tunghoang/preview-mpp-java-server.git)
- [table-renderer-service](https://github.com/trung24012001/table-renderer-service.git)

