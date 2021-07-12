import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import { Result, Button } from 'antd';
import ReactDOM from 'react-dom';
import Navbar from "../navbar";
import Footer from "../homePage/footer";
import '../../css/component.css'

class RegisterResult extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="login" />
                <div id="successPage">
                    <Result
                        status="success"
                        title="Success"

                        subTitle="Welcome to join us."
                        extra={[
                            <Button type="primary" key="login" href="/login">
                                Login now!
                            </Button>,
                            <Button key="home" href="/Home">
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

export default RegisterResult;
