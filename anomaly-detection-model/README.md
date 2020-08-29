# Anomaly Detection Model

## Data

The original dataset is hosted on [Kaggle](https://archive.ics.uci.edu/ml/datasets/ElectricityLoadDiagrams20112014#).


## Dev Setup

Setup uses [Virtualenv](https://virtualenv.pypa.io/en/stable/).

```{.sh}
pip3 install virtualenv
virtualenv -p python3 .

source ./bin/activate
python3 -m pip install --upgrade pip
pip install notebook --upgrade
pip install jupyter --upgrade
pip install numpy --upgrade
pip install sklearn --upgrade
pip install matplotlib --upgrade
pip install chart_studio --upgrade
pip install statsmodels --upgrade
pip install pmdarima --upgrade
pip install pandas --upgrade

jupyter notebook
```

Build application:

```
docker build -t eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.6 .
docker push eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.6

docker run -p 5000:5000  eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.5
curl -X POST 'http://localhost:5000/predict'
```

## Useful Links

- https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.to_datetime.html
- https://towardsdatascience.com/anomaly-detection-with-time-series-forecasting-c34c6d04b24a#:~:text=For%20eg%3A%20revenue%20at%20a,SARIMA%2C%20LSTM%2C%20Holtwinters%20etc.
- https://www.kaggle.com/adithya44/anomaly-detection-with-time-series-forecasting
- https://www.statsmodels.org/dev/generated/statsmodels.tsa.statespace.sarimax.SARIMAX.html
- https://github.com/alkaline-ml/pmdarima
- https://medium.com/@jetnew/anomaly-detection-of-time-series-data-e0cb6b382e33
- https://github.com/rob-med/awesome-TS-anomaly-detection
- https://elering.ee/en/smart-grid-development
- https://github.com/xiufengliu/DataGenerator-Cluster-Version
- https://www.researchgate.net/profile/Xiufeng_Liu2/publication/280830589_Streamlining_Smart_Meter_Data_Analytics/links/55c867e308aebc967df89f1d.pdf
- https://www.tigera.io/blog/deploy-your-first-deep-learning-model-on-kubernetes-with-python-keras-flask-and-docker/