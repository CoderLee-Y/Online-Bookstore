import React, { useContext, useState, useEffect, useRef } from 'react';
import 'antd/dist/antd.css';
import '../../css/console.css'
import {Tooltip, Input, Button, Popconfirm, Form, Table, Space, Pagination} from 'antd';
import '../../css/bootstrap-3.3.7-dist/css//bootstrap.css';
import Highlighter from 'react-highlight-words';
import { SearchOutlined } from '@ant-design/icons';
import {deleteBookById, getBooks, saveBook, addBook, searchBooks} from "../../services/bookService";
import { DatePicker } from 'antd';

const { RangePicker } = DatePicker;
const { Search } = Input;
const EditableContext = React.createContext(null);

const EditableRow = ({ index, ...props }) => {
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
            handleSave({ ...record, ...values });
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
                <Input ref={inputRef} onPressEnter={save} onBlur={save} />
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

class BookManageTable extends React.Component {
    constructor(props) {
        super(props);
        this.columns = [
            {
                title: 'Book Name',
                dataIndex: 'name',
                editable: true,
                textWrap: 'word-break',
                ...this.getColumnSearchProps('name'),
            },
            {
                title: 'Author',
                dataIndex: 'author',
                editable: true,
                textWrap: 'word-break',
                ...this.getColumnSearchProps('author'),
            },
            {
                title: 'Inventory',
                dataIndex: 'inventory',
                editable: true,
                textWrap: 'word-break',
                sorter: {
                    compare: (a, b) => a.inventory - b.inventory,
                },
                ...this.getColumnSearchProps('inventory'),
            },
            {
                title: 'Description',
                dataIndex: 'description',
                editable: true,
                textWrap: 'word-break',
                ellipsis: {
                    showTitle: false,
                },
                render: description => (
                    <Tooltip placement="topLeft" title={description}>
                        {description}
                    </Tooltip>
                ),
                ...this.getColumnSearchProps('description'),
            },
            {
                title: 'Type',
                dataIndex: 'type',

                editable: true,
                textWrap: 'word-break',
                ...this.getColumnSearchProps('type'),
            },
            {
                title: 'price',
                dataIndex: 'price',
                sorter: {
                    compare: (a, b) => a.price - b.price,
                },
                editable: true,
                textWrap: 'word-break',
                ...this.getColumnSearchProps('price'),
            },
            {
                title: 'ISBN',
                dataIndex: 'isbn',
                sorter: {
                    compare: (a, b) => a.isbn - b.isbn,
                },
                textWrap: 'word-break',
                editable: true,
                ...this.getColumnSearchProps('isbn'),
            },
            {
                title: 'Picture Source',
                dataIndex: 'image',
                textWrap: true,
                editable: true,
                width: "10%",
                ellipsis: {
                    showTitle: true,
                    ellipsis: true,
                },
                ...this.getColumnSearchProps('image'),
            },
            {
                title: 'Local',
                dataIndex: 'cover',
                textWrap: true,
                editable: true,
                width: "4%",
                ellipsis: {
                    showTitle: true,
                    ellipsis: true,
                },
            },
            {
                title: 'operation',
                dataIndex: 'operation',
                render: (_, record) =>
                    this.state.data.content.length >= 1 ? (
                        <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.key)}>
                            <a>Delete</a>
                        </Popconfirm>
                    ) : null,
            },
        ];
        this.state = {
            data: [],
            count: 0,
            sortId: 0,
            searchText: '',
            searchedColumn: '',
        }
    }


    componentDidMount() {
        getBooks(0, this.state.sortId, this.callback1)
    }

    callback1 = (data) => {
        console.log(data)
        data.content.forEach(element => {
            element['key'] = element.bookId;
        })

        this.setState({
            data: Object.assign(data,
                {content: data.content.filter((item) => item.inventory !== -1)}),
            newId: 0,
        })
}

    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ width: 188, marginBottom: 8, display: 'block' }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Search
          </Button>
                    <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
                        Reset
          </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            confirm({ closeDropdown: false });
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
        filterIcon: filtered => <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />,
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
                    highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                    text
                ),
    });

    handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex,
        });
    };

    handleDelete = (key) => {
        console.log(key)
        const dataSource = [...this.state.data.content];
        deleteBookById(key, ()=>({}))
        this.setState({
            data: Object.assign(this.state.data,
                {content: dataSource.filter((item) => item.key !== key)}),
        });
    };

    handleReset = clearFilters => {
        clearFilters();
        this.setState({ searchText: '' });
    };

    handleNewId = (book) => {
        book['key'] = book.bookId;
        this.setState({
            data: Object.assign(this.state.data,
                {content: [...this.state.data.content, book]}),
        })
    }

    handleAdd = () => {
        addBook(this.handleNewId);
    };

    handleSortISBN = () => {
        this.setState({
            sortId: 1,
        })
        getBooks(0, this.state.sortId, this.callback1);
    };

    handleSortPrice = () => {
        this.setState({
            sortId: 5,
        })
        getBooks(0, this.state.sortId, this.callback1);
    };

    handleSortInventory = () => {
        this.setState({
            sortId: 3,
        })
        getBooks(0, this.state.sortId, this.callback1);
    };

    handleSortDefault = () => {
        this.setState({
            sortId: 0,
        })
        getBooks(0, this.state.sortId, this.callback1);
    };

    // 需要用update 来更新数据库
    handleSave = async (row) => {
        const newData = [...this.state.data.content];
        const index = newData.findIndex((item) => row.key === item.key);

        saveBook(row, () => ({}));
        console.log(row)
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        this.setState({
            data: Object.assign(this.state.data,
                {content: newData}),
        });
    };

    handlePage = (value) => {
        getBooks(value - 1, this.state.sortId, this.callback1);
    };

    onSearch = (value) => {
        this.setState({
            sortId: 0,
        })
        searchBooks(0, value, this.callback1);
    }

    render() {
        const components = {
            body: {
                row: EditableRow,
                cell: EditableCell
            }
        };

        const paginationProps = {
            showQuickJumper: true,
            pageSize: this.state.data.size,
            total: this.state.data.totalElements,  //数据的总的条数
            current: this.state.data.number + 1,
            defaultCurrent: 1,
            onChange: this.handlePage,
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
                    handleSave: this.handleSave
                })
            };
        });

        return (
            <div>
                <div class="row">
                    <br />
                    <div class="btn-group" role="group" aria-label="..." id="cosolePage_button_grp">
                        <button type="button" class="btn btn-default" onClick={this.handleAdd}>
                            Add Book
                        </button>
                        <button type="button" className="btn btn-default" onClick={this.handleSortDefault}>
                            Reset
                        </button>
                        <button type="button" className="btn btn-default" onClick={this.handleSortISBN}>
                            Global Sort: ISBN
                        </button>
                        <button type="button" className="btn btn-default" onClick={this.handleSortPrice}>
                            Global Sort: Price
                        </button>
                        <button type="button" className="btn btn-default" onClick={this.handleSortInventory}>
                            Global Sort: Inventory
                        </button>
                        <Search placeholder="Search by book name" onSearch={this.onSearch} size="large"
                                style={{width: 300}} />
                    </div>

                    <Table
                        columns={columns}
                        dataSource={this.state.data.content}
                        pagination={paginationProps}
                        rowClassName={() => "editable-row"}
                        components={components} />
                </div>
            </div>
        )
    }
}

export default BookManageTable;
