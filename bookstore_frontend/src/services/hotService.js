import {postRequest, postRequest_v2} from "../utils/ajax";
import {message} from "antd";
import {history} from "../utils/history";


export const getRankedBooks = (start, end, sortId, callback) => {
    const data = {
        start: start === null? "null": start,
        end: end === null? "null": end,
        sortId: sortId,
    };
    const url = 'http://localhost:8080/getBestSellers';
    console.log(data);
    postRequest(url, data, callback);
};

export const getRankedUsers = (start, end, callback) => {
    const data = {
        start: start === null? "null": start,
        end: end === null? "null": end,
    };
    const url = 'http://localhost:8080/bigFans';
    console.log(data);
    postRequest(url, data, callback);
};
