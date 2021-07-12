import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import '../../css/component.css'
import {Table, Badge, Menu, Dropdown, Space, Input} from 'antd';
import {DownOutlined} from '@ant-design/icons';
import OrderItemTable from './orderItemTable'
import { DatePicker } from 'antd';
import moment from "moment";
import {getAllOrdersByUser, getOrder} from "../../services/orderService";

const { Search } = Input;
const { RangePicker } = DatePicker;

function goToLogin(){
    window.location.href = "/Login";
}

class OrderTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            order_table: [],
            backup: [],
        }
    }

    callback1 = (data) => {
        console.log("order data",data);
        data.forEach((element) => {
            element['key'] = element.orderId;
            element['orderTime'] = moment(element['orderTime'],
                'x').format("dddd, MMMM Do YYYY, h:mm:ss a");
        })
        this.setState({
            order_table: data,
            backup: data,
        });
        console.log(data);
    };

    componentDidMount(){
        let user = JSON.parse(localStorage.getItem("user"));
        this.setState({user:user});

        user===null
            ? goToLogin()
            : getOrder(user.userId, this.callback1);

        console.log(user);
        this.setState({
            user: user,
        })
    }


    timeCallback = (value) => {
        console.log(value)
        moment.locale('zh-cn')
        if(value === null)
            getAllOrdersByUser(this.state.user.userId, 'null',
                'null', this.callback1);
        else
            getAllOrdersByUser(this.state.user.userId,
                moment(value[0]).format('YYYY-MM-DD HH:mm:ss'),
                moment(value[1]).format('YYYY-MM-DD HH:mm:ss'), this.callback1);
    }

    onSearch = (value) => {
        if(value === '')
            this.setState({
                order_table: this.state.backup,
            })
        else
            this.setState({
                order_table: this.state.order_table.filter((order) => {
                    for(let i = 0; i < order.items.length; ++i)
                    {
                        let orderItem = order.items[i];
                        if(orderItem.book.name.indexOf(value) >= 0)
                            return true;
                    }
                    return false;
                })
            })
    }

    render() {
        const columns = [
            {title: 'Customer ID', dataIndex: ['user', 'userId'], key: 'userId'},
            {title: 'Order ID', dataIndex: 'orderId', key: 'orderId'},
            {title: 'Order Time (CST)', dataIndex: 'orderTime', key: 'orderTime'},
            {title: 'Address', dataIndex: 'address', key: 'address'},
            {title: 'Phone Number', dataIndex: ['user', 'tel'], key: 'tel'},
            {
                title: 'Status',
                key: 'status',
                dataIndex: 'status',
                render: (status) => (
                    status===1?
                    <span>
                    <Badge status="success"/>
                          Finished
                    </span>:
                        <span>
                    <Badge status="warning"/>
                          In Transit
                    </span>),
            },
        ];

        return (
            <div>
                <Space direction="vertical" size={12}>
                    <br/>
                    <RangePicker showTime
                                 onChange={this.timeCallback}/>

                    <Search
                        placeholder="Search Orders By Book Name"
                        allowClear
                        enterButton="Search"
                        onSearch={this.onSearch}
                    />
                    <br/>
                </Space>
                <div id="orderPage">
                    <Table
                        className="components-table-demo-nested"
                        columns={columns}
                        expandable={{ expandedRowRender: record => <OrderItemTable record={record}/> }}
                        dataSource={this.state.order_table}
                    />
                </div>
            </div>
        );
    }
}

export default OrderTable;