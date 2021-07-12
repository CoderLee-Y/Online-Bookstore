import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import { Result, Button } from 'antd';
import ReactDOM from 'react-dom';
import Navbar from "../navbar";
import Footer from "../homePage/footer";
import '../../css/component.css'

class PurchaseResult extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="order" />
                <div id="successPage">
                    <Result
                        status="success"
                        title="Success"

                        subTitle="Your books will be sent to your address in 48 Hours."
                        extra={[
                            <Button type="primary" key="console" href="/Order">
                                Check Order
                            </Button>,
                            <Button key="buy" href="/Home">
                                Go to Home
                            </Button>,
                        ]}
                    />
                </div>

                <Footer />
            </div>
        );
    }
}



export default PurchaseResult;
