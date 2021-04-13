import React, { Component } from "react"
import { Redirect } from "react-router";

//ICONS
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import { 
    faFolderOpen, 
    faPlus, 
    faHome, 
    faCog, 
    faSignOutAlt, 
    faSchool,
    faTrash,} from '@fortawesome/free-solid-svg-icons'

import FullScreenCourseModal from "../components/FullScreenCourseModal";
import FullScreenRegisterModal from "../components/FullScreenRegisterModal";

class Dashboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            page: "dashboard",
            modalOpen: false,
            currentCourse: "",
            whichModal: 0,
            webSocket: null,
            courses: [{code:"COMP3004", name:"OOSE"}],
            globalCourses : [],
            mapOfStudents : [],
            finalGrades: {},
            currentCourseData: null,
            currentCourseName: "",
        }
    }

    componentWillMount() {
        //get this user's courses from the system
        this.updateCourses();
        this.getGlobalCourses();
        if (this.props.getUser().type=="Student") this.getFinalGrades();
        this.connect(); //connecting to the web socket for this user
    }

    getCourseContentData = async (code) => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                courseCode: code
            })
        };
        await fetch('/api/get-course', requestOptions)
            .then(response => response.json())
            .then(res => {
                this.setState({currentCourseData: res.wrappee})
            });
        //get the list of students associated with this course and set them to the list of students

        const requestOptions2 = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                cid: code
            })
        }
        await fetch('/api/get-all-students-course', requestOptions2)
            .then(response => response.json())
            .then(res => {
                if(res.error) {
                    alert(res.error);
                    return;
                }
                console.log(res)
                this.setState({mapOfStudents: res})
            })
    };

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
            //User.update commands
            if(event.data == "get-courses") {
                this.updateCourses(); //updating this user's list of courses
                this.getGlobalCourses(); //updating this user's list of total courses
                if(this.props.getUser().type == "Student") this.getFinalGrades();
            } else if(event.data == "removal-from-system") {
                //sign this user out
                this.disconnect();
                this.props.logout();
                alert("You have withdrawn from your last course this semester, as such you have been deleted from the system");
            } else if(event.data == "get-global-courses") {
                this.getGlobalCourses();
            } else if(event.data == "get-course-content") {
                this.getCourseContentData(this.state.currentCourse);
            } else if(event.data == "get-final-grade") {
                //do something with this final grade
                if(this.props.getUser().type == "Student") this.getFinalGrades();
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
    sendMessage = (msg) => {
        if(this.state.webSocket == null) {
            console.log("unable to find websocket");
            return;
        }
        console.log('Client sends ' + msg);
        this.state.webSocket.send(msg);
    }
    sendUser = (ws) => {

        if(ws == null) {
            console.log("unable to find websocket");
            return;
        }
        let uString = JSON.stringify({attachUser:this.props.getUser()});
        ws.send(uString);
    }
    setPage = (pageName) => {
        this.setState({page: pageName});
    }

    showModalCourse = (code, name) => {
        this.getCourseContentData(code)
            .then(() => {
                this.setState({modalOpen: true, currentCourse: code, whichModal: 0, currentCourseName: name})
            })
            if (document.getElementById('myModal') != null){
                document.getElementById('myModal').showModal()
            }
    }

    showModalRegister = () => {
        this.setState({modalOpen: true, whichModal: 1})
        if (document.getElementById('myModal') != null){
            document.getElementById('myModal').showModal()
        }
    }

    closeModal = () => {
        this.setState({modalOpen: false})
        document.getElementById('myModal').hidden = true
    }

    getFinalGrades = async () => {
        const requestOptions = { 
            method: 'POST',
            headers: {'Content-Type':'text/html'},
            body: JSON.stringify({
                studentID: this.props.getUser().studentID
            })
        }

        await fetch('/api/get-student-final-grades', requestOptions)
            .then(response => response.json())
            .then(res => {
                if(res.error) {
                    alert(res.error);
                    return;
                }
                console.log(res);
                this.setState({finalGrades: res});
            });
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
                    console.log("error getting this user's course information: " + res.error);
                    return;
                }
                this.setState({courses:res}, () => {
                })
            })
    }
    getGlobalCourses = async () => {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'text/html'},
            body: JSON.stringify(this.props.getUser())
        }

        await fetch('/api/get-global-courses', requestOptions)
            .then(response => response.json())
            .then(res => {
                if(res.error) {
                    console.log("unable to update the list of courses in the system");
                    return;
                }
                console.log("response from global courses: " + res);
                this.setState({globalCourses: res});
            })
    }
    registerInCourse = async (cid) => {
        if(!this.props.getUser().studentID) {
            console.log("error getting this user's studentID");
            return;
        }
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'text/html'},
            body: JSON.stringify({
                courseCode: cid,
                studentID: this.props.getUser().studentID
            })
        }
        console.log('sending registration request for' + cid);
        await fetch ('/api/course-registration', requestOptions)
            .then (response => response.text())
            .then (res => {
                alert(res)
                this.setState({modalOpen: false})
            })
    }
    dropCourse = async (cid) => {
        if(!this.props.getUser().studentID) {
            console.log("error getting this user's studentID");
            return;
        }
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'text/html'},
            body: JSON.stringify({
                courseCode: cid,
                studentID: this.props.getUser().studentID
            })
        }

        await fetch ('/api/course-withdrawal', requestOptions)
            .then(response => response.text())
            .then (res => {
                //determine if withdrawl was successful
                alert(res);
                //reload courses
                //bring back to home screen
            })
    }

    render() {

        let u = this.props.getUser();

        if (u.username == null){
            return (
                <Redirect to="/"></Redirect>
            )
        }

        // If model open than return this instead
        if (this.state.modalOpen){
            if (this.state.whichModal == 0){
                return (
                    <FullScreenCourseModal getUser={this.props.getUser} students={this.state.mapOfStudents} course={this.state.currentCourseData} dashboard={this} name={this.state.currentCourseName} />
                )
            } else if (this.state.whichModal == 1){
                return (
                    <FullScreenRegisterModal dashboard={this} registerInCourse={this.registerInCourse} courses={this.state.globalCourses}/>
                )
            }
        }

        if (this.state.page == "logout"){

            this.props.logout();
            this.disconnect();
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
                                className={`shadow-md m-4 rounded-md ${(this.state.page == "dashboard") ? "bg-white font-bold border-2 border-gray-900 text-lg block py-6 px-6 cursor-pointer text-center text-yellow-500" : "bg-black text-white hover:text-yellow-500 text-lg block py-6 px-6 cursor-pointer text-center border border-gray-300"}`}>
                                    <FontAwesomeIcon size="lg" icon={faHome}/>
                                </a>
                                <a onClick={() => this.setPage("settings")}
                                 className={`border-2 border-gray-200 m-4 rounded-md ${(this.state.page == "settings") ? "bg-white font-bold shadow-md border-2 border-gray-900 text-lg block py-6 px-6 cursor-pointer text-center text-black" : "text-lg block py-6 px-6 cursor-pointer text-center hover:border-gray-300"}`}>
                                    <FontAwesomeIcon size="lg" icon={faCog}/>
                                </a>
                                <a onClick={() => this.setPage("logout")}
                                 className={`shadow-md m-4 rounded-md ${(this.state.page == "logout") ? "bg-white font-bold border-2 border-gray-900 text-lg block py-6 px-6 cursor-pointer text-center" : "text-white bg-black hover:text-red-500 hover:bg-white hover:border-red-500 text-lg block py-6 px-6 cursor-pointer text-center border-2"}`}>
                                    <FontAwesomeIcon size="lg" icon={faSignOutAlt}/>
                                </a>
                            </div>
                        </div>
                        <div class="flex-grow w-10/12">
                            <div className={`mx-auto bg-gray-100 py-3 border-b-4 h-12 ${(this.state.page == "courses") ? "border-black" : ""}`}>
                                <div class="flex items-end pl-10">
                                    <div className="text-md font-mono text-black">
                                        LOGGED-IN=<span className="font-semibold pr-10">{u.username}</span>
                                        USERTYPE=<span className="font-semibold">{u.type}</span>
                                    </div>
                                </div>
                            </div>
                            <div className={`${(this.state.page == "dashboard") ? "" : "hidden"}`}>{makeCourseSection(this)}</div>
                            <div className={`${(this.state.page == "settings") ? "" : "hidden"}`}>{settings()}</div>
                        </div>
                        
                    </div>
            </div>
        );
    }
}


