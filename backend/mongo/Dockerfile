FROM mongo
RUN mkdir /code
WORKDIR /code

ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=pdm123

COPY ./script.js .
COPY ./mongo-init.js /docker-entrypoint-initdb.d/
COPY ./run.sh /docker-entrypoint-initdb.d/

EXPOSE 27017
