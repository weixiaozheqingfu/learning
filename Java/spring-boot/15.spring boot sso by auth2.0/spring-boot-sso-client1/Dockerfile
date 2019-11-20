FROM weixiaozheqingfu/docker_image_jdk8:20190930

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN mkdir -p /jar

# ADD target/libs /jar/libs
ADD target/spring-boot-sso-client1-1.0-SNAPSHOT.jar /jar/sso-client1.jar
ADD startWithContainer.sh /jar/startWithContainer.sh

RUN chmod 700 /jar/startWithContainer.sh

ENTRYPOINT ["sh","/jar/startWithContainer.sh"]



