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
docker push eu.gcr.io/akenza-core-staging/meter-service:234cc645ab5c92139bccf5b779b1a6ad1b034ff0
```{.sh}   
