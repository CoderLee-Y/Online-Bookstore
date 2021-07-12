import React, {useContext, useState, useEffect, useRef} from 'react';
import 'antd/dist/antd.css';
import {Row, Col, Divider, Button} from 'antd';
import {Table, Input, InputNumber, Popconfirm, Form, Typography} from 'antd';
import {changeUserMode, getAdmins, getCustomers} from "../../services/userService";
import {element} from "prop-types";
import {DatePicker, Space} from 'antd';
import {getRankedBooks, getRankedUsers} from "../../services/hotService";
import moment from "moment";

const {RangePicker} = DatePicker;
const EditableContext = React.createContext(null);

const EditableRow = ({index, ...props}) => {
    const [form] = Form.useForm();
    return (
        <Form form={form} component={false}>
            <EditableContext.Provider value={form}>
                <tr {...props} />
            </EditableContext.Provider>
        </Form>
    );
};

const EditableCell = ({
                          title,
                          editable,
                          children,
                          dataIndex,
                          record,
                          handleSave,
                          ...restProps
                      }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef(null);
    const form = useContext(EditableContext);
    useEffect(() => {
        if (editing) {
            inputRef.current.focus();
        }
    }, [editing]);

    const toggleEdit = () => {
        setEditing(!editing);
        form.setFieldsValue({
            [dataIndex]: record[dataIndex],
        });
    };

    const save = async () => {
        try {
            const values = await form.validateFields();
            toggleEdit();
            handleSave({...record, ...values});
        } catch (errInfo) {
            console.log('Save failed:', errInfo);
        }
    };

    let childNode = children;

    if (editable) {
        childNode = editing ? (
            <Form.Item
                style={{
                    margin: 0,
                }}
                name={dataIndex}
                rules={[
                    {
                        required: true,
                        message: `${title} is required.`,
                    },
                ]}
            >
                <Input ref={inputRef} onPressEnter={save} onBlur={save}/>
            </Form.Item>
        ) : (
            <div
                className="editable-cell-value-wrap"
                style={{
                    paddingRight: 24,
                }}
                onClick={toggleEdit}
            >
                {children}
            </div>
        );
    }

    return <td {...restProps}>{childNode}</td>;
};

class UserManageTable extends React.Component {
    constructor(props) {
        super(props);

        this.columns1 = [
            {
                title: 'user ID',
                dataIndex: ['user', 'userId'],
            },
            {
                title: 'name',
                dataIndex:  ['user', 'nickname'],
            },
            {
                title: 'telephone',
                dataIndex: ['user', 'tel'],
            },
            {
                title: 'Total amount',
                dataIndex: 'amount',
            },
            {
                title: 'Total money',
                dataIndex: 'value',
            },
        ];
        this.state = {
            customer: [],
            count: 0,
            searchText: '',
            searchedColumn: '',
        };
    }

    callback1 = (customers) => {
        customers.forEach(element => {
            element['key'] = element.user.userId;
        })
        this.setState({
            customer: customers,
        })
    }

    timeCallback = (value) => {
        console.log(value)
        moment.locale('zh-cn')
        if(value === null)
            getRankedUsers('null',
                'null', this.callback1);
        else
            getRankedUsers(moment(value[0]).format('YYYY-MM-DD HH:mm:ss'),
                moment(value[1]).format('YYYY-MM-DD HH:mm:ss'), this.callback1);
    }

    componentDidMount() {
        getRankedUsers(null, null, this.callback1)
        console.log(this.state)
    }

    render() {
        const components = {
            body: {
                row: EditableRow,
                cell: EditableCell,
            },
        };

        const paginationProps = {
            showQuickJumper: true,
            pageSize: this.state.pageSize,
            total: this.state.customer.size,  //数据的总的条数
            onChange: null,
        };

        const columns1 = this.columns1.map((col) => {
            if (!col.editable) {
                return col;
            }

            return {
                ...col,
                onCell: (record) => ({
                    record,
                    dataIndex: col.dataIndex,
                    title: col.title,
                }),
            };
        });

        return (
            <div>
                <div>
                    <Space direction="vertical" size={12}>
                        <RangePicker showTime
                                     onChange={this.timeCallback}/>
                        <br/>
                    </Space>
                    <Table
                        components={components}
                        rowClassName={() => 'editable-row'}
                        dataSource={this.state.customer}
                        pagination={paginationProps}
                        columns={columns1}
                    />
                </div>
            </div>

        );
    }
}

export default UserManageTable;
