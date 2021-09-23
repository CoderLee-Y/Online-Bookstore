import React from 'react';
import 'antd/dist/antd.css';
import {Row, Col} from 'antd';
import Navbar from "../components/navbar";
import Footer from "../components/homePage/footer";

let stompClient = null;

class ChatRoomView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            SOCKET_ENDPOINT: "/iBookStore",
            SUBSCRIBE_PREFIX: "/websocket/queue",
            SUBSCRIBE: "",
            SEND_ENDPOINT: "/app/test",
        }
    }

    /* 进行连接 */
    connect = () => {
        // 设置 SOCKET
        const socket = new SockJS(this.state.SOCKET_ENDPOINT);
        // 配置 STOMP 客户端
        this.setState({
            stompClient: Stomp.over(socket),
        })
        // STOMP 客户端连接
        stompClient.connect({}, (frame) => {
            alert("连接成功");
        });
    }

    /* 订阅信息 */
    subscribeSocket = () => {
        // 设置订阅地址
        this.setState({
            SUBSCRIBE: this.state.SUBSCRIBE_PREFIX + $("#subscribe").val(),
        })
        // 输出订阅地址
        alert("设置订阅地址为：" + this.state.SUBSCRIBE);
        // 执行订阅消息
        stompClient.subscribe("/user" + this.state.SUBSCRIBE, (responseBody) => {
            let receiveMessage = JSON.parse(responseBody.body);
            console.log(receiveMessage);
            $("#information").append("<tr><td>" + receiveMessage.content + "</td></tr>");
        });
    }

    /* 断开连接 */
    disconnect = () => {
        stompClient.disconnect(() => {
            alert("断开连接");
        });
    }

    /* 发送消息并指定目标地址 */
    sendMessageNoParameter = () => {
        // 设置发送的内容
        const sendContent = $("#content").val();
        // 设置发送的用户
        const sendUser = $("#targetUser").val();
        // 设置待发送的消息内容
        const message = '{"targetUser":"' + sendUser + '", "destination": "' + this.state.SUBSCRIBE + '", "content": "' + sendContent + '"}';
        // 发送消息
        this.state.stompClient.send(this.state.SEND_ENDPOINT, {}, message);
    }

    componentDidMount() {

    }


    render() {
        return (
            <div>
                <Navbar active="book" />

                <Row >
                    <Col span={20} offset={2}>

                    </Col>
                </Row>
                <Footer />
            </div>
        )
    }
}

export default ChatRoomView;
