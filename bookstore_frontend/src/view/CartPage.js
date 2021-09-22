import React from 'react';
import '../css/cartPage.css'
import Navbar from '../components/navbar'
import Footer from '../components/homePage/footer'
import CartTable from '../components/cartPage/cartTable'
import Address from '../components/cartPage/address'
import { Col, Row, Divider } from 'antd';
import {getCartItems} from "../services/cartService";

const add = {
    province: "ShangHai, China",
    city: "MingHang",
    detail: "ShangHai JiaoTong Univercity SE BUILDING"
}

class CartView extends React.Component {
    constructor(props) {
        super(props);
        console.log(this.props);
        this.state={
            cartInfo: [],
            user: [],
        };
    }

    callback1 = (data) => {
        this.setState({
            cartInfo: data,
        });
    };

    componentDidMount(){
        getCartItems(this.callback1);
    }

    render() {
        this.state.cartInfo.forEach((element,index) =>{
            element.amount=1;
            element.key=index;
        });

        return (
            <div>
                <Navbar active="cart" />
                <Divider>Your Cart</Divider>
                <Row gutter={[60, 60]}>
                    <Col offset={3} span={18}>
                        <Address addr={add} />
                    </Col>
                    <Col offset={3} span={18}>
                        <CartTable data={this.state.cartInfo}
                                   user_data={this.state.user}/>
                    </Col>
                </Row>

                <Footer />
            </div>
        );
    }
}


export default CartView;
