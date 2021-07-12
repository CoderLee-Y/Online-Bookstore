import {postRequest_v2} from "../utils/ajax";

export const getCartItems = (user_id, callback) => {
    const data = {user_id: user_id};
    const url = 'http://localhost:8080/getCartItems';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const deleteCartItems = (user_id, book_id, callback) => {
    const data = {user_id: user_id, book_id: book_id};
    const url = 'http://localhost:8080/deleteCartItems';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const addCartItems = (user_id, book_id, callback) => {
    const data = {user_id: user_id, book_id: book_id};
    console.log(user_id, book_id);
    const url = 'http://localhost:8080/addCartItems';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const createOrder = (user_id, book_id, amount, price, callback) => {
    const data = {user_id: user_id, book_id: book_id, amount: amount, price: price};
    console.log(user_id, book_id);
    const url = 'http://localhost:8080/createOrder';
    console.log(data);
    postRequest_v2(url, data, callback);
};

export const checkInventory = (book_id, amount, callback) => {
    const data = {book_id: book_id, amount: amount};
    const url = 'http://localhost:8080/checkInventory';
    postRequest_v2(url, data, callback);
};