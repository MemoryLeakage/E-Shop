import React from "react";
import {IconButton} from "@material-ui/core";
import {SvgIconComponent} from "@material-ui/icons";
import {makeStyles} from "@material-ui/core/styles";

export type IconActionButton = {
    onClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void,
    icon: SvgIconComponent
}

export type ActionBarProps = {
    buttons: IconActionButton[]
}


const useStyles = makeStyles({
    'actionBar': {
        backgroundColor: "rgb(189, 228, 153)",
        display: "flex",
        justifyContent: "center",
        paddingRight: "10%",
        paddingLeft: "10%",
        paddingTop: "2.5%",
        paddingBottom: "2.5%",
    },
    "iconButton": {
        height: "100%",
        marginRight: "5%",
        marginLeft: "5%",
        backgroundColor: "white",
        "&:hover": {
            backgroundColor: "rgb(113, 205, 20)",
        }
    }
});

export default function ActionBar(props: ActionBarProps) {
    const classes = useStyles();
    const buttons = props.buttons.map((iconActionButton, index) =>
        <IconButton className={classes.iconButton} key={index} onClick={event => iconActionButton.onClick(event)}>
            <iconActionButton.icon/>
        </IconButton>);

    return (
        <div className={classes.actionBar}>
            {buttons}
        </div>
    )

}