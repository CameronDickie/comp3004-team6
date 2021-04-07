import React, { Component } from "react"

//ICONS
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import { 
    faPlus, 
    faHome, 
    faPen,} from '@fortawesome/free-solid-svg-icons'

import CreateCourseModal from "../components/CreateCourseModal";

class AdminMainSection extends Component{

    constructor(props){
        super(props);

        this.state = {
            data: this.props.app.state.data,
            modalOpen: false
        }
    }

    createCourseTable = () => {
        let table = []

        for (let i in this.state.data.courses){
            table.push(makeAdminCourseCard(this.state.data.courses[i]))
        }

        return table
    }

    toggleCreateCourseModal = () => {
        this.setState({modalOpen: !this.state.modalOpen})
    }

    render() {
        return (
            <div class="flex flex-col mx-24 mt-14 max-w-8xl h-full">
                    <div className="mx-auto text-7xl font-bold pb-12 -ml-6 text-gray-500"><FontAwesomeIcon className="-ml-2 pr-4 text-black" size="1x" icon={faHome}/>Admin</div>
                    <div className="pb-20 grid grid-cols-10 gap-2">
                        <div className="flex col-span-5 min-h-96 border-2 rounded-lg border-gray-300 p-2">
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
        
                        <div className="flex col-span-5 min-h-96 border-2 rounded-lg border-gray-300 p-2">
                            <div className="w-2/2">
                                <div class="grid grid-cols-12 gap-2 -mr-2 p-8">
                                    <div className="col-start-1 col-end-3 pb-2 pl-2">
                                        <div className="text-5xl font-semibold font-mono text-gray-900 italic">APPLICATIONS:</div>
                                    </div>
        
                                </div>
                            </div>
                        </div>
        
                    </div>

                    <CreateCourseModal show={this.state.modalOpen} hide={this.toggleCreateCourseModal}/>
        
            </div>
            )
    }
}

function makeAdminCourseCard(course){

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

export default AdminMainSection;