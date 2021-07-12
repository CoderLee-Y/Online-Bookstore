import React from 'react';
import {Route, Redirect} from 'react-router-dom'
import {checkAdmin, checkSession} from "../services/userService";
import {Modal} from 'antd';

export class AdminRoute extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isAdmin: false,
            hasChecked: false,
        }
    }

    checkAuthority = data => {
        console.log(data);
        let isAdmin = (data.status === 0);
        this.setState({
            isAdmin: isAdmin,
            hasChecked: true
        })
    };

    componentDidMount() {
        checkAdmin(this.checkAuthority);
    }

    render() {
        const {component: Component, path = "/", exact = false, strict = false} = this.props;

        if (!this.state.hasChecked) return null;

        return <Route path={path} exact={exact} strict={strict} render={props => (
            this.state.isAdmin ? (
                    <Component {...props}/>
                ) :
                <Redirect to={{
                    pathname: '/permissionDenied',
                    state: {from: props.location}
                }}/>
        )}/>
    }
}

export default AdminRoute;