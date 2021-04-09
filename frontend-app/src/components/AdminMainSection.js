import React, { Component } from "react"

//ICONS
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import { 
    faPlus, 
    faHome, 
    faPen,
    faCheck,
    faTimes,} from '@fortawesome/free-solid-svg-icons'

import CreateCourseModal from "../components/CreateCourseModal";

class AdminMainSection extends Component{

    constructor(props){
        super(props);

        this.state = {
            data: this.props.app.state.data,
            modalOpen: false,
        }
    }

    processApplication = async (status, name, type) => {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type':'text/html'},
            body: JSON.stringify({
                name: name,
                type: type
            })
        }
        if(status) {
            //accept this application
            await fetch('/api/process-application', requestOptions)
                .then(response => response.text())
                .then(res => {
                    //make admin dashboard update 'get-applications'
                    if(res == "error") {
                        console.log('error processing application');
                    }
                    //  else this.props.updateApplications();
                })

        } else if(!status) {
            //deny this application
            await fetch ('/api/delete-application', requestOptions)
                .then(repsonse => repsonse.text())
                .then(res => {
                    //make admin dashboard update 'get-applications'
                    if(res == "error") {
                        console.log('error deleting this application')
                    }
                    //  else this.props.updateApplications()
                })

        } else {
            console.log("error with application processing ocurred");
        }
    }

    createCourseTable = () => {
        let table = []

        for (let i in this.props.courses){
            table.push(this.makeAdminCourseCard(this.props.courses[i]))
        }

        return table
    }

    createApplicationTable = () => {
        let table = []

        for (let i in this.props.applications){
            table.push(this.makeApplicationCard(this.props.applications[i]))
        }

        return table
    }


    makeAdminCourseCard = (course) => {
        return (
            <div className="p-2">
                <div className="p-3 rounded-md h-40 w-72 bg-gray-50 border-2 hover:border-blue-500 flex flex-wrap content-between">
                     <div className="w-full">
                         <div className="p-1 pl-2 font-semibold text-xl">{course.code}</div>
                         <div className="pl-2 text-kg">{course.name}</div>
                     </div>
                     <div>
                         <div class="text-center leading-none flex justify-between w-full">
                             <span class=" mr-3 inline-flex items-center leading-none text-sm  py-1 ">
                                 <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Edit<FontAwesomeIcon className="ml-2 text-yellow-500" size="lg" icon={faPen}/></button>
                             </span>
                             <span class=" inline-flex items-center leading-none text-sm pl-28">
                                 <button className="text-md font-semibold hover:underline">View</button>
                             </span>
                         </div>
                     </div>
                 </div>
            </div>
        )
    }
    
    makeApplicationCard = (application) => {
        return (
            <div class="w-full p-2">
                <div class="inline-grid lg:flex rounded-lg border-2 hover:border-gray-700 pb-6 lg:pb-0 min-w-full">
                    <div class="w-full p-4">
                        <div class="inline-grid">
                            <p class="work-sans font-semibold text-xl">{application.name}</p>
                            <p class="raleway text-sm my-2 opacity-75">{application.type}</p>
                        </div>
                    </div>
                    <div className="w-full pl-4 grid-cols-2">
                        <button onClick={() => this.processApplication(true, application.name, application.type)} className="hover:shadow-md px-3 py-2 text-md font-medium border-2 border-gray-900 rounded-lg">Accept
                        <span><FontAwesomeIcon className="ml-2 text-pink-500" size="lg" icon={faCheck}/></span></button>
                        <button onClick={() => this.processApplication(false, application.name, application.type)} className="hover:shadow-md ml-2 px-3 py-2 text-md font-medium border-2 border-gray-900 rounded-lg">Decline <span><FontAwesomeIcon className="ml-2 text-purple-500" size="lg" icon={faTimes}/></span></button>
                    </div>
                </div>
            </div>
        )
    }

    toggleCreateCourseModal = () => {
        if(this.props.professors.length == 0) {
            alert("There needs to be a professor in the system to create a course");
            return;
        }
        this.setState({modalOpen: !this.state.modalOpen})
    }

    render() {
        return (
            <div class="flex flex-col mx-20 mt-12 max-w-8xl h-full">
                    <div className="mx-auto text-7xl font-bold pb-10 -ml-6 text-gray-500"><FontAwesomeIcon className="-ml-2 pr-4 text-black" size="1x" icon={faHome}/>Admin</div>
                    <div className="pb-20 grid grid-cols-10">
                        <div className="flex col-span-5 min-h-96 border-2 rounded-lg border-gray-300 p-2 mr-2">
                            <div className="w-2/2">
                                <div class="grid grid-cols-12 gap-2 -mr-2 p-8">
                                    <div className="col-start-1 col-end-3 pl-2">
                                        <div className="text-5xl font-semibold font-mono text-gray-900 italic">COURSES:</div>
                                    </div>
                                    <div className="col-start-9 col-end-13">
                                        <div className="flex justify-end pr-4">
                                            <button onClick={this.toggleCreateCourseModal} className="bg-white border-gray-800 ml-1 px-3 py-3 border-2 rounded-md font-bold shadow-md hover:shadow-lg">Create Course
                                            <FontAwesomeIcon className="ml-2 text-green-500" size="lg" icon={faPlus}/></button>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex flex-wrap p-4 pl-8 pt-2">
                                        {this.createCourseTable()}
                                </div>
                            </div>
                        </div>
        
                        <div className="flex col-span-5 min-h-96 border-t-2 border-b-2 ml-2 border-gray-600 p-2">
                            <div className="w-2/2">
                                <div class="grid grid-cols-12 gap-2 -mr-2 p-8 pb-0">
                                    <div className="col-start-1 col-end-3 pb-2 pl-2">
                                        <div className="text-5xl font-semibold font-mono text-gray-900 italic">APPLICATIONS:</div>
                                    </div>
        
                                </div>

                                <div className="grid grid-cols-2 p-4 pl-8 pt-6">
                                    {this.createApplicationTable()}
                                </div>
                            </div>
                        </div>
        
                    </div>

                    <CreateCourseModal show={this.state.modalOpen} professors={this.props.professors} hide={this.toggleCreateCourseModal}/>
            </div>
            )
    }
}



export default AdminMainSection;