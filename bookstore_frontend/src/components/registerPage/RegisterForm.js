import React from 'react';
import 'antd/dist/antd.css';
import '../../css/component.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css'
import {Button, Checkbox, Form, Input, Row, Select, Tag} from 'antd';
import {isDup, testDuplicate} from "../../services/userService";
import {CheckCircleOutlined, CloseCircleOutlined,} from '@ant-design/icons';

const {Option} = Select;

const formItemLayout = {
    labelCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 8,
        },
    },
    wrapperCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 16,
        },
    },
};

const tailFormItemLayout = {
    wrapperCol: {
        xs: {
            span: 24,
            offset: 0,
        },
        sm: {
            span: 16,
            offset: 8,
        },
    },
};


class RegistrationForm extends React.Component {
    prefixSelector = (
        <Form.Item name="prefix" noStyle>
            <Select
                style={{
                    width: 70,
                }}
            >
                <Option value="86">+86</Option>
                <Option value="87">+87</Option>
            </Select>
        </Form.Item>
    );

    constructor(props) {
        super(props);
        this.state = ({
            duplicateInfo: "Name OK",
        });
    }

    renderFail = () => {
        return (<Tag icon={<CloseCircleOutlined/>}
                     color="error"
                     className="dupTag">
            {this.state.duplicateInfo}
        </Tag>);
    }

    renderSuccess = () => {
        return (<Tag icon={<CheckCircleOutlined/>}
                     color="success"
                     className="dupTag">
            {this.state.duplicateInfo}
        </Tag>);
    }

    onFinish = (values) => {
        console.log('Received values of form: ', values);
        isDup(values);
    };

    callback = (value) => {
        console.log(value)
        this.setState({
            duplicateInfo: value ? "Name Dup" :
                "Name OK",
        })
    }

    isDuplicateName = (value) => {
        testDuplicate(value, this.callback);
    }

    render() {
        return (
            <div>
                <Form
                    {...formItemLayout}
                    name="register"
                    onFinish={this.onFinish}
                    onValuesChange={this.isDuplicateName}
                    initialValues={{
                        prefix: '86',
                    }}
                    scrollToFirstError
                >
                    <Row justify="center">
                        <Form.Item
                            name="email"
                            label="E-mail"
                            rules={[
                                {
                                    type: 'email',
                                    message: 'The input is not valid E-mail!',
                                },
                                {
                                    required: true,
                                    message: 'Please input your E-mail!',
                                },
                            ]}
                        >
                            <Input style={{width: 200}}/>
                        </Form.Item>
                        {/*<Popover content={<div><p>{this.state.duplicateInfo}</p></div>}*/}
                        {/*         title="Duplicate Test" trigger="click">*/}
                        {/*    <Button>Duplicate?</Button>*/}
                        {/*</Popover>*/}
                        {
                            this.state.duplicateInfo === "Name OK" ? this.renderSuccess() : this.renderFail()
                        }
                    </Row>


                    <Form.Item
                        name="password"
                        label="Password"
                        rules={[
                            {
                                required: true,
                                message: 'Please input your password!',
                            },
                            ({getFieldValue}) => ({
                                validator(_, value) {
                                    if (!value || getFieldValue('password') === value) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('The two passwords that you entered do not match!'));
                                },
                            }),
                        ]}
                        hasFeedback
                    >
                        <Input.Password/>
                    </Form.Item>

                    <Form.Item
                        name="confirm"
                        label="Confirm Password"
                        dependencies={['password']}
                        hasFeedback
                        rules={[
                            {
                                required: true,
                                message: 'Please confirm your password!',
                            },
                            ({getFieldValue}) => ({
                                validator(_, value) {
                                    if (!value || getFieldValue('password') === value) {
                                        return Promise.resolve();
                                    }

                                    return Promise.reject(new Error('The two passwords that you entered do not match!'));
                                },
                            }),
                        ]}
                    >
                        <Input.Password/>
                    </Form.Item>

                    <Form.Item
                        name="nickname"
                        label="Nickname"
                        tooltip="What do you want others to call you?"
                        rules={[
                            {
                                required: true,
                                message: 'Please input your nickname!',
                                whitespace: true,
                            },
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="address"
                        label="address"
                        tooltip="We will deliver the books to this address"
                        rules={[
                            {
                                required: true,
                                message: 'Please input your address!',
                                whitespace: true,
                            },
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="phone"
                        label="Phone Number"
                        rules={[
                            {
                                required: true,
                                message: 'Please input your phone number!',
                            },
                        ]}
                    >
                        <Input
                            addonBefore={this.prefixSelector}
                            style={{
                                width: '100%',
                            }}
                        />
                    </Form.Item>


                    <Form.Item
                        name="agreement"
                        valuePropName="checked"
                        rules={[
                            {
                                validator: (_, value) =>
                                    value ? Promise.resolve() : Promise.reject(new Error('Should accept agreement')),
                            },
                        ]}
                        {...tailFormItemLayout}
                    >
                        <Checkbox>
                            I have read the <a href="/copyright">agreement</a>
                        </Checkbox>
                    </Form.Item>

                    <Form.Item {...tailFormItemLayout}>
                        <Button type="primary" htmlType="submit">
                            Register
                        </Button>
                    </Form.Item>

                </Form>
            </div>
        )
    }
}

export default RegistrationForm;
