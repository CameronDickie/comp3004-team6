import { faFilePdf, faFilePowerpoint } from "@fortawesome/free-regular-svg-icons";
import { faAngleDown, faAngleUp, faChevronCircleLeft, faFile, faGraduationCap, faPlus} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";
import CourseContent from "./CourseContent";
import UploadSubmissionModal from "./UploadSubmissionModal";

class FullScreenCourseModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            fileUpload: false,
        }
    }

    toggleFileSubmissionModal = () => {
        this.setState({fileUpload: !this.state.fileUpload})
    }

    render(){
        return (
            <div id="myModal" className={`border-t-4 border-blue-500 ${this.props.dashboard.state.modalOpen ? 
            "flex h-screen w-screen bg-white rounded-sm" : ""}`}>
                <div class="flex flex-col w-full h-auto pt-2">
                    <div class="sm:px-6 lg:px-8">
                        <div class="flex items-end p-4 pt-8 pl-12">
                            <div className="pr-16">
                                <FontAwesomeIcon className="cursor-pointer text-gray-500 hover:text-gray-900" size="3x" icon={faChevronCircleLeft} 
                                onClick={() => this.props.dashboard.closeModal()} /></div>
                            <div className="text-5xl">
                                <span className="font-semibold pr-10">{this.props.course.code}<span>  </span> -</span>
                                <span className="font-semibold text-gray-500">{this.props.course.name}</span>
                            </div>
                        </div>

                        <div className="grid grid-cols-2">
                            <div>
                                <CourseContent />
                            </div>
                            <div className="pt-10">
                            <div class="w-5/6 mx-auto">
                                <div class="bg-white shadow-sm rounded-lg border-2 my-6">
                                    <table class="text-left w-full border-collapse">
                                    <thead>
                                        <tr>
                                        <th class="py-4 px-8 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light">DELIVERABLES</th>
                                        <th class="py-4 px-8 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light">Actions</th>
                                        <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-grey-light">Due Date</th>
                                        </tr>
                                    </thead>
                                    <tbody>
               
                                        <tr class="hover:bg-grey-lighter">
                                            <td class="py-4 px-6 border-b border-grey-light underline">Assignment #1</td>
                                            <td class="py-4 px-6 border-b border-grey-light">
                                                <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Get Outline
                                                <FontAwesomeIcon className="ml-2 text-red-500" size="lg" icon={faFile}/></button>

                                                <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">View Grade
                                                <FontAwesomeIcon className="ml-2 text-yellow-500" size="lg" icon={faGraduationCap}/></button>

                                                {/* <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Download
                                                <FontAwesomeIcon className="ml-2 text-blue-500" size="lg" icon={faDownload}/></button> */}
                                            </td>
                                            <td class="py-4 px-6 border-b border-grey-light font-mono underline">05/08/19</td>
                                        </tr>
                                        <tr class="hover:bg-grey-lighter">
                                            <td class="py-4 px-6 border-b border-grey-light underline">Assignment #2</td>
                                            <td class="py-4 px-6 border-b border-grey-light">

                                                <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Get Outline
                                                <FontAwesomeIcon className="ml-2 text-red-500" size="lg" icon={faFile}/></button>

                                                <button onClick={this.toggleFileSubmissionModal} className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Add Submission
                                                <FontAwesomeIcon className="ml-2 text-green-500" size="lg" icon={faPlus}/></button>
                                            </td>
                                            <td class="py-4 px-6 border-b border-grey-light font-mono underline">05/08/19</td>
                                        </tr>
                                    </tbody>
                                    </table>
                                </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                {<UploadSubmissionModal show={this.state.fileUpload} toggleIt={this.toggleFileSubmissionModal} />}
            </div>
        );
    }
}

export default FullScreenCourseModal;