FROM python:3.6
WORKDIR /app

COPY requirements.txt /app
COPY pretrained_freqs.pkl /app/pretrained_freqs.pkl
RUN pip install -r ./requirements.txt

COPY app.py /app
CMD ["python", "app.py"]