import React from "react";
import Slider from "@ant-design/react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import {Image} from "antd";
import "antd/dist/antd.css";
import './ProductImageSlider.css'


export interface ProductImageSliderProps {
    images: string[];
}

export class ProductImageSlider extends React.Component<ProductImageSliderProps, any> {

    render() {
        const {images} = this.props;
        const settings = {
            customPaging: function (i: number) {
                return (
                    <a>
                        <img src={images[i]}/>
                    </a>
                );
            },
            dots: true,
            infinite: true,
            dotsClass: "slick-dots slick-thumb",
            speed: 500,
            slidesToShow: 1,
            slidesToScroll: 1,
            autoPlay: true,
            autoplaySpeed: 100
        };
        const Images = images.map((image, index) =>
            <div key={index}>
                <Image src={image}/>
            </div>
        );
        return (<div style={{width: "100%", maxWidth: "700px", minWidth: "300px"}}>
            <Slider  {...settings}>
                {Images}
            </Slider>
        </div>);
    }
}