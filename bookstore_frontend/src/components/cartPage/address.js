import React from 'react';
import 'antd/dist/antd.css';
import { Card, Button, Drawer, Form, Col, Row, Input, Select, Modal, Radio } from 'antd';
import {changeReceipt, getReceipt} from "../../services/userService";
const { Option } = Select;

const prefixSelector = (
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

const CollectionCreateForm = ({ visible, onCreate, onCancel }) => {
    const [form] = Form.useForm();
    return (<Modal
        visible={visible}
        title="Change receipt information"
        okText="Confirm"
        cancelText="Cancel"
        onCancel={onCancel}
        onOk={() => {
            form
                .validateFields()
                .then((values) => {
                    form.resetFields();
                    onCreate(values);
                })
                .catch((info) => {
                    console.log('Validate Failed:', info);
                });
        }}
    >
        <Form
            form={form}
            layout="vertical"
            name="form_in_modal"
            initialValues={{
                modifier: 'public',
            }}
        >
            <Form.Item
                name="address"
                label="address"
                rules={[
                    {
                        required: true,
                        message: 'Please input the title of collection!',
                    },
                ]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                name="phone"
                tooltip="For express information, we'll protect your privacy."
                label="Phone Number"
                rules={[
                    {
                        required: true,
                        message: 'Please input your phone number!',
                    },
                ]}
            >
                <Input
                    addonBefore={prefixSelector}
                    style={{
                        width: '100%',
                    }}
                />
            </Form.Item>

            <Form.Item
                name="nickname"
                label="Nickname"
                tooltip="For express information, we'll protect your privacy."
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
        </Form>
    </Modal>)
}

class Address extends React.Component {
    constructor(props) {
        super(props);
        this.state = ({
            visible: false,
        })
    }

    getAddr = (value) => {
        this.setState({
            tel: value['tel'],
            address: value['address'],
            name: value['name'],
        });
    }

    componentDidMount() {
        getReceipt(this.getAddr);
    }

    showDrawer = () => {
        this.setState({
            visible: true,
        });
    };

    onClose = () => {
        this.setState({
            visible: false,
        });
    };

    onFinish = (value) => {
        console.log(value);
        this.setState({
            visible: false,
        })
        changeReceipt(value);
        this.setState({
            tel: value['phone'],
            address: value['address'],
            name: value['nickname'],
        })
    }

    render() {
        return (
            <div>
                <Card title="Address"
                    extra={<Button type="default" onClick={this.showDrawer}>edit</Button>}
                    style={{ width: 1100 }}>
                    <strong>Your address: </strong>
                    <strong>{this.state.address}</strong><br></br>
                    <strong>Your Recipient Name: </strong>
                    <strong>{this.state.name}</strong><br></br>
                    <strong>Your Tel: </strong>
                    <strong>{this.state.tel}</strong>
                </Card>
                <div>
                    <CollectionCreateForm
                        visible={this.state.visible}
                        onCreate={this.onFinish}
                        onCancel={this.onClose}
                    />
                </div>
            </div>
        )
    }
}

export default Address;
