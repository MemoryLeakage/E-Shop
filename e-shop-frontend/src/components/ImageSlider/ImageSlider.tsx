import React from "react";
import Slider from "@ant-design/react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import "./ImageSlider.css";
import {Image} from "antd";
import "antd/dist/antd.css";

export class ImageSlider extends React.Component<any, any> {

    render() {
        const settings = {
            customPaging: function (i: number) {
                return (
                    <a>
                        <img src={`${i + 1}.jpg`}/>
                    </a>
                );
            },
            dots: true,
            infinite: true,
            dotsClass: "slick-dots slick-thumb",
            speed: 500,
            slidesToShow: 1,
            slidesToScroll: 1,
        };

        return (<div style={{width: "300px", height: "500px"}}>
            <Slider  {...settings}>
                <div>
                    <div style={{width: "150px", margin: "auto"}}>
                        <Image height={150} src={`1.jpg`}/>
                    </div>
                </div>
                <div>
                    <div style={{width: "150px", margin: "auto"}}>
                        <Image height={150} src={`2.jpg`}/>
                    </div>
                </div>
                <div>
                    <div style={{width: "150px", margin: "auto"}}>
                        <Image height={150} src={`3.jpg`}/>
                    </div>
                </div>
            </Slider>
        </div>);
    }
}