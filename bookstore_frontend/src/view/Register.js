import React, {useState} from 'react';
import 'antd/dist/antd.css';
import '../css/component.css'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css'
import {Form, Input, notification, Select, Popover, Checkbox, Button} from 'antd';
import Navbar from "../components/navbar";
import RegistrationForm from '../components/registerPage/RegisterForm'
import Footer from "../components/homePage/footer";

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = ({
            duplicateInfo: "Name OK"
        })
    }

    render() {
        return (
            <div>
                <Navbar active="Register"/>
                <div className="commonTallPage">
                    <div className="row">
                        <div className="col-md-4 col-md-offset-6" id="register">
                            <h2 className="form-signin-heading">Welcome</h2>
                        </div>
                        <div className="col-md-4 col-md-offset-4" id="register">
                            <RegistrationForm/>
                        </div>
                    </div>
                </div>
                <Footer/>
            </div>
        )
    }
}

export default Register;
