import React from 'react';
import '../css/detailPage.css'
import Navbar from '../components/navbar'
import BookDetail from '../components/detailPage/bookImg'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import Footer from '../components/homePage/footer'
import {Col, Row} from 'antd';
import {getBook} from '../services/bookService'
import {withRouter} from "react-router-dom";
import BookComment from "../components/detailPage/bookComment";

const datas = {
    imgsrc: "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1908918586,853942668&fm=26&gp=0.jpg",
    price: "$ 19.99",
    name: "The man who Changed China",
    subName: "His biography",
    press: "上海译文出版社",
    author: "Robert Lawrence Kuhn",
    ISBN: "7-5327-3654-7518",
    translator: "谈峥，于海江等",
    year: "2005",
    desc: "《他改变了中国：江泽民传》依时间为顺序，全书分章列节，详尽介绍了中共中央原总书记、原国家主席江泽民的人生历程，尤其是着墨于江泽民担任中国主要领导人期间的政治生涯。《他改变了中国：江泽民传》还对若干重大事件与决策的史实细节、重大外交事件的始末作了首次披露。比如1979年，江泽民在全国人大常委会上作了有关建立经济特区的报告；如1992年党的十四大召开前，江泽民提出用“社会主义市场经济”这一概念来取代“社会主义经济体制”；如2000年，江泽民提出了“三个代表”重要思想。在国际政治方面，如在美国轰炸南联盟、南海撞机等事件中，江泽民的应对及决策。此外，《他改变了中国：江泽民传》也广泛涉及江泽民的个人生活、人品风格等方方面面。"
};

class DetailView extends React.Component {
    constructor(props) {
        super(props);
        console.log(this.props);
        this.state={
            bookinfo: [],
        };
    }


    componentDidMount(){
        const callback1 = (data) => {
            console.log(data);
            this.setState(
                {
                    bookinfo: data
                });
        };

        console.log("arrive detail page");
        let user = localStorage.getItem("user");
        this.setState({user:user});
        console.log(user);

        const query = this.props.location.search;
        const arr = query.split('&');
        // for ?id=, split 4
        const bookId = arr[0].substr(4);
        getBook(bookId, callback1);
    }

    render() {
        return (
            <div>
                <Navbar active="book"/>
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="page-header">
                            <h1>{this.state.bookinfo.name}<small> {this.state.bookinfo.author}</small></h1>
                        </div>
                    </div>
                </div>
                <BookDetail bookData={this.state.bookinfo} />
                <Col span={12} offset={6}>
                    <BookComment comment={this.state.bookinfo} />
                </Col>

                <Footer />
            </div>
        );
    }
}


export default withRouter(DetailView);
