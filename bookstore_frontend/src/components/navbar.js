import React from 'react';
import {getAuth, logout} from '../services/userService'
import 'antd/dist/antd.css';
import '../css/navbar.css'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import { Input, Button} from 'antd';
const { Search } = Input;

function toFavourite()
{
    window.location.href = "/MyFavourite";
}

class Navbar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
        }
    }

    AuthCallback = (value) => {
        this.setState({
            user: value,
        })
    }

    componentDidMount() {
        getAuth(this.AuthCallback);
    }

    render() {
        return (
            <div>
                <nav className="navbar navbar-default navbar-fixed-top" id="top-navbar">
                    <div className="container">
                        <div className="navbar-header">
                            <a className="navbar-brand" href="/Home">iBookStore</a>
                        </div>
                        <div id="common-use-navbar" className="navbar-collapse collapse">
                            <ul className="nav navbar-nav">
                                <li className={this.props.active === "home" ? "active" : ""}>
                                    <a href="/Home">Home</a>
                                </li>
                                <li className={this.props.active === "book" ? "active" : ""}>
                                    <a href="/Book">Books</a>
                                </li>
                                {/*<li className={this.props.active == "top" ? "active" : ""}>*/}
                                {/*    <a href="/TopSellers">Top</a>*/}
                                {/*</li>*/}
                                <li className={this.props.active === "cart" ? "active" : ""}>
                                    <a href="/Cart">Cart</a>
                                </li>
                                <li className={this.props.active === "order" ? "active" : ""}>
                                    <a href="/Order">Order</a>
                                </li>
                            </ul>

                            <ul className="nav navbar-nav navbar-right">
                                {this.state.user == null ?
                                    <li className={this.props.active === "login" ? "active" : ""}>
                                        <a href="/Login">Login</a>
                                    </li> :
                                    <div>
                                        <p className="navbar-text">Hi, {this.state.user.username}</p>
                                        <button type="button" className="btn btn-default navbar-btn"
                                                onClick={toFavourite}>
                                            <span className="glyphicon glyphicon-star" aria-hidden="true">
                                            </span>
                                        </button>
                                        <button type="button" className="btn btn-default navbar-btn"
                                                onClick={logout}>
                                            <span className="glyphicon glyphicon-log-out" aria-hidden="true">
                                            </span>
                                        </button>

                                    </div>}
                            </ul>
                            {(this.state.user != null && this.state.user.userIdentity === 1) ? <ul className="nav navbar-nav navbar-right">
                                <li className={this.props.active === "console" ? "active" : ""}>
                                    <a href="/Console">Manage Book</a>
                                </li>
                                <li className={this.props.active === "UserConsole" ? "active" : ""}>
                                    <a href="/UserConsole">Manage User</a>
                                </li>
                                <li className={this.props.active === "OrderConsole" ? "active" : ""}>
                                    <a href="/OrderConsole">Manage Order</a>
                                </li>
                                <li className={this.props.active === "Statistics" ? "active" : ""}>
                                    <a href="/statistics">Statistics</a>
                                </li>
                            </ul> : null}
                            {
                                this.state.user === null ?
                                    <ul className="nav navbar-nav navbar-right">
                                        <li className={this.props.active === "Register" ? "active" : ""}>
                                            <a href="/register">Register</a>
                                        </li>
                                    </ul>
                                    : null
                            }
                        </div>
                    </div>
                </nav>
            </div>

        );
    }
}

export default Navbar;