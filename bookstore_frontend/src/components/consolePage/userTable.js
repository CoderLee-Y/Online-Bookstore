import React, {useContext, useState, useEffect, useRef} from 'react';
import 'antd/dist/antd.css';
import {Row, Col, Divider, Button} from 'antd';
import {Table, Input, InputNumber, Popconfirm, Form, Typography} from 'antd';
import {changeUserMode, getAdmins, getCustomers} from "../../services/userService";
import {element} from "prop-types";

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

class UserTable extends React.Component {
    constructor(props) {
        super(props);
        this.columns1 = [
            {
                title: 'user ID',
                dataIndex: 'userId',
            },
            {
                title: 'name',
                dataIndex: 'nickname',
            },
            {
                title: 'status',
                dataIndex: 'status',
                render: (_, record) => (
                    this.state.customer.length < 1 ? null :
                        (record.abandon === 0 ? (
                            <b>Normal</b>
                        ) : (
                            <b>Abandoned</b>
                        )))
            },
            {
                title: 'address',
                dataIndex: 'address',
            },
            {
                title: 'E-mail',
                dataIndex: 'email',
            },
            {
                title: 'telephone',
                dataIndex: 'tel',
            },
            {
                title: 'operation',
                dataIndex: 'operation',
                render: (_, record) => (
                    this.state.customer.length < 1 ? null :
                        (record.abandon === 0 ? (
                            <Popconfirm title="Sure to ABANDON this user?"
                                        onConfirm={() => this.handleChange(record)}>
                                <a>Abandon</a>
                            </Popconfirm>
                        ) : (
                            <Popconfirm title="Sure to restore status?"
                                        onConfirm={() => this.handleChange(record)}>
                                <a>Restore</a>
                            </Popconfirm>
                        )))
            },
        ];
        this.columns2 = [
            {
                title: 'Admin ID',
                dataIndex: 'userId',
            },
            {
                title: 'Admin Name',
                dataIndex: 'username',
            },
        ]
        this.state = {
            customer: [],
            admin: [],
        };
    }

    callback1 = (customers) => {
        customers.forEach(element => {
            element['key'] = element.userId;
        })
        this.setState({
            customer: customers,
        })
    }

    callback2 = (admin) => {
        admin.forEach(element => {
            element['key'] = element.userId;
        })
        this.setState({
            admin: admin,
        })
    }

    componentDidMount() {
        getCustomers(this.callback1);
        getAdmins(this.callback2);
        console.log(this.state)
    }

    handleDelete = (key) => {
        const dataSource = [...this.state.customer];
        this.setState({
            customer: dataSource.filter((item) => item.key !== key),
        });
    };

    handleChange = (record) => {
        console.log(record)
        changeUserMode(record.key, ()=>({}));
        const dataSrc = [...this.state.customer];
        dataSrc.forEach((value) => {
            if(value['userId'] === record.userId){
                value['abandon'] = value['abandon'] === 0?1:0;
            }
        })

        this.setState({
            customer: dataSrc,
        })
    };


    render() {
        const components = {
            body: {
                row: EditableRow,
                cell: EditableCell,
            },
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
        const columns2 = this.columns2.map((col) => {
            if (!col.editable) {
                return col;
            }

            return {
                ...col,
                onCell: (record) => ({
                    record,
                    editable: col.editable,
                    dataIndex: col.dataIndex,
                    title: col.title,
                    handleSave: this.handleSave,
                }),
            };
        });
        return (
            <div>
                <Divider plain>Customer</Divider>
                <div>
                    <Table
                        components={components}
                        rowClassName={() => 'editable-row'}
                        dataSource={this.state.customer}
                        columns={columns1}
                    />
                </div>
                <Divider plain>Administrator</Divider>
                <div>
                    <Table
                        components={components}
                        rowClassName={() => 'editable-row'}
                        dataSource={this.state.admin}
                        columns={columns2}
                    />
                </div>
            </div>

        );
    }
}

export default UserTable;
