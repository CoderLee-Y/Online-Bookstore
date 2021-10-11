import React from 'react';
import '../css/bookPage.css'
import '../css/component.css'
import 'antd/dist/antd.css';
import Navbar from '../components/navbar'
import Footer from '../components/homePage/footer'
import { Input, Row, Col, Pagination, Layout, Menu } from 'antd';
import {getBooks, searchBooks} from "../services/bookService";
import BookSingleView from "../components/singleBookView";
const { Search } = Input;

const ClassifyData = [
    {
        class: "All Kinds",
        url: "/Book"
    },
]

const { Sider } = Layout;

class BookView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: {content: []},
            class: [],
            collapsed: false,
        };
    }

    componentDidMount() {
        getBooks(0 ,0, this.callback1);
    }

    callback1 = (data) => {
        console.log(data)
        data.forEach(element => {
            element['key'] = element.bookId;
        })
        this.setState({
            data: Object.assign(data,
                {content: data.filter((item) => item.inventory !== -1)}),
        })
    }


    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    }

    handlePage = (value) => {
        console.log(value);
        getBooks(value - 1, 0, this.callback1);
    }

    onSearch = (value) => {
        searchBooks(0, value, this.callback1);
    }

    render() {
        return (
            <div>
                <Navbar active="book" />
                <Row >
                    <Col span={20} offset={2}>
                        <Layout>
                            <Sider id="sidebar" class="site-layout-background" trigger={null}>
                                <div className="logo" />
                                <Menu class="sideMenu" mode="inline"
                                      defaultSelectedKeys={['1']}>
                                    {
                                        ClassifyData.map(
                                            (sideClass, index) =>
                                                <Menu.Item id="menuItem" key={index + 1}>
                                                    <a href={sideClass.url}>
                                                        {sideClass.class}
                                                    </a>
                                                </Menu.Item>
                                        )
                                    }
                                </Menu>
                            </Sider>
                            <Layout className="site-layout">
                                <div>
                                    <Row align="middle" >
                                        <Col span={12} offset={6}>
                                            <Search
                                                placeholder="Search Books By Name"
                                                allowClear
                                                enterButton="Search"
                                                size="large"
                                                style={{
                                                    margin: 10,
                                                    height: 30,
                                                }}
                                                onSearch={this.onSearch}
                                            />
                                        </Col>
                                    </Row>

                                    <div className="container-fluid">
                                        <div className="row">
                                            {
                                                this.state.data.content.map(
                                                    function (BookData, index) {
                                                        return(<div className="col-xs-6 col-md-4" key={index}>
                                                            <div span={3}>
                                                                <BookSingleView
                                                                    data={BookData}
                                                                />
                                                            </div>
                                                        </div>)
                                                    }
                                                )
                                            }
                                        </div>
                                        <Pagination showQuickJumper
                                                    defaultCurrent={0}
                                                    style={{margin: 15}}
                                                    current={this.state.data.number + 1}
                                                    total={this.state.data.totalElements}
                                                    defaultPageSize={12}
                                                    onChange={this.handlePage} />
                                    </div>
                                </div>
                            </Layout>
                        </Layout >

                    </Col>
                </Row>
                <Footer />
            </div>
        )
    }
}


export default BookView;
