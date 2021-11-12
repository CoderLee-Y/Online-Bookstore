import React from 'react';
import 'antd/dist/antd.css';
import {Row, Col, Divider, Space} from 'antd';
import {Comment, Avatar, Form, Button, List, Input} from 'antd';
import moment from 'moment';
import {addComment, getBook} from "../../services/bookService";

const {TextArea} = Input;

const CommentList = ({comments}) => (
    <List
        dataSource={comments}
        header={`${comments.length} ${comments.length > 1 ? 'replies' : 'reply'}`}
        itemLayout="horizontal"
        renderItem={props => <Comment {...props} />}
    />
);


class BookComment extends React.Component {
    constructor(props) {
        super(props);
        console.log(this.props.comment)
        this.state = {
            textValue: "",
            submitting: false,
            comments: this.props.comment.bookRemark,
            bookId: this.props.comment.bookId
        }
        console.log(this.state.comments)
    }

    onChange = (data) => {
        this.setState({
            textValue: data.target.value
        })
    }

    addCallback = (data) => {
        console.log(data)
        this.setState({
            submitting: false,
            textValue: ""
        });
    }

    handleSubmit = () => {
        if (!this.state.textValue) {
            return;
        }
        this.setState({
            submitting: true,
        });

        console.log(this.state.textValue, this.props.comment.bookId)
        addComment(this.props.comment.bookId, this.state.textValue, this.addCallback)
    };

    render() {
        return (
            <div>
                <>
                    <List
                        className="comment-list"
                        header={`${this.state.comments.comment.length} book comments`}
                        itemLayout="horizontal"
                        dataSource={this.state.comments.comment}
                        renderItem={item => (
                            <li>
                                <Comment
                                    author={item.authorId}
                                    avatar={'https://joeschmoe.io/api/v1/random'}
                                    content={item.text}
                                    datetime={moment.now()}
                                />
                            </li>
                        )}
                    />

                    <Form.Item>
                        <TextArea rows={4} onChange={this.onChange} value={this.state.textValue}/>
                    </Form.Item>
                    <Form.Item>
                        <Button htmlType="submit" loading={this.state.submitting}
                                onClick={this.handleSubmit} type="primary">
                            Add Comment
                        </Button>
                    </Form.Item>
                </>
            </div>
        )
    }
}

export default BookComment;
