import React from 'react';
import '../css/bookView.css'
import Navbar from '../components/navbar'
import Footer from '../components/homePage/footer'
import OrderTable from "../components/orderPage/orderTable";
import {getOrder} from "../services/orderService";
function goToLogin(){
    window.location.href = "/Login";
}

class orderView extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            order_table: [],
            order_items: [],
        };
    }

    callback1 = (data) => {
        console.log("order data",data);
        this.setState({
            order_table: data,
        });
        console.log(data);
    };

    componentDidMount(){
        let user = JSON.parse(localStorage.getItem("user"));
        this.setState({user:user});

        user===null?goToLogin():getOrder(user.userId, this.callback1);
        console.log(user);
        this.setState({
            user: user,
        })
    }

    render() {
        return (
            <div>
                <Navbar active="order"/>
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <OrderTable/>
                    </div>
                </div>
                <Footer />
            </div>
        );
    }
}


export default orderView;