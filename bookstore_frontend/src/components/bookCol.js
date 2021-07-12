import React from 'react';
import 'antd/dist/antd.css';
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css'
import '../css/component.css'
import BookSingleView from './singleBookView';

class BookCol extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: this.props.bookData,
        }
    }

    render() {
        return (
            <div class="container-fluid">
                <div class="row">
                    {
                        this.state.data.map(
                            (BookData, index) =>
                                <div class="col-xs-6 col-md-4" key={index}>
                                    <div span={3}>
                                        <BookSingleView
                                            data={BookData}
                                        />
                                    </div>
                                </div>
                        )
                    }
                </div>
            </div>


        )
    }
}

export default BookCol;
