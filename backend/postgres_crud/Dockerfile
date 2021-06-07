FROM ubuntu:18.04
RUN mkdir /code
WORKDIR /code

RUN apt-get update --fix-missing

RUN apt-get install --fix-missing -y \
        python3 \
        python3-pip \
        python3-setuptools \
        libpq-dev \
    && rm -rf /var/lib/apt/lists/*  && \
    apt-get purge --auto-remove && \
    apt-get clean

COPY requirements.txt /code
RUN pip3 install -r requirements.txt

COPY . /code

RUN chmod -R +x /code/

ENTRYPOINT ["python3", "./api/app.py"]
