import {postRequest, postRequest_v2} from "../utils/ajax";
import {message, Button, notification} from "antd";
import {history} from "../utils/history";

const openNotificationAbandon = placement => {
    notification.info({
        message: `Sorry, You Failed to login.`,
        description:
            'Your account was abandoned, please contact with admin.',
        placement,
        onClick: () => {
            console.log('Notification Clicked!');
        },
    });
};

const openNotificationFail = placement => {
    notification.info({
        message: `Sorry, You Failed to login.`,
        description:
            'Your account name or password is wrong, please confirm again',
        placement,
        onClick: () => {
            console.log('Notification Clicked!');
        },
    });
};

export function login (data) {
    const callback = result => {
        console.log(result)
        if (result.status === -1) {
            openNotificationFail("bottomRight");
        }
        else if (result.status > 0) {
            localStorage.setItem('user', JSON.stringify(result.data));
            history.push("/");
            history.go(0);
            message.success(result.message);
        } else {
            openNotificationAbandon("bottomRight");
        }
    };

    const url = 'http://localhost:8080/Login';
    postRequest(url, data, callback);
}

export function checkSession(callback) {
    const url = 'http://localhost:8080/checkSession';
    postRequest(url, {}, callback);
}

export function getAuth(callback) {
    const url = 'http://localhost:8080/getAuth';
    postRequest(url, {}, callback);
}

export function checkAdmin(callback) {
    const url = 'http://localhost:8080/checkAdmin';
    postRequest(url, {}, callback);
}

export function logout() {
    const url = 'http://localhost:8080/logout';
    const callback = data => {
        if (data.status > 0) {
            localStorage.removeItem('user');
            history.push("/");
            history.go(0);
            message.success(data.message);
        } else {
            message.error(data.message);
        }
    };
    postRequest(url, {}, callback);
}



export function getUser(callback) {
    const url = 'http://localhost:8080/getUser';
    postRequest(url, {}, callback);
}

export function getUserById(userId, callback) {
    const url = `http://localhost:8080/getUserById?userId=${userId}`;
    postRequest(url, {}, callback);
}

export function getCustomers(callback) {
    const url = `http://localhost:8080/getCustomers`;
    postRequest(url, {}, callback);
}

export function getAdmins(callback) {
    const url = `http://localhost:8080/getAdmins`;
    postRequest(url, {}, callback);
}

export function changeUserMode(userId, callback) {
    const url = `http://localhost:8080/changeUserMode`;
    postRequest_v2(url, {userId: userId}, callback);
}

export function register(values) {
    const url = `http://localhost:8080/register`;
    postRequest(url, {file: values}, () => ({}));
}

const openNotification = placement => {
    notification.info({
        message: `Duplicate E-mail`,
        description:
            'Your E-mail has been occupied. Please use new E-mail so that we could differentiate you.',
        placement,
        onClick: () => {
            console.log('Notification Clicked!');
        },
    });
};

export function isDup(values) {
    const url = `http://localhost:8080/isDupName`;
    const callback = (data) => {
        if(data){
            openNotification('bottomRight')
        }
        else{
            register(values);
            window.location.href = "/successRegister";
        }
    }
    postRequest(url, {file: values}, callback);
}

export function testDuplicate(values, callback) {
    const url = `http://localhost:8080/testDupName`;
    postRequest(url, values, callback);
}

export function changeReceipt(values) {
    const url = `http://localhost:8080/changeReceipt`;
    const data = {
        file: values,
    }
    postRequest(url, data, () => {});
}

export function getReceipt(callback) {
    const url = `http://localhost:8080/getReceipt`;
    postRequest(url, {}, callback);
}