function makeCourseCard(i, course, app){
    return(
        <div class="w-1/3 p-4 cursor-pointer" >
            <div class="bg-white hover:bg-gray-50 bg-opacity-40 shadow-sm hover:shadow-xl border-2 border-gray-300 p-6 rounded-md hover:border-blue-600">
                <div class="w-14 h-14 inline-flex items-center justify-center rounded-full bg-gray-100 text-gray-500 mb-4">
                    <FontAwesomeIcon size="2x" icon={faFolderOpen} />
                </div>
                <h2 class="text-2xl font-medium title-font mb-2">{course.code}</h2>
                <p class="leading-relaxed text-lg">{course.name}</p>
                                        
                <div class="text-center mt-3 leading-none flex justify-between w-full">
                    <span class=" mr-3 inline-flex items-center leading-none text-sm  py-1 ">
                    <button onClick={() => {app.dropCourse(course.code)}} className={(app.props.getUser().type == "Student") ? "bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md" : "hidden"}>Drop<FontAwesomeIcon className="ml-2 text-red-500" size="lg" icon={faTrash}/></button>
                    </span>
                    <span className={`pl-4 ${app.state.finalGrades[course.code] ? "" : "hidden"}`}>
                                <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold">
                                    Grade={app.state.finalGrades[course.code]}
                                </button>
                            </span>
                    <span class=" inline-flex items-center leading-none text-sm">
                        <button onClick={() => app.showModalCourse(course.code, course.name)} className="text-lg font-semibold hover:underline">View</button>
                    </span>
                </div>
            </div>
        </div>
    )
}

