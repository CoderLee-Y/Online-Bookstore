import React from 'react';
import 'antd/dist/antd.css';
import '../../css/component.css'
import { Layout, Menu } from 'antd';
import {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
} from '@ant-design/icons';
import BookCol from '../bookCol'
const { Sider } = Layout;

class BookMarket extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            collapsed: false,
            data: this.props.bookData,
            class: this.props.classData,
        };
    }

    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    }

    render() {
        return (
            <Layout>
                <Sider id="sidebar" class="site-layout-background" trigger={null} collapsible collapsed={this.state.collapsed}>
                    <div className="logo" />
                    <Menu class="sideMenu" mode="inline"
                        defaultSelectedKeys={['1']}>
                        {
                            this.props.classData.map(
                                (sideClass, index) =>
                                    <Menu.Item id="menuItem" key={index + 1}>
                                        <a href={sideClass.url}>
                                            {sideClass.class}
                                        </a>
                                    </Menu.Item>
                            )
                        }
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <div id="sideBarClick">
                        {React.createElement(this.state.collapsed ? MenuUnfoldOutlined : MenuFoldOutlined, {
                            className: 'trigger',
                            onClick: this.toggle,
                        })}
                    </div>
                        <div>
                            <BookCol bookData={this.state.data} />
                        </div>
                </Layout>
            </Layout >
        )
    }
}

export default BookMarket;