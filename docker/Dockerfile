FROM eclipse-temurin:17

COPY build/contube/libs /contube/builtin-lib
COPY libs /contube/builtin-lib
COPY bin /contube/bin
COPY conf /contube/conf
COPY docker/entrypoint.sh /contube/entrypoint.sh

VOLUME [ "/contube/libs" ]
VOLUME [ "/contube/tubes" ]

WORKDIR /contube/

ENTRYPOINT [ "/contube/entrypoint.sh" ]
