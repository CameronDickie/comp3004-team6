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

                        <div className="grid grid-cols-3 ml-16">
                            <div className="col-span-2">
                                <CourseContent />
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