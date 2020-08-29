# Anomaly Detection Model

## Data

The original dataset is hosted on [Kaggle](https://archive.ics.uci.edu/ml/datasets/ElectricityLoadDiagrams20112014#).

During the course of the Hackathon, we have created multiple other anonymised datasets (kindly provided by [Solarify](https://solarify.ch/?lang=en)) that can be found under `./data`.

## Model Selection

Our approach is to combine expert knowhow and statistical models to detect anomalies. Further, for us to understand if our model has produced useful outputs, anomalies should be labelled based on a predefined set of characteristics (e.g. peak energy consumption, high baseload etc.).

### Thresholding

Very simple cases of anomalies should actually be detected using expert knowhow. For this, we defined a standardised expert model based on historic data:
1) Structure load curve on weekly basis

2) 

Possible Features:

- Baseload (especially during weekends)
  - total baseload
  - delta baseload
- Fast Fourier Transform (Frequency Analysis)
  - small variance in amplitude is good for energy producer
- Gradient might be interesting, because it might indicate unusual increases in energy consumption
- Negative values
- Zero values

### Stats/ML-based 

A more sophisticated ML/Stats-based model should be used to find unusual patterns, that are hard to detect by static rules and therefore might not be easily quantifiable by experts.

**Models**

We have tried the following models:

- Isolation Forest: Isolation Forest detects anomalies purely based on the fact that anomalies are data points that are few and different.
- 
- ARIMA Model: Time-series forecasting model and 

-- Minutely/Hourly Model (Online) --> Predictive Model
-- Daily Model (Historic)
-- Weekly Model (Historic)

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
docker build -t eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.3 .
docker push eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.3

docker run -p 5000:5000  eu.gcr.io/akenza-core-staging/anomaly-detector:v0.0.3
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
