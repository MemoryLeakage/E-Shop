import React from "react";
import {NumberSelector, NumberSelectorProps} from "../components/NumberSelector/NumberSelector";
import {Story} from "@storybook/react";

export default {
    title: "E-Shop/ProductDetails/NumberSelector",
    component: NumberSelector,
}

const Template: Story<NumberSelectorProps> = (args) => <NumberSelector {...args}/>
export const Default = Template.bind({});
Default.args = {
    minValue:1,
    maxValue:10,
    label: "Quantity",
    onValueChange: console.log
}