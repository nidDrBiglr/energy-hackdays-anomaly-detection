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
docker push eu.gcr.io/akenza-core-staging/meter-service:b0fd9040cf66979c56e4888e71ff88e52e6f5e38
```{.sh}   
