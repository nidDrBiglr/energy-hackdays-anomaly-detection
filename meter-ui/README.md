## meter-service-ui

### Dev

To locally start the development server, first run `npm install` and afterwards `npm start`. This will start a development server which is reachable at http://localhost:4200

To build and push the docker image, run the following commands

```
npm run build-prod
docker build --no-cache -t eu.gcr.io/akenza-core-staging/meter-service-ui:v0.0.1 .
docker push eu.gcr.io/akenza-core-staging/meter-service-ui:v0.0.1
```
