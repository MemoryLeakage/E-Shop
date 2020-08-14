import Keycloak, {KeycloakAdapter, KeycloakInitOptions, KeycloakInstance, KeycloakLoginOptions} from 'keycloak-js'

let keycloakInstance: KeycloakInstance = initKeycloakInstance();

function initKeycloakInstance(): KeycloakInstance {
    const keycloakConfig: Keycloak.KeycloakConfig = {
        clientId: 'eshop-ui',
        url: 'http://localhost:8080/auth',
        realm: 'eshop',
    }
    const keycloakInstance: Keycloak.KeycloakInstance = Keycloak(keycloakConfig);
    keycloakInstance.onAuthSuccess = function () {
        console.log("SUCCESSFUL AUTHENTICATION")
    }

    keycloakInstance.onReady = function (authenticated: boolean) {
        if (authenticated)
            console.log("Auth Ready")
    }

    const initOptions: KeycloakInitOptions = {
        useNonce: true,
        checkLoginIframe: true,
        checkLoginIframeInterval: 60,
        flow: "standard",
        adapter: "default",
        onLoad: "check-sso",
        redirectUri: window.location.href,
        pkceMethod: "S256"
    }

    keycloakInstance.init(initOptions);
    return keycloakInstance;
}

export default keycloakInstance;
