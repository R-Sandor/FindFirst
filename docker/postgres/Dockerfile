FROM postgres:16.2-alpine3.19

COPY ./scripts/* /docker-entrypoint-initdb.d
RUN chmod a+x /docker-entrypoint-initdb.d/*

EXPOSE 5432
