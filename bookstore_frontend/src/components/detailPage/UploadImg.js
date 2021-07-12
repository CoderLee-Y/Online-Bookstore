import React from 'react';
import 'antd/dist/antd.css';
import {Modal, Upload} from 'antd';
import {PlusOutlined} from '@ant-design/icons';

function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

class LoadImg extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            previewVisible : [],
            previewImage : [],
            fileList : [],
            previewTitle : [],
        }
    }


    handleCancel = () => this.setState({previewVisible: false});

    handlePreview = async file => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj);
        }

        this.setState({
            previewImage: file.url || file.preview,
            previewVisible: true,
            previewTitle: file.name || file.url.substring(file.url.lastIndexOf('/') + 1),
        });
    };

    handleChange = ({fileList}) => this.setState({fileList});

    handleBeforeUpload = file => {
        //限制图片 格式、size、分辨率
        const isJPG = file.type === 'image/jpeg';
        const isJPEG = file.type === 'image/jpeg';
        const isGIF = file.type === 'image/gif';
        const isPNG = file.type === 'image/png';
        if (!(isJPG || isJPEG || isGIF || isPNG)) {
            Modal.error({
                title: '只能上传JPG 、JPEG 、GIF、 PNG格式的图片~',
            });
            return;
        }
        const isLt2M = file.size / 1024 / 1024 < 4;
        if (!isLt2M) {
            Modal.error({
                title: '超过4M限制.',
            });
            return;
        }

        return (isJPG || isJPEG || isGIF || isPNG) && isLt2M;
    };

    render() {
        const {previewVisible, previewImage, fileList, previewTitle} = this.state;
        const uploadButton = (
            <div>
                <PlusOutlined/>
                <div style={{marginTop: 8}}>Upload</div>
            </div>
        );
        return (
            <>
                <Upload
                    action="http://localhost:8080/uploadImg"
                    listType="picture-card"
                    fileList={this.state.fileList}
                    data={file => ({ // data里存放的是接口的请求参数
                        photoContent: file, // file 是当前正在上传的图片
                    })}
                    onPreview={this.handlePreview}
                    onChange={this.handleChange}
                    beforeUpload={this.handleBeforeUpload}
                >
                    {fileList.length >= 8 ? null : uploadButton}
                </Upload>
                <Modal
                    visible={previewVisible}
                    title={previewTitle}
                    footer={null}
                    onCancel={this.handleCancel}
                >
                    <img alt="example" style={{width: '100%'}} src={previewImage}/>
                </Modal>
            </>
        );
    }


}

export default LoadImg;
