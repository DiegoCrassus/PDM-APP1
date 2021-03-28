FROM ubuntu:18.04
RUN mkdir /code
WORKDIR /code

ADD . /code

RUN apt-get update --fix-missing && \
    apt-get install -y python3 python3-dev && \
    apt-get install -y python3-setuptools python3-pip && \
    apt-get install -y libsm6 libxext6 libxrender-dev && \
    apt-get install -y build-essential cmake pkg-config && \
    apt-get install -y libx11-dev libatlas-base-dev && \
    apt-get install -y libgtk-3-dev libboost-python-dev cmake

ADD ./requirements.txt ./

RUN pip3 install -r requirements.txt

RUN chmod +x /code/run.sh

CMD ["./code/run.sh"]
