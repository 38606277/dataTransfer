import React from 'react';
import { Link, Redirect } from 'react-router-dom';
import { Layout, Menu, Icon,Spin} from 'antd';
import  LocalStorge         from '../../util/LogcalStorge.jsx';
const localStorge = new LocalStorge();
const { Sider } = Layout;
const SubMenu = Menu.SubMenu;




export default class SiderBar extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            loading: false,
            userId:0
        };

    }
   
    render() {
       
         const collapsed=this.props.collapsed;
        return (
            <div className="navbar-side">
            <Sider
                trigger={null}
                collapsible
                collapsed={collapsed}
                theme="light"
                width='250px'
                style={{ overflow: 'auto', height: '100vh', left: 0 }}
            >
             <Spin spinning={this.state.loading} delay={100}>
                <Menu theme="light" defaultSelectedKeys={['1']} mode="inline"  >
                    <SubMenu key="sub1" title={<span><Icon type="mail" /><span>任务的列表</span></span>}>
                        <Menu.Item key="1">任务列表</Menu.Item>
                        <Menu.Item key="2"><Link to='/task'>任务列表</Link></Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub2" title={<span><Icon type="mail" /><span>脚本的列表</span></span>}>
                        <Menu.Item key="5">脚本列表</Menu.Item>
                        <Menu.Item key="6"><Link to='/user/index'>脚本列表</Link></Menu.Item>
                    </SubMenu>
                </Menu>
                </Spin>
            </Sider>
            </div>
        )
    }
}


