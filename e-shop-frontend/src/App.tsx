import React from 'react';
import './App.css';
import keycloakInstance from "./Keyclaok";

function App() {
    let keycloak = keycloakInstance;
    console.log(keycloak.authenticated)
    return (
        <div>
            Hello world!
        </div>
    );
}

export default App;