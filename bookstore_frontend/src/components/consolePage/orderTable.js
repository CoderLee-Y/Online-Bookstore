import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import '../../css/component.css'
import {Table, Badge, Menu, Dropdown, Popconfirm} from 'antd';
import OrderItemTable from './orderItemTable'
import {finishOrder, getAllOrders} from "../../services/orderService";
import { DatePicker, Space } from 'antd';
import moment from "moment";
import { Input} from 'antd';
import { AudioOutlined } from '@ant-design/icons';

const { Search } = Input;

// 管理员的订单页面
const { RangePicker } = DatePicker;

class ConsoleOrderTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            order_items: [],
            backup: [],
        }
    }

    callback = (data) => {
        console.log(data)
        data.forEach(element => {
            element['key'] = element.orderId;
            element['orderTime'] = moment(element['orderTime'],
                'x').format("dddd, MMMM Do YYYY, h:mm:ss a");
        })
        this.setState({
            order_items: data,
            backup: data,
        })
    }

    timeCallback = (value) => {
        console.log(value)
        moment.locale('zh-cn')
        if(value === null)
            getAllOrders('null',
                'null', this.callback);
        else
            getAllOrders(moment(value[0]).format('YYYY-MM-DD HH:mm:ss'),
                moment(value[1]).format('YYYY-MM-DD HH:mm:ss'), this.callback);
    }

    componentDidMount() {
        getAllOrders(null, null, this.callback);
    }

    onSearch = (value) => {
        if(value === '')
            this.setState({
                order_items: this.state.backup,
            })
        else
            this.setState({
                order_items: this.state.order_items.filter((order) => {
                        for(let i = 0; i < order.items.length; ++i)
                        {
                            let orderItem = order.items[i];
                            console.log(orderItem)
                            if(orderItem.book.name.indexOf(value) >= 0)
                                return true;
                        }
                        return false;
                    })
            })
    }

    handleChange = (value) => {
        finishOrder(value['orderId']);
        let data = this.state.order_items;
        data.forEach((element) => {
            if(element['orderId'] === value['orderId'])
            {
                element['status'] = 1;
            }
        })
        this.setState({
            order_items: data,
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
                    status === 1 ?
                        <span>
                    <Badge status="success"/>
                          Finished
                    </span> :
                        <span>
                    <Badge status="warning"/>
                          In Transit
                    </span>),
            },
            {title: 'Action', key: 'operation',
                render: (_, record) => (
                            record['status'] === 0?
                            (<Popconfirm title="This Order would be set finished"
                                        onConfirm={() => this.handleChange(record)}>
                                <a>Finish</a>
                            </Popconfirm>):null)

            },
        ];

        return (
            <div>
                <Space direction="vertical" size={12}>
                    <RangePicker showTime
                                 onChange={this.timeCallback}/>

                    <Search
                        placeholder="Input Search Text"
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
                        expandable={{expandedRowRender: record =>
                                <OrderItemTable record={record}/>}}
                        dataSource={this.state.order_items}
                    />
                </div>
            </div>
        );
    }
}

export default ConsoleOrderTable;