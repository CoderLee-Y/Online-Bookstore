import React from 'react';
import 'antd/dist/antd.css';
import { Row, Col, Divider, Space } from 'antd';
import Navbar from "../components/navbar";
import '../css/component.css'
import Footer from "../components/homePage/footer";
import UserTable from "../components/consolePage/userTable";

class statistics extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="Statistics" />
                <div class="row" className="commonTallPage">
                    <div class="col-md-8 col-md-offset-2">
                        <h1>@Copyright Li Yiyan, 2021</h1>
                        <h1>All rights reserved</h1>
                    </div>
                </div>
                <Footer />
            </div>
        )
    }
}

export default statistics;
