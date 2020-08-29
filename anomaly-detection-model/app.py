# USAGE
# Start the server:
# 	python app.py
# Submit a request via cURL:
# 	curl -X POST -F image=@dog.jpg 'http://localhost:5000/predict'

# import the necessary packages
import numpy as np
import flask
import io
import pandas as pd
import pickle
import collections as cl


# initialize our Flask application and the Keras model
app = flask.Flask(__name__)
model = None

def load_model():
    # load the pre-trained model
    global model
    pretrained_freqs = open("pretrained_freqs.pkl", "rb")
    global appr
    #appr = pickle.load(pretrained_freqs)
    appr = cl.defaultdict(float)

@app.route("/predict", methods=["POST"])
def predict():
    # initialize the data dictionary that will be returned from the
    # view
    count = 0
    alert_threshold = 0.005
    data = {"success": False}
    if flask.request.method == "POST":
        content = flask.request.json

        data = {
                    "prob": 1.0,  
                    "label": "non-anomalous",
                    "severity": "NONE",
                    "success": True
                }

        val = content['kWh']
        if pd.isna(val):
            print('Missing value')
        else:
            count += 1
            if val in appr:
                appr[val] += 1
            else:
                appr[val] = 1
            
            freq = appr[val]/count
            if freq< alert_threshold:
                print('Anomolous value detected')
                data = {
                    "prob": 0.9,  
                    "label": "anomaly",
                    "severity": "HIGH",
                    "success": True
                }
            #reset model
            if count == 10000:
                appr = cl.defaultdict(float)
                count = 0

    # return the data dictionary as a JSON response
    return flask.jsonify(data)

# if this is the main thread of execution first load the model and
# then start the server
if __name__ == "__main__":
    print(("* Loading model and Flask starting server..."
        "please wait until server has fully started"))
    load_model()
    app.run(host='0.0.0.0')
