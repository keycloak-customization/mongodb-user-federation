FROM jboss/keycloak:latest
ADD mongodb /opt/jboss/keycloak/modules/system/layers/keycloak/org/mongodb
#RUN mv **/app.jar **/mongodb_user_storage_provider.jar
ADD app/build/libs/app.jar /opt/jboss/keycloak/standalone/deployments/mongodb_user_storage_provider.jar

RUN /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin
ENTRYPOINT ["/usr/bin/env"]
CMD ["sh","/opt/jboss/tools/docker-entrypoint.sh"]