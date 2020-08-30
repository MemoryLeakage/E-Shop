FROM jboss/keycloak:11.0.0
COPY ./eshop-realm.json /tmp/eshop-realm.json
COPY ./users-event-listener/users-event-listener-ear/target/*.ear /opt/jboss/keycloak/standalone/deployments/
