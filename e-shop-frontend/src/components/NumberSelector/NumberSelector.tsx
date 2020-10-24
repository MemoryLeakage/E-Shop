import React from "react";
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

export class NumberSelector extends React.Component<NumberSelectorProps, NumberSelectorState> {
    readonly minValue: number;
    readonly maxValue: number;

    constructor(props: NumberSelectorProps) {
        super(props);
        this.minValue = this.props.minValue ? this.props.minValue : 1;
        this.maxValue = this.props.maxValue ? this.props.maxValue : 10;
        this.state = {
            value: this.minValue,
        };
        this.decrease = this.decrease.bind(this);
        this.increase = this.increase.bind(this);
    }


    increase() {
        this.setState((state: NumberSelectorState) => ({
            value: this.validateValue(state.value + 1) ? state.value + 1 : state.value,
        }), () => this.props.onValueChange(this.state.value));

    }

    decrease() {
        this.setState((state: NumberSelectorState) => ({
            value: this.validateValue(state.value - 1) ? state.value - 1 : state.value,
        }), () => this.props.onValueChange(this.state.value));
    }

    validateValue(value: number) {
        return (value >= this.minValue && value <= this.maxValue);
    }

    render() {
        return (
            <div className={styles.flexRow}>
                <label className={styles.label}>{this.props.label}:</label>
                <div className={styles.flexRow}>
                    <input className={styles.input} type="text" value={this.state.value}/>
                    <div className={styles.flexColumn}>
                        <ArrowButton style={{marginBottom: "-25%"}} onClick={this.increase}>
                            <StyledExpandLess/>
                        </ArrowButton>
                        <ArrowButton style={{marginTop: "-25%"}} onClick={this.decrease}>
                            <StyledExpandMore/>
                        </ArrowButton>
                    </div>
                </div>
            </div>
        );
    }
}