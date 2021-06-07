FROM postgres

ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=pdm@123

COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 27017
