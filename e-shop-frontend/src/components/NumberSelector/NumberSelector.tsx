import React, {useEffect, useState} from "react";
import {IconButton, withStyles} from "@material-ui/core";
import {ExpandLess, ExpandMore} from "@material-ui/icons";
import styles from "./NumberSelector.module.css"

interface NumberSelectorState {
    value: number;
}

export interface NumberSelectorProps {
    maxValue?: number;
    minValue?: number;
    onValueChange: (value: number) => void;
    label: string;
}

const ArrowButton = withStyles({
    root: {
        width: "1em",
        height: "1em",
    }
})(IconButton)

const IconButtonStyles = {
    root: {
        width: "1em",
        height: "1em",
        color: "silver",
        transition: "color 0.5s ease",
        "&:hover": {
            color: "#282c34"
        }
    }
}
const StyledExpandMore = withStyles(IconButtonStyles)(ExpandMore);
const StyledExpandLess = withStyles(IconButtonStyles)(ExpandLess);


export function NumberSelector(props: NumberSelectorProps) {
    const minValue: number = props.minValue ? props.minValue : 1;
    const maxValue: number = props.maxValue ? props.maxValue : 10;
    const [counter, setCounter] = useState<NumberSelectorState>({value: minValue});

    function increase() {
        setCounter((counter: NumberSelectorState) => ({
            value: validateValue(counter.value + 1) ? counter.value + 1 : counter.value,
        }));
    }

    function decrease() {
        setCounter((counter: NumberSelectorState) => ({
            value: validateValue(counter.value - 1) ? counter.value - 1 : counter.value,
        }));
    }

    useEffect(() => {
        props.onValueChange(counter.value)
    }, [counter])


    function validateValue(value: number) {
        return (value >= minValue && value <= maxValue);
    }

    return (
        <div className={styles.flexRow}>
            <label className={styles.label}>{props.label}:</label>
            <div className={styles.flexRow}>
                <input className={styles.input} type="text" value={counter.value}/>
                <div className={styles.flexColumn}>
                    <ArrowButton style={{marginBottom: "-25%"}} onClick={increase}>
                        <StyledExpandLess/>
                    </ArrowButton>
                    <ArrowButton style={{marginTop: "-25%"}} onClick={decrease}>
                        <StyledExpandMore/>
                    </ArrowButton>
                </div>
            </div>
        </div>
    );
}