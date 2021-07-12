import {postRequest, postRequest_v2} from "../utils/ajax";
import {message} from "antd";
import {history} from "../utils/history";


export const getOrder = (user_id, callback) => {
    const data = {user_id: user_id};
    const url = 'http://localhost:8080/getOrder';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const getOrderItems = (order_id, callback) => {
    const data = {order_id:order_id};
    const url = 'http://localhost:8080/getOrderItems';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const getAllOrders = (start, end, callback) => {
    const data = {
        start: start === null? "null": start,
        end: end === null? "null": end,
    };
    const url = 'http://localhost:8080/getAllOrders';
    console.log(data)
    postRequest(url, data, callback);
};

export const getAllOrdersByUser = (userId, start, end, callback) => {
    const data = {
        userId: userId,
        start: start === null? "null": start,
        end: end === null? "null": end,
    };
    const url = 'http://localhost:8080/getAllOrdersByUserId';
    console.log(data)
    postRequest(url, data, callback);
};

export const getFavourite = (userId, start, end, callback) => {
    const data = {
        userId: userId,
        start: start === null? "null": start,
        end: end === null? "null": end,
    };
    const url = 'http://localhost:8080/getFavourite';
    postRequest(url, data, callback);
};

export const finishOrder = (orderId) => {
    const data = {
        orderId: orderId,
    };
    const url = 'http://localhost:8080/finishOrder';
    postRequest(url, data, () => {});
};