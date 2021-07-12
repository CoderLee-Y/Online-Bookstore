import React from 'react';
import 'antd/dist/antd.css';
import '../../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import '../../css/component.css'
import {Table, Badge, Menu, Dropdown, Space} from 'antd';
import {DownOutlined} from '@ant-design/icons';
import {getOrderItems} from "../../services/orderService";
import {getBook} from '../../services/bookService'
import {element} from "prop-types";

class OrderItemTable extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            record: props.record,
        }
        console.log(this.state.record)
    }

    render() {
        const columns = [
            {title: 'Book Name', dataIndex: ['book', 'name'], key: 'name'},
            {title: 'Book ID', dataIndex: ['book', 'bookId'], key: 'bookId'},
            {title: 'Amount', dataIndex: 'amount', key: 'amount'},
            {title: 'Unit Price (RMB)', dataIndex: 'price', key: 'price'},
        ];

        return (
            <div>
                <div>
                    <Table
                        className="components-table-demo-nested"
                        columns={columns}
                        dataSource={this.state.record.items}
                        pagination={false}
                    />
                </div>
            </div>
        );
    }
}

export default OrderItemTable;