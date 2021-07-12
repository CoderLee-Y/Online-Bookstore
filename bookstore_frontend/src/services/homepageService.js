import {postRequest} from "../utils/ajax";
import { message } from "antd";
import { history } from "../utils/history";

export function getHomepageContent(callback) {
    const url = `http://localhost:8080/getHomepageContent`;
    const data = {};
    postRequest(url, data, callback);
}

