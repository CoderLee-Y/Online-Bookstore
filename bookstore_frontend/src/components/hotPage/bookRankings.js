import React from 'react';
import 'antd/dist/antd.css';
import { Row, Col, Divider, Space, List, Avatar } from 'antd';
import { MessageOutlined, LikeOutlined, StarOutlined } from '@ant-design/icons';

const listData = [
    {
        href: '/Detail?id=2',
        src: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1817888330,1382207609&fm=224&gp=0.jpg',
        title: `深入理解计算机系统`,
        avatar: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1817888330,1382207609&fm=224&gp=0.jpg',
        description:
            '系统软件的入门教程。',
        content:
            '优化程序性能”是全书最闪光的章节。作者对一个例子不断优化，讲循环效率和过程调用，到讲存储器引用，一直讲到现代处理器的结构，讲到IA32处理器的局限，一路下来，畅快淋漓。还将IA32处理器的优化结果和Compaq Alpha 21164做对比，一目了然的看出哪些优化是处理器相关的，最后讲到profiling(程序剖析)指导优化，可以说平时能够用到的最高技巧（毕竟我还不是编译器开发人员……）和流程走了一边，现实意义相当之高。',
    },
    {
        href: '/Detail?id=32',
        title: `编译原理：龙`,
        avatar: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4190271963,3321500896&fm=11&gp=0.jpg',
        description:
            '图灵奖得主力作。',
        src: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4190271963,3321500896&fm=11&gp=0.jpg',
        content:
            '本书全面、深入地探讨了编译器设计方面的重要主题，包括词法分析、语法分析、语法制导定义和语法制导翻译、运行时刻环境、目标代码生成、代码优化技术、并行性检测以及过程间分析技术，并在相关章节中给出大量的实例。与上一版相比，本书进行了全面的修订，涵盖了编译器开发方面的最新进展。每章中都提供了大量的系统及参考文献。\n' +
            '\n' +
            '本书是编译原理课程方面的经典教材，内容丰富，适合作为高等院校计算机及相关专业本科生及研究生的编译原理课程的教材，也是广大技术人员的极佳参考读物。',
    }

];
const IconText = ({ icon, text }) => (
    <Space>
        {React.createElement(icon)}
        {text}
    </Space>
);
class BookRanking extends React.Component {
    render() {
        return (
            <div>
                <List
                    itemLayout="vertical"
                    size="large"
                    pagination={{
                        onChange: page => {
                            console.log(page);
                        },
                        pageSize: 3,
                    }}
                    dataSource={listData}
                    renderItem={item => (
                        <List.Item
                            key={item.title}
                            actions={[
                                <IconText icon={StarOutlined} text="156" key="list-vertical-star-o" />,
                                <IconText icon={LikeOutlined} text="156" key="list-vertical-like-o" />,
                                <IconText icon={MessageOutlined} text="2" key="list-vertical-message" />,
                            ]}
                            extra={
                                <img
                                    height={172}
                                    alt="logo"
                                    src={item.src}
                                />
                            }
                        >
                            <List.Item.Meta
                                avatar={<Avatar src={item.avatar} />}
                                title={<a href={item.href}>{item.title}</a>}
                                description={item.description}
                            />
                            {item.content}
                        </List.Item>
                    )}
                />
            </div>
        )
    }
}

export default BookRanking;
