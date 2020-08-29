# meter-service


## Running the app locally

Run the following commands:

```{.sh}
make dev
```

## Troubleshooting

- Clearing the Maven cache
```
    mvn dependency:purge-local-repository
```

```{.sh}
mvn -Dmaven.test.skip=true -Dgoogle-project=akenza-core-staging package dockerfile:build

mvn package -DskipTests=true
docker build --build-arg JAR_FILE=target/meter-service-0.0.1-SNAPSHOT.jar -t eu.gcr.io/akenza-core-staging/meter-service:v0.0.1 . 
docker push eu.gcr.io/akenza-core-staging/meter-service:362072c8dab3fd0ef1125897d3ffa11bf0e4396c
```{.sh}   
