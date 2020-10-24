import React, {CSSProperties} from "react";
import {NumberSelector} from "../NumberSelector/NumberSelector";
import {Button, IconButton, styled, withStyles} from "@material-ui/core";
import {FavoriteBorder, Visibility} from "@material-ui/icons";
import styles from "./ProductDetailsOverview.module.css";


export interface ProductDetailsOverviewProps {
    title: string;
    price: string;
    oldPrice: string;
    category: string;
    availability: boolean;
    description: string;
}

interface ProductDetailsOverviewState {
    quantity: number;
}

const StyledButton = withStyles({
    root: {
        width: "30%",
        height: "100%",
        maxWidth: "16em",
        minWidth: "10em",
        fontSize: "0.75em",
        color: "white",
        border: "1px solid #71cd14",
        backgroundColor: "#71cd14",
        transition: "color 0.5s ease, background-color 0.5s ease",
        '&:hover': {
            color: "#71cd14",
            backgroundColor: "white",
        }
    }
})(Button)

const StyledIconButton = withStyles({
    root: {
        marginLeft: "15%",
        border: "1px #f6f6f6 solid",
        background: "#f6f6f6",
        transition: "all 0.5s ease",
        borderRadius: "0",
        width: "1.5em",
        height: "1.5em",
        "&:hover": {
            background: "white",
            boxShadow: "-14px 14px 20px 0px rgba(0, 0, 0, 0.1)",
        }
    }
})(IconButton)


export class ProductDetailsOverview extends React.Component<ProductDetailsOverviewProps, ProductDetailsOverviewState> {
    constructor(props: ProductDetailsOverviewProps) {
        super(props);
        this.state = {
            quantity: 1,
        }
        this.onQuantityChange = this.onQuantityChange.bind(this);
    }

    onQuantityChange(quantity: number) {
        this.setState({
            quantity: quantity,
        });
    }

    render() {
        const availabilityColor: CSSProperties = {
            color: this.props.availability ? "#71cd14" : "red",
        };
        const availabilityText = this.props.availability ? "In Stock" : "Out of Stock";
        return (
            <div className={`${styles.mainContainer} ${styles.flexColumn}`}>
                <div className={styles.flexColumn}>
                    <h2 className={styles.title}>{this.props.title}</h2>
                    <div className={styles.flexRow}>
                    <span className={styles.price}>
                        {this.props.price}
                        <sub className={styles.oldPrice}>{this.props.oldPrice}</sub>
                    </span>
                    </div>
                </div>
                <div className={`${styles.middleContainer} ${styles.flexColumn}`}>
                    <div className={`${styles.flexRow} ${styles.categoryContainer}`}>
                        <span className={styles.category}>Category</span>
                        <span>:{this.props.category}</span>
                    </div>
                    <div className={`${styles.flexRow} ${styles.availabilityContainer}`}>
                        <span className={styles.availabilityField}>Availability</span>
                        <span style={availabilityColor}>:{availabilityText}</span>
                    </div>
                </div>
                <p className={styles.description}>{this.props.description}</p>
                <NumberSelector minValue={1} maxValue={25} label="Quantity" onValueChange={this.onQuantityChange}/>
                <div className={`${styles.buttonsContainer} ${styles.flexRow}`}>
                    <StyledButton>ADD TO CART</StyledButton>
                    <div className={styles.flexRow}>
                        <StyledIconButton>
                            <FavoriteBorder style={{width: "200%"}}/>
                        </StyledIconButton>
                        <StyledIconButton>
                            <Visibility style={{width: "200%"}}/>
                        </StyledIconButton>
                    </div>
                </div>
            </div>
        );
    }
}

