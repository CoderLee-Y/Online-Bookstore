import React, {useContext, useEffect, useRef, useState} from 'react';
import 'antd/dist/antd.css';
import '../../css/cartPage.css'
import {Button, Card, Form, Input, Pagination, Popconfirm, Table, Tag, notification} from 'antd';
import {checkInventory, createOrder, deleteCartItems} from '../../services/cartService'

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
        console.log(record);
        console.log(dataIndex);
        form.setFieldsValue({
            [dataIndex]: record[dataIndex],
        });

    };

    const save = async () => {
        try {
            const values = await form.validateFields();
            console.log(values);
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


const openNotification = () => {
    notification.open({
        message: 'Understocked',
        description:
            'Sorry, Your quantity is more than we have in stock.',
        placement: 'bottomRight',
        onClick: () => {
            console.log('Notification Clicked!');
        },
    });
};

class CartTable extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            dataSrc: this.props.data,
            user: this.props.user_data,
            selectedElement: [],
            total: 0,
            success: 1,
        }

        this.columns = [
            {
                title: 'Book Name',
                dataIndex: 'name',
            },
            {
                title: 'Price',
                dataIndex: 'price',
            },
            {
                title: 'Amount',
                dataIndex: 'amount',
                editable: true,
            },
            {
                title: 'Inventory',
                dataIndex: 'inventory',
            },
            {
                title: 'Tag',
                dataIndex: 'type',
                render: type => <Tag color='green' key={type}>
                    {type}
                </Tag>
            },
            {
                title: 'Operation',
                dataIndex: 'operation',
                render: (_, record) =>
                    this.state.dataSrc.length >= 1 ? (
                        <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.key)}>
                            <a>Delete</a>
                        </Popconfirm>
                    ) : null,
            },
        ];
    };

    componentWillReceiveProps(nextProps) {
        this.setState({
            dataSrc: nextProps.data,
            selectedElement: [],
            total: 0,
            user: nextProps.user_data,
        });
    }

    onSelectChange = selectedRowKeys => {
        console.log('selectedRowKeys changed: ', selectedRowKeys);
        this.setState({
            selectedElement: selectedRowKeys,
        });

        let newPrice = 0;
        selectedRowKeys.forEach(element => {
            this.state.dataSrc.forEach((element1) => {
                newPrice += element1.key === element ? (element1.price * element1.amount) : 0;
                newPrice = parseFloat(newPrice.toFixed(2));
            })
        })

        this.setState({
            total: newPrice,
        });
    };

    emptyCallback = () => {
    }

    handleDelete = (key) => {
        console.log(this.state);
        let bookID = 0;
        for (let i = 0; i < this.state.dataSrc.length; ++i) {
            bookID = this.state.dataSrc[i].key === key ? this.state.dataSrc[i].bookId : 0;
            if (bookID !== 0) break;
        }

        deleteCartItems(this.state.user.userId, bookID, this.emptyCallback);
        this.state.selectedElement = this.state.selectedElement.filter((a_key) => a_key !== key);
        console.log(this.state.selectedElement)
        let dataSource = this.state.dataSrc.filter((item) => item.key !== key);

        this.setState({
            dataSrc: dataSource,
        })
        console.log("delete end:" + this.state.dataSrc)

        let newPrice = 0;
        this.state.selectedElement.forEach(element => {
            this.state.dataSrc.forEach((element1) => {
                newPrice += element1.key === element ? (element1.price * element1.amount) : 0;
                newPrice = parseFloat(newPrice.toFixed(2));
            })
        })
        this.setState({
            total: newPrice,
        });
    };

    handleSave = (row) => {
        const newData = [...this.state.dataSrc];
        console.log(newData);
        const index = newData.findIndex((item) => row.key === item.key);
        const item = newData[index];
        console.log(item)
        newData.splice(index, 1, {...item, ...row});
        let newPrice = 0;

        this.setState({
            dataSrc: newData,
        });
        this.state.selectedElement.forEach(element => {
            this.state.dataSrc.forEach((element1) => {
                newPrice += element1.key === element ? (element1.price * element1.amount) : 0;
                newPrice = parseFloat(newPrice.toFixed(2));
            })
        })

        newPrice = parseFloat(newPrice.toFixed(2));
        this.setState({
            total: newPrice,
        });
        console.log(this.state.dataSrc)
    };

    inventoryCallback = (value) => {
        console.log(value);
        this.setState({
            success: value,
        });
        if(value)
        {
            let purchaseBooks = this.state.selectedElement;

            let purchaseBookId = [];
            let purchaseAmount = [];
            let purchasePrice = [];
            console.log(purchaseBooks)

            purchaseBooks.forEach(key => {
                let bookID = 0;
                for (let i = 0; i < this.state.dataSrc.length; ++i) {
                    bookID = this.state.dataSrc[i].key === key ? this.state.dataSrc[i].bookId : 0;

                    if (bookID !== 0) {
                        purchaseBookId = [...purchaseBookId, bookID];
                        purchaseAmount = [...purchaseAmount, this.state.dataSrc[i].amount];
                        purchasePrice = [...purchasePrice, this.state.dataSrc[i].price];
                        break;
                    }
                }

                deleteCartItems(this.state.user.userId, bookID, this.emptyCallback);
                let dataSource = this.state.dataSrc.filter((item) => item.key !== key);
                this.setState({
                    dataSrc: dataSource,
                })
            })

            this.setState({
                total: 0,
                selectedElement: [],
            })

            createOrder(this.state.user.userId, purchaseBookId,
                purchaseAmount, purchasePrice, this.emptyCallback);

            //    backend: add order + jump to a successful page
            window.location.href = "/PurchaseResult"
        }
        else
        {
            openNotification();
        }
    }

    handlePurchase = () => {
        console.log("arrive purchase");

        let purchaseBooks = this.state.selectedElement;

        let purchaseBookId = [];
        let purchaseAmount = [];
        let purchasePrice = [];
        console.log(purchaseBooks)

        purchaseBooks.forEach(key => {
            let bookID = 0;
            for (let i = 0; i < this.state.dataSrc.length; ++i) {
                bookID = this.state.dataSrc[i].key === key ? this.state.dataSrc[i].bookId : 0;

                if (bookID !== 0) {
                    purchaseBookId = [...purchaseBookId, bookID];
                    purchaseAmount = [...purchaseAmount, this.state.dataSrc[i].amount];
                    purchasePrice = [...purchasePrice, this.state.dataSrc[i].price];
                    break;
                }
            }
        })

        console.log(purchaseAmount, purchaseBookId, purchasePrice)

        // now check inventory
        checkInventory(purchaseBookId, purchaseAmount, this.inventoryCallback);
    }

    title = () => {
        return 'Cart Table'
    }

    render() {
        const components = {
            body: {
                row: EditableRow,
                cell: EditableCell,
            },
        };


        const {selectedRowKeys} = this.state.selectedElement;
        const rowSelection = {
            selectedRowKeys,
            preserveSelectedRowKeys: false,
            onChange: this.onSelectChange,
            selections: [
                Table.SELECTION_ALL,
                Table.SELECTION_INVERT,
                Table.SELECTION_NONE,
            ],
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
                    handleSave: this.handleSave,
                }),
            };
        });

        return (
            <div>
                <Table rowSelection={rowSelection}
                       components={components}
                       columns={columns}
                       rowClassName={() => 'editable-row'}
                       title={this.title}
                       pagination={<Pagination showQuickJumper defaultCurrent={1} onChange={null}/>}
                       dataSource={this.state.dataSrc}/>
                <Card id="buyCard">
                    <p id="total_price">
                        <b>Price: $ </b>
                        <b>{this.state.total}</b>
                    </p>
                    <Button id="purchase_button" size="large" onClick={this.handlePurchase}>
                        Purchase
                    </Button>
                </Card>
            </div>

        )
    }
}

export default CartTable;
