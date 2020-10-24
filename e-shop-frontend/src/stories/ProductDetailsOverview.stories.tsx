import {ProductDetailsOverview, ProductDetailsOverviewProps} from "../components/ProductDetails/ProductDetailsOverview";
import {Meta, Story} from "@storybook/react";
import React from "react";

export default {
    title: "E-Shop/ProductDetails/ProductDetailsOverview",
    component: ProductDetailsOverview,
} as Meta;

const Template: Story<ProductDetailsOverviewProps> = (args) => <ProductDetailsOverview {...args}/>;
export const Default = Template.bind({});
Default.args = {
    title: "Faded SkyBlu Denim Jeans",
    price: "$149.99",
    oldPrice: "$189.99",
    category: "Household",
    availability: true,
    description: "Mill Oil is an innovative oil filled radiator with the most modern technology. If you are looking for something that can make your interior look awesome, and at the same time give you the pleasant warm feeling during the winter",
}