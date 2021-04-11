FROM ubuntu:18.04
RUN mkdir /code
WORKDIR /code

RUN apt-get update

RUN apt-get update --fix-missing -y

RUN apt-get install --fix-missing -y \
    python3 \
    python3-pip \
    python3-setuptools \
    && rm -rf /var/lib/apt/lists/*  && \
    apt-get purge --auto-remove && \
    apt-get clean

COPY requirements.txt /code

RUN pip3 install --no-cache-dir -r requirements.txt

COPY . /code

RUN chmod -R +x /code/

EXPOSE 9000

ENTRYPOINT ["python3", "api/app.py"]
