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
            applications: [

            ],
            courses: [
                
            ],
            professors: [

            ]
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
            this.updateApplications();
            this.updateCourses();
            this.updateProfessors();
        }

        ws.onmessage = (event) => {
            console.log('Client received: ' + event.data);
            if(event.data == 'get-applications') {
                this.updateApplications();
            }
            if(event.data == 'get-professor') {
                this.updateProfessors();
            }
            if(event.data == 'get-courses') {
                this.updateCourses();
            }
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

    //Send Socket message
    sendMessage = (msg) => {
        console.log('Client sends ' + msg);
        this.state.webSocket.send(msg);
    }


    sendUser = (ws) => {

        if(ws == null) {
            console.log("unable to find websocket");
            return;
        }
        let uString = JSON.stringify({attachUser:this.props.getUser()});
        console.log('Client sends: ' + uString);
        ws.send(uString);
    }
    updateProfessors = async () => {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'text/html'},
        }

        await fetch('/api/get-all-professors', requestOptions)
            .then(response => response.json())
            .then(res => {
                if(res.error) {
                    console.log("unable to update the list of professors");
                    return;
                }
                console.log(res);
                let formattedList = [];
                for(let i in res) {
                    let formatted = {};
                    formatted.name = res[i].name;
                    formatted.id = res[i].id;
                    formattedList.push(formatted);
                }
                this.setState({professors: formattedList});
            })
    }
    updateApplications = async () => {
        if(this.state.webSocket == null) {
            console.log("no socket connection found");
            return;
        }
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'text/html'},
        };

        await fetch('/api/get-applications', requestOptions)
            .then(response => response.json())
            .then(res => {

                //update this.state.data.applictions
                let formatted = res;
                
                //formatting the response to be of type {name, type}
                for(let i = 0; i < formatted.length; i++) {
                    delete formatted[i].password;
                    formatted[i].name = formatted[i].firstname + " " + formatted[i].lastname
                    delete formatted[i].firstname;
                    delete formatted[i].lastname;
                }                
                //Set local state so render gets called
                this.setState({applications: formatted})
            })
    }

    setPage = (pageName) => {
        this.setState({page: pageName});
    }

    updateCourses = async () => {
        //make a request to /api/get-user-courses-minimal

        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type':'text/html'},
            body: JSON.stringify(this.props.getUser())
        }

        await fetch('/api/get-user-courses-minimal', requestOptions)
            .then(response => response.json())
            .then(res => {
                //update our list of courses to be the response
                if(res.error) {
                    console.log("error getting this user's course information");
                    return;
                }
                console.log(res);
                this.setState({courses:res})
            })
    }


    render() {

        let u = this.props.getUser();

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
                                <AdminMainSection 
                                    app={this} 
                                    updateApplications={this.updateApplications} 
                                    updateCourses={this.updateCourses} 
                                    updateProfessors={this.updateProfessors} 
                                    courses={this.state.courses} 
                                    applications={this.state.applications} 
                                    professors={this.state.professors} />
                            </div>
                        </div>
                        
                    </div>
            </div>
        );
    }
}

export default AdminDashboard;