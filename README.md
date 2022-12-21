# Currently, in dev. <br /> Info here may be outdated and/or wrong.
# TeamPlay
<img src="assets/banner.png" alt="banner.png">

# About
Organize your company work

## Setting up
```sh
$ git clone https://github.com/26twentysix/TeamPlay.git
$ cd TeamPlay
```
### Run tests with maven
```sh
$ mvn clean test
```

### Run app with maven
```sh
$ mvn clean compile spring-boot:run
```

### Run standalone app from console
```sh
$ mvn clean compile package
$ cd target
$ java -jar TeamPlay-{current app version}.jar
```

### Run app without maven installed
```sh
$ ./mvnw spring-boot:run
```