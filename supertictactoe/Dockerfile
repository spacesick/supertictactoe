FROM eclipse-temurin:17-jdk-alpine
# ENV DB_USERNAME=${DB_USERNAME}
# ENV DB_PASSWORD=${DB_PASSWORD}
COPY build/libs/supertictactoe-1.0.0.jar .
CMD java ${JAVA_OPTS} -jar supertictactoe-1.0.0.jar
EXPOSE 8080

# FROM eclipse-temurin:17-jdk-alpine AS build
# WORKDIR /workspace/app

# COPY . /workspace/app
# RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
# RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/supertictactoe-1.0.0.jar)

# FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp
# ARG DEPENDENCY=/workspace/app/build/dependency
# COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /
# EXPOSE 8080
# ENTRYPOINT ["java","-cp","app:app/lib/*","supertictactoe.Application"]