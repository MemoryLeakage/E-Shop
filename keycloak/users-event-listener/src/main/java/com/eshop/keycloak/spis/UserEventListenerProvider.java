package com.eshop.keycloak.spis;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserProvider;

import java.util.Collections;
import java.util.Set;


public class UserEventListenerProvider implements EventListenerProvider {


    private static final Logger logger = Logger.getLogger(UserEventListenerProvider.class);

    private static final Set<OperationType> adminOperationTypes =
            Set.of(OperationType.CREATE,
                    OperationType.DELETE,
                    OperationType.UPDATE);
    private static final Set<ResourceType> adminResourceTypes =
            Collections.singleton(ResourceType.USER);

    private static final Set<EventType> eventTypes =
            Set.of(EventType.REGISTER,
                    EventType.UPDATE_EMAIL,
                    EventType.UPDATE_PROFILE);
    private final UserProvider users;
    private final RealmModel realm;
    private KeycloakSession keycloakSession;

    public UserEventListenerProvider(KeycloakSession keycloakSession) {

        this.keycloakSession = keycloakSession;
        this.users = keycloakSession.userStorageManager();
        this.realm = keycloakSession.getContext().getRealm();
    }

    @Override
    public void onEvent(Event event) {
        if (eventTypes.contains(event.getType())) {
            try {
                String usersFirstName = users.getUserById(event.getUserId(),
                        realm)
                        .getFirstName();
                logger.error("USERS METHOD: " + usersFirstName);
            } catch (Exception e) {
                logger.error("USERS METHOD: FAIL");
            }
            try {
                logger.error("AUTHENTICATED USER:" +
                        keycloakSession.getContext().getAuthenticationSession().getAuthenticatedUser().getFirstName());
            }catch (Exception e){
                logger.error("AUTHENTICATED USER: FAIL");
            }
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {

        if (adminOperationTypes.contains(event.getOperationType())
                && adminResourceTypes.contains(event.getResourceType())) {
            logger.error("ADMIN EVENT: " + event.getRepresentation());
        }
    }

    @Override
    public void close() {

    }
}
