
import React            from 'react';
import { HashRouter as Router, Switch, Redirect, Route, Link } from 'react-router-dom'
// 页面
import TaskList from './taskList.jsx';
import TaskInfo from './taskInfo.jsx';
import TaskView from './taskView.jsx';

class RouterTask extends React.Component{
    render(){
        return (
            <Switch>
                 <Route path="/task/taskList" component={TaskList} />
                 <Route path="/task/taskInfo/:id" component={TaskInfo} />
                 <Route path="/task/taskView/:id" component={TaskView} />
                 <Redirect exact from="/task" to="/task/taskList"/> 
            </Switch>
        )
    }
}
export default RouterTask;