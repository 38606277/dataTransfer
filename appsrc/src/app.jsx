import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Switch, Redirect, Route} from 'react-router-dom'

import Loadable from 'react-loadable';
import loading from './util/loading.jsx'
import './App.css'
import  LocalStorge         from './util/LogcalStorge.jsx';
const localStorge = new LocalStorge();

// import Layout from './page/main/Layout.jsx';

const Layout = Loadable({
    loader: () => import('./page/main/Layout.jsx'),
    loading: loading,
    delay:3000
});


const UserRouter = Loadable({
    loader: () => import('./page/user/router.jsx'),
    loading: loading,
    delay:3000
});


const Login = Loadable({
    loader: () => import('./page/login/index.jsx'),
    loading: loading,
    delay:3000
});

const Home = Loadable({
    loader: () => import('./page/home/index.jsx'),
    loading: loading,
    delay:3000
});



const TaskRouter = Loadable({
    loader: () => import('./page/task/routerTask.jsx'),
    loading: loading,
    delay:3000
});


class App extends React.Component {
    render() {

        // let LayoutRouter = (
        //     <Layout>
        //                 <Switch>
        //                     <Route exact path="/" component={Home} />
        //                     <Route path="/dashboard" component={dashboardRouter}/>
        //                     <Route path="/task" component={TaskRouter}/>
        //                     <Route path="/user" component={UserRouter}/>
        //                     <Route path="/dbs" component={DbsRouter}/>
        //                     <Route path="/rule" component={RuleRouter}/>
        //                     <Route path="/Auth" component={Auth}/>
        //                     <Route path="/role" component={RoleRouter}/>
        //                     <Route path="/authType" component={AuthTypeRouter}/>
        //                     <Route path="/query" component={QueryRouter}/>
        //                     <Route path="/dict" component={DictRouter}/>
        //                     <Route path="/function" component={FunctionRouter}/>
        //                     <Route path="/cube" component={CubeRouter}/>
    
        //                 </Switch>
        //             </Layout>
        // );
        let LayoutRouter = (nextState, replace) => {
            if(undefined!=localStorge.getStorage('userInfo') && ''!=localStorge.getStorage('userInfo')){
                return (
                    <Layout>
                        <Switch>
                            <Route exact path="/" component={Home} />
                            <Route path="/task" component={TaskRouter}/>
                            <Route path="/user" component={UserRouter}/>
                        </Switch>
                    </Layout>
                );
            }else{
                localStorage.setItem('lasurl', nextState.location.pathname);
              return (<Redirect to="/login"/>);
            }
        }
        return (
            <Router>
                <Switch>
                    <Route path="/login" component={Login} />
                    <Route path="/" render={LayoutRouter} />
                    {/* <Route path="/" render={props=>LayoutRouter} /> */}
                </Switch>
            </Router>
        )
    }
}


ReactDOM.render(
    <App />,
    document.getElementById('app')
);
