import React from 'react';
import { Link, Redirect } from 'react-router-dom';
import {  Avatar, Icon, Tooltip, Button, Card, Popover } from 'antd';
import './Layout.scss';
import LocalStorge from '../../util/LogcalStorge.jsx';
const localStorge = new LocalStorge();
import logo from '../../asset/logo.png'

export default class TopBar extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            collapsed: false,
            visible: false,
            ishow: '0',
            userCode: localStorge.getStorage('userInfo') == '' ? '' : localStorge.getStorage('userInfo').userCode,
            userid:localStorge.getStorage('userInfo') == '' ? '' : localStorge.getStorage('userInfo').id,
        };

    }
    hide = () => {
        this.setState({
            visible: false,
        });
    }
    handleVisibleChange = (visible) => {
        this.setState({ visible });
    }
    toggle = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        },function(){
            this.props.callbackParent(this.state.collapsed);
        });
        
    }
    onCollapse(collapsed) {
        this.setState({ collapsed });
    }
    onselect(e) {
        this.setState({ ishow: e });
    }
    // 退出登录
    onLogout() {
        localStorge.removeStorage('userInfo');
        localStorge.removeStorage('lasurl');
        this.setState({ redirect: true });
    }

    linkUserInfo(){
        this.props.history.push("/user/userView/"+this.state.userid);
    }
    render() {
        if (this.state.redirect) {
            return <Redirect push to="/login" />; //or <Redirect push to="/sample?a=xxx&b=yyy" /> 传递更多参数
        }
        let showwei = 'bottom';
        let contenttwo = <li className="dropdown">
                <ul className="dropdown-menu">
                    <li style={{ margin: '10px' }}>
                        <Link to={"/user/userView/"+this.state.userid}>
                            <Icon type="user" theme="outlined" style={{ fontSize: '18px', color: '#0a0a0a' }} /> 
                            <span style={{ fontSize: '16px', marginLeft: '5px', color: '#0a0a0a' }}>个人信息</span>
                         </Link>
                    </li>
                    <li style={{ margin: '10px' }}><Link to={"/user/UpdatePwd/"+this.state.userid}>
                        <Icon type="key" theme="outlined" style={{ fontSize: '18px', color: '#0a0a0a' }} /> 
                        <span style={{ fontSize: '16px', marginLeft: '5px', color: '#0a0a0a' }}>密码修改</span></Link>
                    </li>
                    <li style={{ margin: '10px' }} ><a href="javascript:void(0)"><Icon type="setting" theme="outlined" style={{ fontSize: '18px', color: '#0a0a0a' }} />
                        <span style={{ fontSize: '16px', marginLeft: '5px', color: '#0a0a0a' }}>设置</span></a>
                    </li>
                    <li style={{ margin: '10px' }} ><a onClick={() => { this.onLogout() }}><Icon type="logout" theme="outlined" style={{ fontSize: '18px', color: '#0a0a0a' }} />
                        <span style={{ fontSize: '16px', marginLeft: '5px', color: '#0a0a0a' }}>退出</span> </a>
                    </li>
                </ul>
            </li>;
            showwei = 'bottomRight';
       
        return (
            <div className="top-navbar" style={{lineHeight:'50px',background:'#2f96e2'}}>
                <div style={{float:'left',background:'#2f96e2'}} >

                        <a href="javascript:;">
                            <img alt="logo" style={{ width: '30px', height: '30px' }} src={logo} />
                            <span style={{ marginLeft: "15px", color: "#ffffff", fontSize: "18px", fontWeight: "600" }}>数据转化平台</span>
                        </a>
                        <Tooltip title='缩回'>
                            <Icon
                                className="trigger"
                                type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}
                                onClick={this.toggle}
                            />
                        </Tooltip>
                       
                    </div>
                {/* <div style={{float:'right',background:'#2f96e2'}} >
                        {
                            this.state.userCode
                                ? <span style={{color: 'white'}}>欢迎，{this.state.userCode}</span>
                                : <span style={{color: 'white'}}>请登录</span>
                        }
                        <Tooltip>
                            <Popover
                                placement={showwei}
                                content={contenttwo}
                                trigger="click"
                                visible={this.state.visible}
                                onVisibleChange={this.handleVisibleChange}
                            >
                                <Button type="primary" onClick={() => this.onselect('4')} style={{ background: 'transparent', borderColor: 'transparent' }}>
                                    <Avatar size="{32}" icon="user" />
                                </Button>
                            </Popover>
                        </Tooltip>
                        
                    </div>  */}
            </div>   
        );
    }
}

