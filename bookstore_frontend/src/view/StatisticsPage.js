import React from 'react';
import 'antd/dist/antd.css';
import { Row, Col, Divider, Space } from 'antd';
import Navbar from "../components/navbar";
import '../css/component.css'
import OrderData from "../components/consolePage/statisticsTable";
import Footer from "../components/homePage/footer";
import UserTable from "../components/consolePage/userTable";
import CustomerData from "../components/consolePage/userManageTable";

class statistics extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="Statistics" />
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <Divider>Best sellers</Divider>
                        <OrderData />
                        <Divider>Big Fans</Divider>
                        <CustomerData />
                    </div>
                </div>
                <Footer />
            </div>
        )
    }
}

export default statistics;
