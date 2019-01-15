(window.webpackJsonp=window.webpackJsonp||[]).push([[4],{577:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}(),l=f(a(1)),r=a(189),o=f(a(604)),u=f(a(605)),i=f(a(606)),s=f(a(607));function f(e){return e&&e.__esModule?e:{default:e}}var d=function(e){function t(){return function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t),function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).apply(this,arguments))}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,l.default.Component),n(t,[{key:"render",value:function(){return l.default.createElement(r.Switch,null,l.default.createElement(r.Route,{path:"/Job/JobList",component:o.default}),l.default.createElement(r.Route,{path:"/Job/JobInfo/:id",component:u.default}),l.default.createElement(r.Route,{path:"/Job/JobExecInfo/:id",component:i.default}),l.default.createElement(r.Route,{path:"/Job/JobLog/:id",component:s.default}),l.default.createElement(r.Redirect,{exact:!0,from:"/Job",to:"/Job/JobList"}))}}]),t}();t.default=d},591:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var n=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(435);var l,r=a(188);var o=new((l=r)&&l.__esModule?l:{default:l}).default,u=function(){function e(){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e)}return n(e,[{key:"get",value:function(){}}],[{key:"getBaseUrl",value:function(){return window.getServerUrl()}},{key:"post",value:function(t,a){if(null==o.getStorage("userInfo")&&"/reportServer/user/encodePwd"!=t&&"/reportServer/user/Reactlogin"!=t||""==o.getStorage("userInfo")&&"/reportServer/user/encodePwd"!=t&&"/reportServer/user/Reactlogin"!=t)return window.location.href="#login",new Promise(function(e,t){});var n=e.getBaseUrl()+t,l={method:"POST",headers:{"Access-Control-Allow-Origin":"*",credentials:JSON.stringify(o.getStorage("userInfo")||"")},body:a};return fetch(n,l).then(function(e){return e.json()}).catch(function(e){return e.json()})}}]),e}();t.default=u},593:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n,l=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}(),r=a(591),o=(n=r)&&n.__esModule?n:{default:n};var u=function(){function e(){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e)}return l(e,[{key:"getList",value:function(e){return o.default.post("transfer/job/getAllJob",JSON.stringify(e))}},{key:"getJobExecuteByJobId",value:function(e){return o.default.post("transfer/jobExecute/getJobExecuteByJobId",JSON.stringify(e))}},{key:"getJobExecutePorcess",value:function(e){return o.default.post("transfer/jobExecute/getJobExecutePorcess",JSON.stringify({job_execute_id:e}))}},{key:"getJobInfo",value:function(e){return o.default.post("transfer/job/getJobById",JSON.stringify({id:e}))}},{key:"save",value:function(e){return"null"==e.id?o.default.post("transfer/job/createJob",JSON.stringify(e)):o.default.post("transfer/job/updateJob",JSON.stringify(e))}},{key:"delJob",value:function(e){return o.default.post("transfer/job/deleteJob",JSON.stringify({id:e}))}},{key:"executeJob",value:function(e){return o.default.post("transfer/job/executeJob",JSON.stringify({id:e}))}},{key:"pauseJob",value:function(e){return o.default.post("transfer/job/pauseJob",JSON.stringify({id:e}))}},{key:"resumeJob",value:function(e){return o.default.post("transfer/job/resumeJob",JSON.stringify({id:e}))}},{key:"getAllTransfer",value:function(){return o.default.post("transfer/sql/getAllTransferList",null)}}]),e}();t.default=u},604:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=h(a(568)),l=h(a(426)),r=h(a(433)),o=h(a(34)),u=h(a(138)),i=h(a(436)),s=h(a(428)),f=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(564),a(427),a(434),a(104),a(190),a(437),a(78);var d=h(a(1)),c=a(189),p=h(a(593)),m=h(a(438));function h(e){return e&&e.__esModule?e:{default:e}}new(h(a(188)).default);var b=new p.default,y=s.default.Search,v=function(e){function t(e){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var a=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));return a.handleOk=function(e){a.setState({visible:!1,pageNumd:1})},a.handleCancel=function(e){a.setState({visible:!1})},a.state={list:[],pageNum:1,perPage:10,listType:"list",searchKeyword:"",dictionaryList:[],pageNumd:1,searchDictionary:"",paramValue:"",totald:0},a}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,d.default.Component),f(t,[{key:"componentDidMount",value:function(){this.loadJobList()}},{key:"loadJobList",value:function(){var e=this,t={};t.pageNum=this.state.pageNum,t.perPage=this.state.perPage,"search"===this.state.listType&&(t.keyword=this.state.searchKeyword),b.getList(t).then(function(t){e.setState({list:t.data.resultRows,total:t.data.resultTotal})},function(t){e.setState({list:[]})})}},{key:"onPageNumChange",value:function(e){var t=this;this.setState({pageNum:e},function(){t.loadJobList()})}},{key:"onValueChange",value:function(e){var t=e.target.name,a=e.target.value.trim();this.setState(function(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}({},t,a))}},{key:"onSearch",value:function(e){var t=this,a=""===e?"list":"search";this.setState({listType:a,pageNum:1,searchKeyword:e},function(){t.loadJobList()})}},{key:"deleteJob",value:function(e,t){var a=this;if("1"==t)return alert("正在运行中不能删除！"),!1;confirm("确认删除吗？")&&b.delJob(e).then(function(e){alert("删除成功"),a.loadJobList()},function(e){alert("删除失败")})}},{key:"stopJob",value:function(e,t){var a=this;0==t?b.executeJob(e).then(function(e){alert("启动成功"),a.loadJobList()},function(e){alert("启动失败")}):b.pauseJob(e).then(function(e){alert("暂停成功"),a.loadJobList()},function(e){alert("暂停失败")})}},{key:"openModelClick",value:function(e){this.setState({visible:!0,dictionaryList:[],paramValue:e,totald:0},function(){this.loadModelData(e)})}},{key:"loadModelData",value:function(e){var t=this,a={};a.pageNumd=this.state.pageNumd,a.perPaged=10,a.searchDictionary=this.state.searchDictionary,a.job_id=e,b.getJobExecuteByJobId(a).then(function(e){t.setState({dictionaryList:e.data.resultRows,totald:e.data.resultTotal})}).catch(function(e){t.setState({loading:!1}),message.error(e)})}},{key:"onPageNumdChange",value:function(e){var t=this;this.setState({pageNumd:e},function(){t.loadModelData(t.state.paramValue)})}},{key:"render",value:function(){var e=this,t=(this.state.list,[{title:"任务编号",dataIndex:"id",key:"id"},{title:"任务名称（英文）",dataIndex:"job_name",key:"job_name",render:function(e,t,a){return d.default.createElement(c.Link,{to:"/Job/JobLog/"+t.id},e)}},{title:"任务组别",dataIndex:"job_group",key:"job_group"},{title:"任务表达式",dataIndex:"job_cron",key:"job_cron"},{title:"任务描述",dataIndex:"job_describe",key:"job_describe"},{title:"运行状态",dataIndex:"job_status",key:"job_status",render:function(e,t){return d.default.createElement("span",null,"0"==t.job_status?"停用":"启用")}},{title:"操作",dataIndex:"操作",render:function(t,a){return d.default.createElement("span",null,d.default.createElement(c.Link,{to:"/Job/JobInfo/"+a.id},"编辑"),d.default.createElement(i.default,{type:"vertical"}),d.default.createElement("a",{onClick:function(){return e.deleteJob(""+a.id,""+a.job_status)},href:"javascript:;"},"删除"),d.default.createElement(i.default,{type:"vertical"}),d.default.createElement("a",{onClick:function(){return e.stopJob(""+a.id,""+a.job_status)},href:"javascript:;"},"1"==a.job_status?"暂停":"启用"),d.default.createElement(i.default,{type:"vertical"}),d.default.createElement("a",{onClick:function(t){return e.openModelClick(""+a.id)},href:"javascript:;"},"查看任务执行"))}}]),a=[{title:"任务编号",dataIndex:"id",key:"id"},{title:"开始时间",dataIndex:"begin_time",key:"begin_time"},{title:"结束进间",dataIndex:"end_time",key:"end_time"},{title:"执行结果",dataIndex:"job_process",key:"job_process"},{title:"失败原因",dataIndex:"job_failure_reason",key:"job_failure_reason"},{title:"任务状态",dataIndex:"job_status",key:"job_status",render:function(e,t){return d.default.createElement("span",null,"0"==t.job_status?"暂停":"启用")}},{title:"操作",dataIndex:"操作",render:function(e,t){return d.default.createElement("span",null,d.default.createElement(c.Link,{to:"/Job/jobLog/"+t.id},"日志"))}}];return d.default.createElement("div",{id:"page-wrapper"},d.default.createElement(l.default,{title:"任务列表"},d.default.createElement(u.default,null,d.default.createElement(y,{style:{width:300,marginBottom:"10px"},placeholder:"请输入...",enterButton:"查询",onSearch:function(t){return e.onSearch(t)}})),d.default.createElement(u.default,null,d.default.createElement(o.default,{href:"#/Job/JobInfo/null",style:{float:"right",marginRight:"30px"},type:"primary"},"新建任务")),d.default.createElement(r.default,{dataSource:this.state.list,columns:t,pagination:!1}),d.default.createElement(m.default,{current:this.state.pageNum,total:this.state.total,showTotal:function(t){return"共 "+e.state.total+"条"},onChange:function(t){return e.onPageNumChange(t)}})),d.default.createElement("div",null,d.default.createElement(n.default,{title:"执行结果列表",width:"800px",visible:this.state.visible,onOk:this.handleOk,onCancel:this.handleCancel},d.default.createElement(r.default,{ref:"diction",columns:a,dataSource:this.state.dictionaryList,size:"small",bordered:!0,pagination:!1}),d.default.createElement(m.default,{current:this.state.pageNumd,total:this.state.totald,showTotal:function(e){return"共 100条"},onChange:function(t){return e.onPageNumdChange(t)}}))))}}]),t}();t.default=v},605:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=h(a(568)),l=h(a(426)),r=h(a(34)),o=h(a(48)),u=h(a(33)),i=h(a(428)),s=h(a(66)),f=h(a(431)),d=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var a=arguments[t];for(var n in a)Object.prototype.hasOwnProperty.call(a,n)&&(e[n]=a[n])}return e},c=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(564),a(427),a(104),a(429),a(430),a(78),a(191),a(432);var p=h(a(1)),m=h(a(188));function h(e){return e&&e.__esModule?e:{default:e}}function b(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}var y=new(h(a(593)).default),v=new m.default,g=f.default.Item,E=s.default.Option,w=function(e){function t(e){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var a=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));return a.handleOk=function(e){var t=a.state.seconds+" "+a.state.minutes+" "+a.state.hours+" "+a.state.day+" "+a.state.month+" "+a.state.week+" "+a.state.year;""==a.state.year.trim()&&(t=t.substring(0,t.length-1)),a.setState({visible:!1,jobCron:t,seconds:"",minutes:"",hours:"",day:"",month:"",week:"",year:""}),a.props.form.setFieldsValue(b({},"job_cron",t))},a.handleCancel=function(e){a.setState({visible:!1,seconds:"",minutes:"",hours:"",day:"",month:"",week:"",year:""})},a.handleOktwo=function(e){a.setState({visibletwo:!1,mkey1:"global_param",mvalue1:"",mkey2:"transfer_id",mvalue2:"",mkey3:"task_path",mvalue3:""}),a.props.form.setFieldsValue(b({},"job_param","{"+a.state.mkey1+':"'+a.state.mvalue1+'",'+a.state.mkey2+':"'+a.state.mvalue2+'",'+a.state.mkey3+':"'+a.state.mvalue3+'"}'))},a.handleCanceltwo=function(e){a.setState({visibletwo:!1,mkey1:"global_param",mvalue1:"",mkey2:"transfer_id",mvalue2:"",mkey3:"task_path",mvalue3:""})},a.state={confirmDirty:!1,id:a.props.match.params.id,visible:!1,visibletwo:!1,seconds:"",minutes:"",hours:"",day:"",month:"",week:"",year:"",selectTransferList:[],selectList:[],mkey1:"global_param",mvalue1:"",mkey2:"transfer_id",mvalue2:"",mkey3:"task_path",mvalue3:""},a.handleSubmit=a.handleSubmit.bind(a),a.handleConfirmBlur=a.handleConfirmBlur.bind(a),a}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,p.default.Component),c(t,[{key:"componentDidMount",value:function(){var e=this,t=[{id:0,name:"停用"},{id:1,name:"启用"}],a=[],n=[];y.getAllTransfer().then(function(t){if(console.log(t),"3000"!=t.resultCode){for(var n=t.data,l=0;l<n.length;l++)a.push(p.default.createElement(E,{key:n[l].transfer_id,value:n[l].transfer_id},n[l].transfer_name));e.setState({selectTransferList:a})}});for(var l=0;l<t.length;l++)n.push(p.default.createElement(E,{key:t[l].id,value:t[l].id},t[l].name));this.setState({selectList:n}),null!=this.state.id&&""!=this.state.id&&"null"!=this.state.id&&y.getJobInfo(this.state.id).then(function(t){e.setState(t.data),e.props.form.setFieldsValue(t.data)},function(t){e.setState({})})}},{key:"onValueChange",value:function(e){var t=e.target.name,a=e.target.value.trim();this.props.form.setFieldsValue(b({},t,a))}},{key:"onValueChangetwo",value:function(e){var t=e.target.name,a=e.target.value.trim();this.setState(b({},t,a))}},{key:"onSelectChange",value:function(e,t){this.setState(b({},e,t)),this.props.form.setFieldsValue(b({},e,t))}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault(),this.props.form.validateFieldsAndScroll(function(e,a){console.log(a),e||(a.id=t.state.id,y.save(a).then(function(e){alert("保存成功"),window.location.href="#/Job"},function(e){t.setState({}),v.errorTips(e)}))})}},{key:"handleConfirmBlur",value:function(e){var t=e.target.value;this.setState({confirmDirty:this.state.confirmDirty||!!t})}},{key:"openModelClick",value:function(e){var t=e.target.value,a="",n="",l="",r="",o="",u="",i="";if(""!=t&&null!=t){var s=t.split(" ");a=s[0],n=s[1],l=s[2],r=s[3],o=s[4],u=s[5],i=s[6]}this.setState({visible:!0,seconds:a,minutes:n,hours:l,day:r,month:o,week:u,year:i},function(){})}},{key:"openModelClickTwo",value:function(e){e.target.value;this.setState({visibletwo:!0},function(){})}},{key:"render",value:function(){var e=this,t=this.props.form.getFieldDecorator,a={labelCol:{xs:{span:24},sm:{span:8}},wrapperCol:{xs:{span:24},sm:{span:16}}};return p.default.createElement("div",{id:"page-wrapper"},p.default.createElement(l.default,{title:"null"==this.state.id?"新建任务":"编辑任务"},p.default.createElement(f.default,{onSubmit:this.handleSubmit},p.default.createElement(o.default,null,p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"任务名称（英文）"}),t("job_name",{rules:[{required:!0,message:"请输入任务名称（英文）!"}]})(p.default.createElement(i.default,{type:"text",name:"job_name"})))),p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"任务组别（英文）"}),t("job_group",{rules:[{required:!0,message:"请选择任务组别（英文）!",whitespace:!0}]})(p.default.createElement(i.default,{type:"text",name:"job_group"}))))),p.default.createElement(o.default,null,p.default.createElement(u.default,{span:24},p.default.createElement(g,d({},{labelCol:{xs:{span:4},sm:{span:4}},wrapperCol:{xs:{span:20},sm:{pan:20}}},{label:"任务描述"}),t("job_describe",{rules:[{required:!0,message:"请输入任务描述!",whitespace:!0}]})(p.default.createElement(i.default,{type:"text",name:"job_describe"}))))),p.default.createElement(o.default,null,p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"Cron表达式"}),t("job_cron",{rules:[{required:!0,message:"请输入Cron表达式!",whitespace:!0}]})(p.default.createElement(i.default,{type:"text",name:"job_cron",onClick:function(t){return e.openModelClick(t)}})))),p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"任务脚本"}),t("transfer_id",{})(p.default.createElement(s.default,{style:{minWidth:"300px"},allowClear:!0,onChange:function(t){return e.onSelectChange("transfer_id",t)}},this.state.selectTransferList))))),p.default.createElement(o.default,null,p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"任务状态"}),t("job_status",{})(p.default.createElement(s.default,{name:"job_status",allowClear:!0,style:{minWidth:"300px"},onChange:function(t){return e.onSelectChange("job_status",t)}},this.state.selectList)))),p.default.createElement(u.default,{span:12},p.default.createElement(g,d({},a,{label:"参数"}),t("job_param",{})(p.default.createElement(i.default,{type:"text",name:"job_param",onClick:function(t){return e.openModelClickTwo(t)}}))))),p.default.createElement(g,{wrapperCol:{xs:{span:24,offset:0},sm:{span:16,offset:8}}},p.default.createElement(r.default,{type:"primary",htmlType:"submit",style:{marginLeft:"30px"}},"保存"),p.default.createElement(r.default,{href:"#/Job",type:"primary",style:{marginLeft:"30px"}},"返回")))),p.default.createElement("div",null,p.default.createElement(n.default,{title:"corn表达式",width:"800px",visible:this.state.visible,onOk:this.handleOk,onCancel:this.handleCancel},p.default.createElement(o.default,null,p.default.createElement(u.default,{span:3}),p.default.createElement(u.default,{span:3},"秒"),p.default.createElement(u.default,{span:3},"分钟"),p.default.createElement(u.default,{span:3},"时"),p.default.createElement(u.default,{span:3},"日"),p.default.createElement(u.default,{span:3},"月"),p.default.createElement(u.default,{span:3},"星期"),p.default.createElement(u.default,{span:3},"年")),p.default.createElement(o.default,null,p.default.createElement(u.default,{span:3},"表达式字段"),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"seconds",value:this.state.seconds,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"minutes",value:this.state.minutes,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"hours",value:this.state.hours,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"day",value:this.state.day,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"month",value:this.state.month,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"week",value:this.state.week,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"year",value:this.state.year,onChange:function(t){return e.onValueChangetwo(t)},style:{width:80}}))))),p.default.createElement("div",null,p.default.createElement(n.default,{title:"param",width:"600px",visible:this.state.visibletwo,onOk:this.handleOktwo,onCancel:this.handleCanceltwo},p.default.createElement(o.default,null,p.default.createElement(u.default,{span:3},"key"),p.default.createElement(u.default,{span:3,style:{width:80,marginLeft:60}},"value")),p.default.createElement(o.default,{style:{padding:"5px 5px 5px 0px"}},p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mkey1",value:this.state.mkey1,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mvalue1",value:this.state.mvalue1,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120,marginLeft:60}}))),p.default.createElement(o.default,{style:{padding:"5px 5px 5px 0px"}},p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mkey2",value:this.state.mkey2,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mvalue2",value:this.state.mvalue2,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120,marginLeft:60}}))),p.default.createElement(o.default,{style:{padding:"5px 5px 5px 0px"}},p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mkey3",value:this.state.mkey3,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120}})),p.default.createElement(u.default,{span:3},p.default.createElement(i.default,{name:"mvalue3",value:this.state.mvalue3,onChange:function(t){return e.onValueChangetwo(t)},style:{width:120,marginLeft:60}}))))))}}]),t}();t.default=f.default.create()(w)},606:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var n=p(a(426)),l=p(a(433)),r=p(a(34)),o=p(a(138)),u=p(a(428)),i=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(427),a(434),a(104),a(190),a(78);var s=p(a(1)),f=a(189),d=p(a(593)),c=p(a(438));function p(e){return e&&e.__esModule?e:{default:e}}function m(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}new(p(a(188)).default),new d.default;var h=u.default.Search,b=function(e){function t(e){var a;!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var n=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));return n.state={list:[(a={id:1,jobName:"abc",jobGroup:"ys",begin_time:"2018-1-1 12:00:00",end_time:"2018-1-1  13:00:00"},m(a,"end_time","2018-1-1  13:00:00"),m(a,"duration","1小时"),a)],pageNumd:1,perPaged:10,listType:"list",searchKeyword:""},n}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,s.default.Component),i(t,[{key:"render",value:function(){var e=this,t=this.state.list,a=[{title:"任务编号",dataIndex:"id",key:"id"},{title:"任务名称",dataIndex:"jobName",key:"jobName",render:function(e,t,a){return s.default.createElement(f.Link,{to:"/user/UserView/"+t.id},e)}},{title:"开始时间",dataIndex:"begin_time",key:"begin_time"},{title:"结束进间",dataIndex:"end_time",key:"end_time"},{title:"执行结果",dataIndex:"jobClassPath",key:"jobClassPath"},{title:"执行结果",dataIndex:"duration",key:"duration"},{title:"失败原因",dataIndex:"jobDescribe",key:"jobDescribe"},{title:"任务状态",dataIndex:"jobStatusStr",key:"jobStatusStr"},{title:"操作",dataIndex:"操作",render:function(e,t){return s.default.createElement("span",null,"1"!=t.userId?s.default.createElement(f.Link,{to:"/user/userInfo/"+t.id},"编辑"):"")}}];return s.default.createElement("div",{id:"page-wrapper"},s.default.createElement(n.default,{title:"用户列表"},s.default.createElement(o.default,null,s.default.createElement(h,{style:{width:300,marginBottom:"10px"},placeholder:"请输入...",enterButton:"查询",onSearch:function(t){return e.onSearch(t)}})),s.default.createElement(o.default,null,s.default.createElement(r.default,{href:"#/Job/JobInfo/null",style:{float:"right",marginRight:"30px"},type:"primary"},"新建任务")),s.default.createElement(l.default,{dataSource:t,columns:a,pagination:!1}),s.default.createElement(c.default,{current:this.state.pageNumd,total:this.state.total,onChange:function(t){return e.onPageNumChange(t)}})))}}]),t}();t.default=b},607:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=s(a(426)),l=s(a(433)),r=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(427),a(434);var o=s(a(1)),u=s(a(578)),i=s(a(593));function s(e){return e&&e.__esModule?e:{default:e}}new(s(a(188)).default);var f=new i.default,d=function(e){function t(e){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,t);var a=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));return a.tick=function(){var e=a.state.seconds;a.setState({seconds:e+1}),0!=a.state.total&&a.state.num!=a.state.total?a.loadlogList():0==a.state.total&&0==a.state.num&&a.loadlogList()},a.state={id:a.props.match.params.id,list:[],seconds:0,score:0,total:0,num:0},a}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,o.default.Component),r(t,[{key:"componentDidMount",value:function(){var e=this;this.interval=setInterval(function(){return e.tick()},1e3)}},{key:"componentWillUnmount",value:function(){clearInterval(this.interval)}},{key:"loadlogList",value:function(){var e=this;f.getJobExecutePorcess(this.state.id).then(function(t){if("1000"==t.resultCode){var a=parseFloat(null==t.data.current?0:t.data.current),n=parseFloat(null==t.data.count?0:t.data.count),l=n<=0?"0":Math.round(a/n*1e4)/100;e.setState({score:l,total:n,num:a})}},function(t){e.setState({list:[]})})}},{key:"render",value:function(){return o.default.createElement("div",{id:"page-wrapper"},o.default.createElement("div",{style:{marginLeft:"350px"}},o.default.createElement(u.default,{progress:this.state.score,size:"160",lineWidth:"25"})),o.default.createElement(n.default,{title:"日志列表"},o.default.createElement("div",null,"Seconds:",this.state.seconds),o.default.createElement(l.default,{dataSource:this.state.list,columns:[{title:"日志编号",dataIndex:"id",key:"id"},{title:"日志信息",dataIndex:"job_log",key:"job_log"}],pagination:!1})))}}]),t}();t.default=d}}]);
//# sourceMappingURL=4.js.map