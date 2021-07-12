// 书籍销量榜单
import React, {useContext, useEffect, useRef, useState} from 'react';
import 'antd/dist/antd.css';
import '../../css/component.css'
import {Button, Col, DatePicker, Form, Input, Row, Space, Table} from 'antd';
// eslint-disable-next-line no-unused-vars
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {getRankedBooks} from "../../services/hotService";
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

class StatisticsTable extends React.Component {
    constructor(props) {
        super(props);
        this.columns = [
            {
                title: 'Book Name',
                dataIndex: ['book', 'name'],
                textWrap: 'word-break',
            },
            {
                title: 'Inventory',
                dataIndex: ['book', 'inventory'],
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
                textWrap: 'word-break',
            },
            {
                title: 'Amount',
                dataIndex: 'amount',
                textWrap: 'word-break',
            },
            {
                title: 'Total',
                dataIndex: 'total',
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
        getRankedBooks(null, null, 0, this.callback1);
        console.log(this.state)
    }

    handleSortAmount = () => {
        getRankedBooks(null, null, 0, this.callback1);
    };

    handleSortTotal = () => {
        getRankedBooks(null, null, 1, this.callback1);
    };

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
        moment.locale('zh-cn')
        if (value === null)
            getRankedBooks('null',
                'null', this.callback1);
        else
            getRankedBooks(moment(value[0]).format('YYYY-MM-DD HH:mm:ss'),
                moment(value[1]).format('YYYY-MM-DD HH:mm:ss'), 0, this.callback1);
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

        const paginationProps = {
            showQuickJumper: true,
            pageSize: this.state.pageSize,
            total: this.state.data.size,  //数据的总的条数
            onChange: null,
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
                <div class="row">
                    <Space direction="vertical" size={12}>
                        <Row>
                            <Col id="rangePicker">
                                <RangePicker showTime
                                             onChange={this.timeCallback}/>
                            </Col>
                            <Col>
                                <div className="row">
                                    <div className="btn-group" role="group" aria-label="..." id="button_grp">
                                        <button type="button" className="btn btn-default"
                                                onClick={this.handleSortAmount}>
                                            Sort By Amount
                                        </button>
                                        <button type="button" className="btn btn-default"
                                                onClick={this.handleSortTotal}>
                                            Sort By Total Value
                                        </button>
                                    </div>
                                </div>
                            </Col>

                        </Row>

                        <br/>
                    </Space>

                    <br/>
                    <Table
                        columns={columns}
                        dataSource={this.state.data}
                        onChange={onChange}
                        pagination={paginationProps}
                        rowClassName={() => "editable-row"}
                        components={components}/>
                </div>
            </div>
        )
    }
}

export default StatisticsTable;
