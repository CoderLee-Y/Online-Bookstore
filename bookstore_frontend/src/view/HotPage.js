import React from 'react';
import '../css/hotPage.css'
import Navbar from '../components/navbar'
import Footer from '../components/homePage/footer'
import CarouselHP from '../components/homePage/carousel'
import StarBooks from '../components/hotPage/bestSellers'

class HotView extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="top" />
                <div class="row">
                    <div class="col-md-10 col-md-offset-1 ">
                        <CarouselHP
                            src1="http://img60.ddimg.cn/ddimg/2401/douban1200560-1608277950.jpg"
                            src2="http://img59.ddimg.cn/9002840116561559.jpg"
                            src3="http://img60.ddimg.cn/topic_img/gys_04514/dakeke7503159.jpg"
                            src4="http://img58.ddimg.cn/9007760189748728.jpg"
                        />
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <StarBooks />
                    </div>
                </div>
                <Footer />
            </div>
        );
    }
}


export default HotView;