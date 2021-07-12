import React from 'react';
import 'antd/dist/antd.css';
import {Row, Col, Divider, Space, notification} from 'antd';
import '../../css/bootstrap-3.3.7-dist/css//bootstrap.css';
import '../../css/component.css'
import {addCartItems} from "../../services/cartService";

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

class BookDetail extends React.Component {
    emptyCallback = () => {
    }

    addCart = () => {
        let user = null;
        user = (localStorage.getItem("user") !== null)?
            JSON.parse(localStorage.getItem("user")):
            null;
        console.log(user);
        (user !== null && user['userIdentity'] === 1)? OpenNotification_Admin(): (user !== null?
            addCartItems(user.userId, this.props.bookData.bookId, this.emptyCallback):
            goToLogin());

        (user['userIdentity'] !== 1 && user !== null)? OpenNotification(): this.emptyCallback();
    };

    render() {
        console.log(this.props)
        return (
            <div class="row ant-row-top">
                <div class="col-md-4 col-md-offset-1">
                    <img src={"http://localhost:8080/static/bookImg/" + this.props.bookData.cover}
                        alt={this.props.bookData.name}
                        height="480px"
                        class="img-rounded" />
                </div>
                <div class="col-md-6 col-md-offset-1" id="bookdetail">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-12">
                                    <h4 class="textShadowed">Price:  ${this.props.bookData.price}</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <h4 class="textShadowed">ISBN:  {this.props.bookData.isbn}</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <h4 class="textShadowed">Author:  {this.props.bookData.author}</h4>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-12">
                                    <h4 className="textShadowed">Type: {this.props.bookData.type}</h4>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-12">
                                    <h4 className="textShadowed">Inventory: {this.props.bookData.inventory}</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <h4>Description:  {this.props.bookData.description}</h4>
                                </div>
                            </div>
                            <div class="row" id="buttonsInDeatial">
                                <div class="col-md-12">
                                    <div class="btn-group" role="group" aria-label="...">
                                        <button type="button" class="btn btn-default" onClick={this.addCart}>
                                            <span class="glyphicon glyphicon-shopping-cart"
                                                  aria-hidden="true" >
                                            </span>Add to cart
                                        </button>
                                        <button type="button" class="btn btn-default">
                                            <span class="glyphicon glyphicon-star" aria-hidden="true"></span> Star
                                         </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default BookDetail;
