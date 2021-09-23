import React from 'react';
import 'antd/dist/antd.css';
import {Col, Row} from 'antd';
import Navbar from "../components/navbar";
import Footer from "../components/homePage/footer";
import SockJsClient from 'react-stomp';
import {TalkBox} from "react-talk";
import {getAuth} from "../services/userService";


class ChatRoomView extends React.Component {
    constructor(props) {
        super(props);
        // randomUserId is used to emulate a unique user id for this demo usage
        this.state = {
            clientConnected: false,
            replyTo: '',
            destination: '',
            messages: [],
        };
    }

    AuthCallback = (value) => {
        this.setState({
            user: value,
        })
        console.log(value)
        if (value.userIdentity !== 2) {
            this.setState({
                replyTo: 'server1',
                destination: '/websocket/service',
                messages: [{
                    "author": "Server1",
                    "authorId": "2",
                    "message": "Welcome to iBookStore, I'm your counselor.",
                    "timestamp": Date.now().toString()
                }]
            })
        }
    }

    async componentDidMount() {
        await getAuth(this.AuthCallback);
    }

    onMessageReceive = (msg, topic) => {
        const message = {
            "author": msg.from,
            "authorId": msg.from,
            "message": msg.content,
            "timestamp": Date.now().toString()
        };

        this.setState(prevState => ({
            messages: [...prevState.messages, message],
        }));

        // if you're a server, just reply to most recently asking.
        if(this.state.user.userIdentity === 2){
            this.setState({
                replyTo: msg.from,
                destination: msg.destination,
            })
        }
    }

    sendMessage = (msg, selfMsg) => {
        const data = {
            content: msg,
            targetUser: this.state.replyTo,
            destination: this.state.destination,
        }
        selfMsg.timestamp = Date.now().toString();
        console.log(selfMsg)
        this.setState(prevState => ({
            messages: [...prevState.messages, selfMsg]
        }));
        try {
            this.clientRef.sendMessage("/app/sendToPoint", JSON.stringify(data));
            return true;
        } catch (e) {
            return false;
        }
    }


    render() {
        return (
            <div>
                <Navbar active="book"/>

                <Row>
                    <Col span={20} offset={2}>
                        <div>
                            {/* here we use REAL username */}
                            <TalkBox topic={"My iBookstore Chatting"}
                                     currentUserId='You'
                                     currentUser='You'
                                     messages={this.state.messages}
                                     onSendMessage={this.sendMessage}
                                     style={{width: 1300, marginTop: 60, marginBottom: 60}}
                                     connected={this.state.clientConnected}/>

                            <SockJsClient url='http://localhost:8080/iBookStore'
                                          topics={['/user/websocket/service']}
                                          onMessage={this.onMessageReceive}
                                          ref={(client) => {
                                              this.clientRef = client
                                          }}
                                          onConnect={() => {
                                              this.setState({clientConnected: true})
                                          }}
                                          onDisconnect={() => {
                                              this.setState({clientConnected: false})
                                          }}
                                          debug={false}/>
                        </div>
                    </Col>
                </Row>
                <Footer/>
            </div>
        )
    }
}

export default ChatRoomView;
