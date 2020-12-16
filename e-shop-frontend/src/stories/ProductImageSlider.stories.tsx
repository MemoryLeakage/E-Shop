import {ProductImageSlider, ProductImageSliderProps} from "../components/ProductDetails/ProductImageSlider/ProductImageSlider";
import {Meta, Story} from "@storybook/react";
import React from "react";


export default {
    title: "E-Shop/ProductDetails/ProductImageSlider",
    component: ProductImageSlider
} as Meta;

const Template: Story<ProductImageSliderProps> = (args) => <ProductImageSlider {...args}/>
export const Default = Template.bind({})
Default.args = {
    images:['http://0.0.0.0:5050/1.jpg','http://0.0.0.0:5050/2.jpg','http://0.0.0.0:5050/3.jpg'],
}