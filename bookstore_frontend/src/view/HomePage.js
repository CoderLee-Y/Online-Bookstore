import React from 'react';
import '../css/homePage.css'
import Navbar from '../components/navbar'
import CarouselHP from '../components/homePage/carousel'
import Footer from '../components/homePage/footer'
import BookCol from '../components/bookCol'
import { Row, Col, Divider } from 'antd';
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import StarBooks from "../components/hotPage/bestSellers";

const bookData = [
    {
        image: "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1908918586,853942668&fm=26&gp=0.jpg",
        price: "$ 19.99",
        name: "he changed china",
        description: "一本他的历史."
    },
    {
        image: "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3843066828,1780502566&fm=26&gp=0.jpg",
        price: "$ 29.99",
        name: "Computer system: a programmer's perspective",
        description: "计算机系统的入门书."
    },
    {
        image: "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3259968346,475279375&fm=26&gp=0.jpg",
        price: "$ 9.99",
        name: "Machine learning",
        description: "炼丹师的起点."
    }
];
class HomeView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            list: [bookData]
        };
        this.handleClick = this.handleClick.bind(this);
    }


    handleClick() {
        const a = this.state.list;
        a.push(bookData);
        this.setState(() => ({
            list: a
        }));
    }

    render() {
        console.log(this.state.list);
        return (
            <div>
                <Navbar active="home" />
                <div class="container" id="bigPage">
                    <div class="jumbotron">
                        <h2>Hi, 了解阿加莎吗？</h2>
                        <p>无可争议的悬疑女王。</p>
                        <p><a class="btn btn-primary btn-lg" href="/Detail?id=30" role="button">
                            Learn more</a></p>
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-10 col-md-offset-1 ">
                        <CarouselHP
                            src1="http://img60.ddimg.cn/ddimg/2401/douban1200560-1608277950.jpg"
                            src2="http://img59.ddimg.cn/9002840116561559.jpg"
                            src3="http://img60.ddimg.cn/topic_img/gys_04514/dakeke7503159.jpg"
                            src4="http://img58.ddimg.cn/9007760189748728.jpg"
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-10 col-md-offset-1">
                        <StarBooks/>
                    </div>
                </div>
                <Footer />
            </div>
        );
    }
}


export default (HomeView);