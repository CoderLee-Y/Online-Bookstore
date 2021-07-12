// 书籍销量榜单
import React, {useContext, useEffect, useRef, useState} from 'react';
import 'antd/dist/antd.css';
import {Button, DatePicker, Divider, Form, Input, Space, Table} from 'antd';
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import moment from "moment";
import Navbar from "../components/navbar";
import Footer from "../components/homePage/footer";
import {getFavourite, getOrder} from "../services/orderService";

const {RangePicker} = DatePicker;
const EditableContext = React.createContext(null);

function goToLogin(){
    window.location.href = "/Login";
}

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
            [dataIndex]: record[dataIndex]
        });
    };

    const save = async () => {
        try {
            const values = await form.validateFields();
            toggleEdit();
            handleSave({...record, ...values});
        } catch (errInfo) {
            console.log("Save failed:", errInfo);
        }
    };

    let childNode = children;

    if (editable) {
        childNode = editing ? (
            <Form.Item
                style={{
                    margin: 0
                }}
                name={dataIndex}
                rules={[
                    {
                        required: true,
                        message: `${title} is required.`
                    }
                ]}
            >
                <Input ref={inputRef} onPressEnter={save} onBlur={save}/>
            </Form.Item>
        ) : (
            <div
                className="editable-cell-value-wrap"
                style={{
                    paddingRight: 24
                }}
                onClick={toggleEdit}
            >
                {children}
            </div>
        );
    }

    return <td {...restProps}>{childNode}</td>;
};

function onChange(pagination, filters, sorter, extra) {
    console.log('params', pagination, filters, sorter, extra);
}

class MyFavourite extends React.Component {
    constructor(props) {
        super(props);
        this.columns = [
            {
                title: 'Book Name',
                dataIndex: ['book', 'name'],
                textWrap: 'word-break',
            },
            {
                title: 'Book Author',
                dataIndex: ['book', 'author'],
                textWrap: 'word-break',
            },
            {
                title: 'Type',
                dataIndex: ['book', 'type'],
                textWrap: 'word-break',
            },
            {
                title: 'ISBN',
                dataIndex: ['book', 'isbn'],
                sorter: {
                    compare: (a, b) => a.book.isbn - b.book.isbn,
                },
                textWrap: 'word-break',
            },
            {
                title: 'Amount',
                dataIndex: 'amount',
                sorter: {
                    compare: (a, b) => a.amount - b.amount,
                },
                textWrap: 'word-break',
            },
            {
                title: 'Total',
                dataIndex: 'total',
                sorter: {
                    compare: (a, b) => a.total - b.total,
                },
                textWrap: 'word-break',
            },
        ];
        this.state = {
            data: [],
            count: 0,
            searchText: '',
            searchedColumn: '',
        }
    }


    componentDidMount() {
        let user = JSON.parse(localStorage.getItem("user"));
        this.setState({user:user});

        user===null
            ? goToLogin()
            : getFavourite(user.userId, null, null, this.callback1);

        console.log(user);
        this.setState({
            user: user,
        })
    }

    callback1 = (data) => {
        console.log(data)
        data.forEach((element) => {
            element['key'] = element.book.bookId;
        })
        this.setState({
            data: data,
        })
    }

    handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex,
        });
    };

    timeCallback = (value) => {
        console.log(value)
        let user = JSON.parse(localStorage.getItem("user"));

        moment.locale('zh-cn')

        if(value === null)
            getFavourite(user.userId, null, null, this.callback1);
        else
            getFavourite(user.userId, moment(value[0]).format('YYYY-MM-DD HH:mm:ss'),
                moment(value[1]).format('YYYY-MM-DD HH:mm:ss'), this.callback1);
    }

    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{width: 188, marginBottom: 8, display: 'block'}}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{width: 90}}
                    >
                        Search
                    </Button>
                    <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{width: 90}}>
                        Reset
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            confirm({closeDropdown: false});
                            this.setState({
                                searchText: selectedKeys[0],
                                searchedColumn: dataIndex,
                            });
                        }}
                    >
                        Filter
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: filtered => <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>,
        onFilter: (value, record) =>
            record[dataIndex]
                ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
                : '',
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => this.searchInput.select(), 100);
            }
        },
        render: text =>
            this.state.searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                text
            ),
    });


    handleReset = clearFilters => {
        clearFilters();
        this.setState({searchText: ''});
    };

    render() {
        const components = {
            body: {
                row: EditableRow,
                cell: EditableCell
            }
        };

        const columns = this.columns.map((col) => {
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
                })
            };
        });
        return (
            <div>
                <Navbar active="order" />
                <div className="row">
                    <div className="col-md-8 col-md-offset-2">
                        <h2>Your Favourite Books</h2>
                        <br/>
                        <Space direction="vertical" size={12}>
                            <RangePicker showTime
                                         onChange={this.timeCallback}/>
                            <br/>
                        </Space>
                        <br/>
                        <Table
                            columns={columns}
                            dataSource={this.state.data}
                            onChange={onChange}
                            rowClassName={() => "editable-row"}
                            components={components}/>
                    </div>
                    </div>
                <Footer/>
            </div>
        )
    }
};

export default MyFavourite;
