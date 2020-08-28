- https://elering.ee/en/smart-grid-development

Clemap

Energy consumption in buildings and industry is often wasted due to user behaviour, human error, and poorly performing equipment. In this context, identifying abnormal consumption power behavior can be an important part of reducing peak energy consumption and changing undesirable user behavior. With the widespread rollouts of smart meters, normal operating consumption can be learned over time and used to identify or flag abnormal consumption. Such information can help indicate to users when their equipment is not operating as normal and can help to change user behavior or to even indicate what the problem appliances may be to implement lasting changes.

This challenge is looking for data scientists to apply their skills to an anomaly detection problem using smart meter data. Ideally, such an algorithm should begin to operate after as little as 3 months and should improve over time. A platform to visualise the anomalies would also be useful. Users can select any type of machine learning algorithms that they wish to in order to detect the anomalies from the dat

Goal:

## Dev Setup

Setup uses [Virtualenv](https://virtualenv.pypa.io/en/stable/).

```{.sh}
pip3 install virtualenv
virtualenv -p python3 .
source ./bin/activate
python3 -m pip install --upgrade pip
pip install notebook --upgrade
pip install jupyter
pip install sklearn
pip install matplotlib
pip install chart_studio
pip install statsmodels
pip install pmdarima
pip install pandas

jupyter notebook
```

https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.to_datetime.html

https://towardsdatascience.com/anomaly-detection-with-time-series-forecasting-c34c6d04b24a#:~:text=For%20eg%3A%20revenue%20at%20a,SARIMA%2C%20LSTM%2C%20Holtwinters%20etc.
https://www.kaggle.com/adithya44/anomaly-detection-with-time-series-forecasting
https://www.statsmodels.org/dev/generated/statsmodels.tsa.statespace.sarimax.SARIMAX.html
https://github.com/alkaline-ml/pmdarima
https://medium.com/@jetnew/anomaly-detection-of-time-series-data-e0cb6b382e33
https://github.com/rob-med/awesome-TS-anomaly-detection

Data:
https://archive.ics.uci.edu/ml/datasets/ElectricityLoadDiagrams20112014#

https://github.com/xiufengliu/DataGenerator-Cluster-Version

https://www.researchgate.net/profile/Xiufeng_Liu2/publication/280830589_Streamlining_Smart_Meter_Data_Analytics/links/55c867e308aebc967df89f1d.pdf

## Thresholding

Expert knowhow:

- Baseload (especially during weekends)
  - total baseload
  - delta baseload
- Fast Fourier Transform (Frequency Analysis)
  - small variance in amplitude is good for energy producer
- Gradient might be interesting, because
- Negative values
- Zero values

## Stat

https://www.tigera.io/blog/deploy-your-first-deep-learning-model-on-kubernetes-with-python-keras-flask-and-docker/

```
docker build -t eu.gcr.io/akenza-core-staging/anomaly-detection-model:v0.0.1 .
docker push eu.gcr.io/akenza-core-staging/anomaly-detection-model:v0.0.1

docker run -p 5000:5000  eu.gcr.io/akenza-core-staging/anomaly-detection-model:v0.0.1
curl -X POST 'http://localhost:5000/predict'
```
