import React from 'react';
import 'antd/dist/antd.css';
import '../css/component.css'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css'
import {Card, Image, Button, notification, Tag} from 'antd';
import {addCartItems} from '../services/cartService'
import {history} from '../utils/history'

import {Link} from 'react-router-dom'
import {element} from "prop-types";

const {Meta} = Card;

function goToLogin(){
    window.location.href = "/Login";
}

function OpenNotification(){
    notification.open({
        message: 'Success',
        description:
            'The book has been added to your Cart.',
        className: 'custom-class',
        top: 180,
        style: {
            width: 500,
        },
    });
}

function OpenNotification_Admin(){
    notification.open({
        message: 'Fail',
        description:
            'You need to register as our customer to use our services.',
        className: 'custom-class',
        top: 180,
        style: {
            width: 500,
        },
    });
}

class BookSingleView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            data: this.props.data,
        };
    }


    onChange = checked => {
        this.setState(
            {
            loading: !checked
        });
    };


    emptyCallback = ()=>{
    }


    addCart = () => {
        let user = null;
        user = (localStorage.getItem("user") !== null)?
            JSON.parse(localStorage.getItem("user")):
            null;
        console.log(user);
        (user !== null && user['userIdentity'] === 1)? OpenNotification_Admin(): (user !== null?
        addCartItems(user.userId, this.props.data.bookId, this.emptyCallback):
        goToLogin());

        (user['userIdentity'] !== 1 && user !== null)? OpenNotification(): this.emptyCallback();
    };

    render() {
        const {loading} = this.state.loading;
        return (
            <Card>
            <div className="btn-group" role="group" aria-label="...">
                <button type="button" className="btn btn-default" onClick={this.addCart}>
                                   <span className="glyphicon glyphicon-shopping-cart" aria-hidden="true">
                                   </span>
                    Add Cart
                </button>
                <button type="button" className="btn btn-default">
                    <span className="glyphicon glyphicon-star" aria-hidden="true"/>
                    Star
                </button>
            </div>
            <Link to={{
                pathname: '/Detail',
                search: '?id=' + this.props.data.bookId}}
                  // target="_blank"
            >

            <div><Card id="bookcard"
                       hoverable
                       tabBarExtraContent

                       loading={loading}
                       style={{width: 300, height: 410}}
                       cover={<img alt="bookPicture"
                                   src={"http://localhost:8080/static/bookImg/" + this.props.data.cover}
                                   width={270}
                                   height={290}
                       />}
            >
                <p id="bookName">{this.props.data.name} : {this.props.data.author}</p>
                <p id="bookPrice">￥ {this.props.data.price}</p>
                <p>Inventory: {this.props.data.inventory} & ISBN: {this.props.data.isbn}</p>
            </Card>
            </div>
            </Link>
            </Card>
        );
    }
}

export default BookSingleView;