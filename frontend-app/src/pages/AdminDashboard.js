import React, { Component } from "react"
import { Redirect } from "react-router";

//ICONS
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import { 
    faHome, 
    faSignOutAlt, 
    faSchool} from '@fortawesome/free-solid-svg-icons'

import AdminMainSection from "../components/AdminMainSection";

class AdminDashboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            page: "dashboard",
            modalOpen: false,
            currentCourse: 0,
            whichModal: 0,
            webSocket: null,
            data: {
                courses: [
                    {
                        name: "Intro to Computer Science",
                        code: "COMP1001"
                    },
                    {
                        name: "Intro to Mathematics 1",
                        code: "MATH1001"
                    },
                    {
                        name: "Cognitive Science - Mysteries of the Mind",
                        code: "CGSC1001"
                    },
                    {
                        name: "Dinosaurs",
                        code: "ERTH2401"
                    },
                    {
                        name: "Intro to Philosophy",
                        code: "PHIL1001"
                    },
                    {
                        name: "Intro to Web Development",
                        code: "COMP2406"
                    }
                ]
            }
        }
    }
    componentDidMount() {
        this.connect();
    }
    connect = () => {
        let ws = new WebSocket('ws://localhost:8080/api/websocket', 'subprotocol.demo.websocket');
        ws.onopen = () => {
            console.log('Client connection opened');

            console.log('Subprotocol: ' + ws.protocol);
            console.log('Extensions: ' + ws.extensions);
            this.sendUser(ws);
        }
        ws.onmessage = (event) => {
            console.log('Client received: ' + event.data);
        }
        ws.onerror = (event) => {
            console.log('Client error: ' + event.data);
        }
        ws.onclose = (event) => {
            console.log('Client connection closed: ' + event.code);
        }
        this.setState({webSocket: ws})
    }
    disconnect = () => {
        if(this.state.webSocket != null) {
            this.state.webSocket.close();
            this.state.webSocket = null;
        }
    }
    sendMessage = (msg) => {
        console.log('Client sends ' + msg);
        this.state.webSocket.send(msg);
    }


    //Local stuff

    sendUser = (ws) => {

        if(ws == null) {
            console.log("unable to find websocket");
            return;
        }
        let uString = JSON.stringify({attachUser:this.props.getUser()});
        console.log('Client sends: ' + uString);
        ws.send(uString);
    }

    setPage = (pageName) => {
        this.setState({page: pageName});
    }


    render() {

        let u = this.props.getUser();

        // if (u.username == null){
        //     return (
        //         <Redirect to="/"></Redirect>
        //     )
        // }

        if (this.state.page == "logout"){

            this.props.logout();

            return (
                <Redirect to="/"></Redirect>
            )
        }

        return (
            <div class="w-full min-w-0 mx-auto">
                    <div class="flex rounded-md shadow w-screen">
                        <div className="bg-gray-50 rounded-l-md border-r border-dashed w-30 border-gray-300">
                            <div class="flex justify-center text-gray-300 border-b items-center h-24 text-center font-semibold text-4xl italic">
                                <FontAwesomeIcon className="pb-1" size="md" icon={faSchool}/>
                            </div>
                            <div class="border-gray-300 pt-1">
                                <a onClick={() => this.setPage("dashboard")}
                                className={`shadow-md m-4 rounded-md ${(this.state.page == "courses") ? "bg-white font-bold border-2 border-gray-900 text-lg block py-6 px-6 cursor-pointer text-center text-yellow-500" : "bg-black text-white hover:text-yellow-500 text-lg block py-6 px-6 cursor-pointer text-center border border-gray-300"}`}>
                                    <FontAwesomeIcon size="lg" icon={faHome}/>
                                </a>
                                <a onClick={() => this.setPage("logout")}
                                 className={`shadow-md m-4 rounded-md ${(this.state.page == "logout") ? "bg-white font-bold border-2 border-gray-900 text-lg block py-6 px-6 cursor-pointer text-center" : "text-white bg-black hover:text-red-500 hover:bg-white hover:border-red-500 text-lg block py-6 px-6 cursor-pointer text-center border-2"}`}>
                                    <FontAwesomeIcon size="lg" icon={faSignOutAlt}/>
                                </a>
                            </div>
                            {/* <button onClick={this.sendUser} className="w-5 rounded-lg bg-gray-200">Send User info</button> */}
                        </div>
                        <div class="flex-grow w-10/12">
                            <div className={`mx-auto bg-gray-100 py-3 border-b-4 h-12 ${(this.state.page == "dashboard") ? "border-black" : ""}`}>
                                <div class="flex items-end pl-10">
                                    <div className="text-md font-mono text-black">
                                        LOGGED-IN=<span className="font-semibold pr-10">ADMIN</span>
                                        USERTYPE=<span className="font-semibold">{this.props.getUser().type}</span>
                                    </div>
                                </div>
                            </div>
                            <div className={`${(this.state.page == "dashboard") ? "" : "hidden"}`}>
                                <AdminMainSection app={this} />
                            </div>
                        </div>
                        
                    </div>
            </div>
        );
    }
}

export default AdminDashboard;