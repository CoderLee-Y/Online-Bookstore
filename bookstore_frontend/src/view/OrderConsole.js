import React from 'react';
import 'antd/dist/antd.css';
import { Row, Col, Divider, Space } from 'antd';
import Navbar from "../components/navbar";
import '../css/component.css'
import {getAllOrders} from "../services/orderService";
import Footer from "../components/homePage/footer";
import ConsoleOrderTable from "../components/consolePage/orderTable";
class orderConsole extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="OrderConsole" />
                <div class="row">
                    <Divider>All orders in iBookStore</Divider>
                    <div class="col-md-8 col-md-offset-2">
                        <ConsoleOrderTable/>
                    </div>
                </div>
                <Footer />
            </div>
        )
    }
}

export default orderConsole;
