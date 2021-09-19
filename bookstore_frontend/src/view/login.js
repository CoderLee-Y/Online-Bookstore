import React from 'react';
import '../css/login.css'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import {login} from '../services/userService'
import {Form, Input, Button, Checkbox, Row} from 'antd';
import Navbar from '../components/navbar'
import Footer from '../components/homePage/footer'

const layout = {
    labelCol: {
        span: 8,
    },
    wrapperCol: {
        span: 16,
    },
};
const tailLayout = {
    wrapperCol: {
        offset: 8,
        span: 16,
    },
};

class loginView extends React.Component {
    onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };

    handleSubmit = (values) => {
        console.log('Received values of form: ', values);
        login(values);
    };

    render() {
        return (
            <div>
                <Navbar active="login"/>
                <div id="loginPage">
                    <div class="row">
                        <Form   {...layout}
                                name="basic"
                                initialValues={{remember: true}}
                                onFinish={this.handleSubmit}
                        >

                            <div className="myInput" class="col-md-3 col-md-offset-4" align="center">
                                <Form.Item
                                    label="username"
                                    name="username"
                                    rules={[{required: true, message: 'Please Input Your ID.'}]}
                                >
                                    <Input bordered={false}/>
                                </Form.Item>
                            </div>

                            <div className="myInput" class="col-md-3 col-md-offset-4" align="center">
                                <Form.Item
                                    label="password"
                                    name="password"
                                    rules={[{required: true, message: 'Please Input Your Username.'}]}
                                >
                                    <Input.Password placeholder="input password"
                                                    bordered={false}/>
                                </Form.Item>
                            </div>

                            <div className="myRemember" class="col-md-3 col-md-offset-4">
                                <Form.Item
                                    {...tailLayout}
                                    name="remember"
                                    valuePropName="checked"
                                >
                                    <Checkbox>
                                        Remember ID
                                    </Checkbox>
                                </Form.Item>
                            </div>
                            <div class="col-md-3 col-md-offset-4">
                                <Form.Item {...tailLayout} className="mySubmit">
                                    <Button type="primary" htmlType="submit">
                                        登录
                                    </Button>
                                </Form.Item>
                            </div>
                        </Form>
                    </div>

                </div>
                <Footer/>
            </div>
        );
    }
}

export default loginView;
