import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import { Result, Button} from 'antd';
import ReactDOM from 'react-dom';
import Navbar from "../navbar";
import Footer from "../homePage/footer";
import '../../css/component.css'

class PermissionDenied extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="login" />
                <div id="successPage">
                    <Result
                        title="Permission Denied"
                        subTitle="You don't have Administrator's Auth"
                        extra={
                            <Button type="primary" key="console">
                                <a href="/">
                                    Go Home
                                </a>
                            </Button>
                        }
                    />
                </div>
                <Footer />
            </div>
        );
    }
}

export default PermissionDenied;
