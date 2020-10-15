import {Meta, Story} from "@storybook/react/types-6-0";
import ActionBar, {IconActionButton, ActionBarProps} from "../components/ActionBar/ActionBar";
import React from "react";
import {AddShoppingCart, FavoriteBorder, Visibility} from "@material-ui/icons";

export default {
    title: 'E-Shop/HomePage/ActionBar',
    component: ActionBar,
} as Meta;


const Template: Story<ActionBarProps> = (args) => <div style={{width: "300px"}}><ActionBar {...args} /></div>;

export const DefaultActionBar = Template.bind({});

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

DefaultActionBar.args = {
    buttons:buttons
};
