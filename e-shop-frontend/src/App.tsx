import React from 'react';
import './App.css';
import keycloakInstance from "./Keyclaok";
import ActionBar, {IconActionButton} from "./components/ActionBar/ActionBar";
import {AddShoppingCart, FavoriteBorder, Visibility} from "@material-ui/icons";

function App() {
    let keycloak = keycloakInstance;
    let buttons: IconActionButton[] = [
        {
            onClick: event => console.log('clicked'),
            icon: AddShoppingCart
        },
        {
            onClick: event => console.log('clicked'),
            icon: FavoriteBorder
        },
        {
            onClick: event => console.log('clicked'),
            icon: Visibility
        }
    ]

    console.log(keycloak.authenticated)
    return (
        <div>
            <ActionBar buttons={buttons}/>

        </div>
    );
}

export default App;
