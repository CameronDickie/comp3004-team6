import { faFilePdf, faFilePowerpoint } from "@fortawesome/free-regular-svg-icons";
import { faChevronCircleLeft} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";
import CourseContent from "./CourseContent";
import UploadSubmissionModal from "./UploadSubmissionModal";

class FullScreenCourseModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            fileUpload: false,
            data: {}
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
                                <span className="font-semibold pr-10">{this.props.course.name}<span>  </span> -</span>
                                <span className="font-semibold text-gray-500">{this.props.name}</span>
                            </div>
                        </div>

                        <div className="grid grid-cols-4 ml-16">
                            <div className="col-span-3">
                                <CourseContent courseCode={this.props.course.code} course={this.props.course} />
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