function makeCourseSection(app){

    const createCourseTable = () => {
        let table = []

        for (let i in app.state.courses){
            table.push(makeCourseCard(i, app.state.courses[i], app))
        }

        return table
    }

    return (
    <div class="flex flex-col mx-24 mt-14 max-w-8xl h-full">
            <div className="mx-auto text-7xl font-bold pb-12 -ml-6 text-yellow-500"><FontAwesomeIcon className="-ml-2 pr-4 text-black" size="1x" icon={faHome}/>Dashboard</div>
            <div class="grid grid-cols-12 gap-2 -mr-2 border-b-4 border-gray-100">
                <div className="col-start-1 col-end-3 pb-6 pl-2">
                    <div className="text-5xl font-semibold font-mono text-gray-900 italic"><span className="text-gray-300">YOUR</span> COURSES:</div>
                </div>
                <div className="col-start-10 col-end-13 mt-8">
                    <div className="flex justify-end p-1 pr-4">
                        <button onClick={() => app.showModalRegister()} className={(app.props.getUser().type=="Student") ? "bg-white border-gray-800 ml-1 px-3 py-3 border-2 rounded-md font-bold shadow-md hover:shadow-lg" : "hidden"}>Add Course(s)
                        <FontAwesomeIcon className="ml-2 text-green-500" size="lg" icon={faPlus}/></button>
                    </div>
                </div>
            </div>      
            <div className="pt-4 -ml-2 -mr-2 pb-10 mx-6">
                <div class="flex flex-wrap mx-auto">
                    {createCourseTable()}
                </div>
            </div>
    </div>
    )
}

function settings(){
    return (
        <div class="flex flex-col mx-20 mt-10"><div className="text-7xl font-bold pb-20 -ml-6">
            <FontAwesomeIcon className="-ml-2 pr-4" size="1x" icon={faCog}/>Settings</div>
        </div>
    )
}

export default Dashboard;