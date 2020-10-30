import {Meta, Story} from "@storybook/react/types-6-0";
import React from "react";
import {ImageSlider} from "../components/ImageSlider/ImageSlider";


export default {
    title: 'E-Shop/HomePage/ImageSlider',
    component: ImageSlider,
} as Meta;

const Template: Story<{}> = (args) => <ImageSlider  {...args} />;
export const Default = Template.bind({});
