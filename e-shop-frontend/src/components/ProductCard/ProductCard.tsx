import React, {Component, CSSProperties} from "react";
import ActionBar, {IconActionButton} from "../ActionBar/ActionBar";
import {AddShoppingCart, FavoriteBorder, Visibility} from "@material-ui/icons";
import styles from "./ProductCard.module.css";

export type ProductCardProps = {
    backgroundImage: string,
    price: string,
    oldPrice: string,
    title: string,
    onClick: () => void
}

type ProductCardState = {
    mouseOver: boolean
}

export default class ProductCard extends Component<ProductCardProps, ProductCardState> {

    constructor(props: ProductCardProps) {
        super(props);
        this.state = {
            mouseOver: false
        }
    }


    render() {

        const actionBar: CSSProperties = {
            transform: this.state.mouseOver ? "translate(0%,-100%)" : "translate(0%,0%)",
        };

        const textContainer: CSSProperties = {
            color: this.state.mouseOver ? "rgb(158, 216, 70)" : "black",
        }

        return (
            <div className={styles.card}
                 onMouseLeave={() => this.setState({mouseOver: false})}
                 onMouseEnter={() => this.setState({mouseOver: true})}>
                <div className={styles.pictureContainer}>
                    <img src={this.props.backgroundImage} alt={"loading"}/>
                    <div className={styles.actionBar} style={actionBar}>
                        <ActionBar buttons={buttons}/>
                    </div>
                </div>
                <div className={styles.textContainer} style={textContainer}>
                    <span className={styles.title} onClick={()=>this.props.onClick()}>
                    {this.props.title}
                    </span>
                    <div className={styles.pricesContainer}>
                        <p className={styles.price}>{this.props.price}</p>
                        <p className={styles.oldPrice}>{this.props.oldPrice}</p>
                    </div>
                </div>
            </div>
        )
    }
}

let buttons: IconActionButton[] = [
    {
        onClick: () => console.log('clicked'),
        icon: AddShoppingCart
    },
    {
        onClick: () => console.log('clicked'),
        icon: FavoriteBorder
    },
    {
        onClick: () => console.log('clicked'),
        icon: Visibility
    }
]