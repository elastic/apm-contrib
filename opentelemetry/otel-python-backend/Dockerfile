FROM python:3

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
ADD . /app
CMD ["python", "server.py"]

