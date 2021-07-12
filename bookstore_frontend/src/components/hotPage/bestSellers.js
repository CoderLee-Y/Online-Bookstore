import React from 'react';
import 'antd/dist/antd.css';
import '../../css/component.css'
import BookRanking from './bookRankings'
import { Row, Col, Divider, Space, Layout, Menu } from 'antd';
import {
    UserOutlined,
    UploadOutlined,
    VideoCameraOutlined,
} from '@ant-design/icons';
const { Content, Sider } = Layout;

class StarBooks extends React.Component {
    render() {
        return (
            <div>
                <Layout>
                    <Sider className="site-layout-background"
                        style={{
                            overflow: 'auto',
                            height: '100vh',
                            position: 'relative',
                            color: "white",
                            left: 0,
                        }}
                    >
                        <div className="logo" />
                        <Menu
                            class="sideMenu" mode="inline"
                            defaultSelectedKeys={['1']}>
                            <Menu.Item id="menuItem" key="1" icon={<UserOutlined />}>
                                All BestSellers
                            </Menu.Item>
                        </Menu>
                    </Sider>
                    <Layout className="site-layout" style={{ marginLeft: 50 }}>
                        <Content style={{ margin: '16px 16px 0', overflow: 'initial' }}>
                            <div class="row">
                                <div class="col-md-12">
                                    <BookRanking />
                                </div>
                            </div>
                        </Content>
                    </Layout>
                </Layout>
            </div >
        )
    }
}

export default StarBooks;
