import React from 'react';
import 'antd/dist/antd.css';
import { List, message, Spin } from 'antd';
import BookCol from '../bookCol'
import WindowScroller from 'react-virtualized/dist/commonjs/WindowScroller';
import AutoSizer from 'react-virtualized/dist/commonjs/AutoSizer';
import VList from 'react-virtualized/dist/commonjs/List';
import InfiniteLoader from 'react-virtualized/dist/commonjs/InfiniteLoader';

const bookdata = []
for (let i = 0; i < 100; ++i)
    bookdata.push(<BookCol />);

class InfiniteList extends React.Component {
    state = {
        data: [],
        loading: false,
    };

    loadedRowsMap = {};

    componentDidMount() {
        this.setState({
            data: bookdata,
        })
    }


    handleInfiniteOnLoad = ({ startIndex, stopIndex }) => {
        let { data } = this.state;
        this.setState({
            loading: true,
        });
        for (let i = startIndex; i <= stopIndex; i++) {
            // 1 means loading
            this.loadedRowsMap[i] = 1;
        }
        if (data.length > 100) {
            message.warning('Virtualized List loaded all');
            this.setState({
                loading: false,
            });
            return;
        }
    };

    isRowLoaded = ({ index }) => !!this.loadedRowsMap[index];

    renderItem = ({ index, key, style }) => {
        const { data } = this.state;
        const item = data[index];
        return (
            item
        );
    };

    render() {
        const { data } = this.state;
        const vlist = ({ height, isScrolling, onChildScroll, scrollTop, onRowsRendered, width }) => (
            <VList
                autoHeight
                height={height}
                isScrolling={isScrolling}
                onScroll={onChildScroll}
                overscanRowCount={2}
                rowCount={data.length}
                rowHeight={100}
                rowRenderer={this.renderItem}
                onRowsRendered={onRowsRendered}
                scrollTop={scrollTop}
                width={width}
            />
        );
        const autoSize = ({ height, isScrolling, onChildScroll, scrollTop, onRowsRendered }) => (
            <AutoSizer disableHeight>
                {({ width }) =>
                    vlist({
                        height,
                        isScrolling,
                        onChildScroll,
                        scrollTop,
                        onRowsRendered,
                        width,
                    })
                }
            </AutoSizer>
        );
        const infiniteLoader = ({ height, isScrolling, onChildScroll, scrollTop }) => (
            <InfiniteLoader
                isRowLoaded={this.isRowLoaded}
                loadMoreRows={this.handleInfiniteOnLoad}
                rowCount={data.length}
            >
                {({ onRowsRendered }) =>
                    autoSize({
                        height,
                        isScrolling,
                        onChildScroll,
                        scrollTop,
                        onRowsRendered,
                    })
                }
            </InfiniteLoader>
        );
        return (
            <List>
                {data.length > 0 && <WindowScroller>{infiniteLoader}</WindowScroller>}
                {this.state.loading && <Spin className="demo-loading" />}
            </List>
        );
    }
}

export default InfiniteList;