import { faAngleDoubleRight, faChevronCircleLeft, faFile, faGraduationCap, faIdCard, faPlus, faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";
import UploadSubmissionModal from "./UploadSubmissionModal";

class FullScreenCourseModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            fileUpload: false
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
                                <div className="ml-24 pt-14 text-3xl font-bold uppercase mb-6">Course Content:</div>
                                <div class="max-w-3xl pt-10 ml-24 overflow-auto h-10/12 border-t">
                                <div class="flow-root">
                                    <ul class="-mb-8">
                                    
                                    <li>
                                        <div class="relative pb-8">
                                        <span class="absolute top-5 left-5 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true"></span>
                                        <div class="relative flex items-start space-x-3">
                                            <div>
                                            <div class="relative px-1">
                                                <div class="h-10 w-10 bg-black rounded-full ring-8 ring-white flex items-center justify-center">
                                                    <FontAwesomeIcon className="text-white" icon={faAngleDoubleRight}/>
                                                </div>
                                            </div>
                                            </div>
                                            <div class="min-w-0 flex-1 py-0">
                                            <div class="text-lg text-gray-500">
                                                <div>
                                                <a href="#" class="font-medium text-gray-900 mr-2">Welcome Class!!</a>

                                                <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                                    <div class="absolute flex-shrink-0 flex items-center justify-center">
                                                    <span class="h-1.5 w-1.5 rounded-full bg-yellow-500" aria-hidden="true"></span>
                                                    </div>
                                                    <div class="ml-3.5 font-medium text-gray-900">Post</div>
                                                </a>
                                                </div>
                                                <span class="whitespace-nowrap text-sm">December 25th, 2019</span>
                                            </div>
                                            <div class="mt-2 text-gray-700">
                                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Tincidunt nunc ipsum tempor purus vitae id. Morbi in vestibulum nec varius. Et diam cursus quis sed purus nam. Scelerisque amet elit non sit ut tincidunt condimentum. Nisl ultrices eu venenatis diam.</p>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                    </li><li>
                                        <div class="relative pb-8">
                                        <span class="absolute top-5 left-5 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true"></span>
                                        <div class="relative flex items-start space-x-3">
                                            <div>
                                            <div class="relative px-1">
                                                <div class="h-10 w-10 bg-black rounded-full ring-8 ring-white flex items-center justify-center">
                                                    <FontAwesomeIcon className="text-white" icon={faAngleDoubleRight}/>
                                                </div>
                                            </div>
                                            </div>
                                            <div class="min-w-0 flex-1 py-0">
                                            <div class="text-lg text-gray-500">
                                                <div>
                                                <a href="#" class="font-medium text-gray-900 mr-2">Week 1 - LECTURE #1</a>

                                                <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                                    <div class="absolute flex-shrink-0 flex items-center justify-center">
                                                    <span class="h-1.5 w-1.5 rounded-full bg-green-500" aria-hidden="true"></span>
                                                    </div>
                                                    <div class="ml-3.5 font-medium text-gray-900">Lecture</div>
                                                </a>
                                                </div>
                                                <span class="whitespace-nowrap text-sm">December 28th, 2019</span>
                                            </div>
                                            <div class="mt-2 text-gray-700">
                                                <p>Lecture content for the week</p>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                    </li>

                                    <li>
                                        <div class="relative pb-8">
                                        <span class="absolute top-5 left-5 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true"></span>
                                        <div class="relative flex items-start space-x-3">
                                            <div>
                                            <div class="relative px-1">
                                                <div class="h-10 w-10 bg-black rounded-full ring-8 ring-white flex items-center justify-center">
                                                    <FontAwesomeIcon className="text-white" icon={faAngleDoubleRight}/>
                                                </div>
                                            </div>
                                            </div>
                                            <div class="min-w-0 flex-1 py-0">
                                            <div class="text-lg text-gray-500">
                                                <div>
                                                <a href="#" class="font-medium text-gray-900 mr-2">Week 1 - Zoom Lecture #1</a>

                                                <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                                    <div class="absolute flex-shrink-0 flex items-center justify-center">
                                                    <span class="h-1.5 w-1.5 rounded-full bg-red-500" aria-hidden="true"></span>
                                                    </div>
                                                    <div class="ml-3.5 font-medium text-gray-900">Live Lecture</div>
                                                </a>
                                                </div>
                                                <span class="whitespace-nowrap text-sm">January 2nd, 2020</span>
                                            </div>
                                            <div class="mt-2 text-gray-700">
                                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Tincidunt nunc ipsum tempor purus vitae id. Morbi in vestibulum nec varius. Et diam cursus quis sed purus nam. Scelerisque amet elit non sit ut tincidunt condimentum. Nisl ultrices eu venenatis diam.</p>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="relative pb-8">
                                        <span class="absolute top-5 left-5 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true"></span>
                                        <div class="relative flex items-start space-x-3">
                                            <div>
                                            <div class="relative px-1">
                                                <div class="h-10 w-10 bg-black rounded-full ring-8 ring-white flex items-center justify-center">
                                                    <FontAwesomeIcon className="text-white" icon={faAngleDoubleRight}/>
                                                </div>
                                            </div>
                                            </div>
                                            <div class="min-w-0 flex-1 py-0">
                                            <div class="text-lg text-gray-500">
                                                <div>
                                                <a href="#" class="font-medium text-gray-900 mr-2">Week 2 - POWERPOINT</a>

                                                <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                                    <div class="absolute flex-shrink-0 flex items-center justify-center">
                                                    <span class="h-1.5 w-1.5 rounded-full bg-red-500" aria-hidden="true"></span>
                                                    </div>
                                                    <div class="ml-3.5 font-medium text-gray-900">PPXT</div>
                                                </a>
                                                </div>
                                                <span class="whitespace-nowrap text-sm">January 2nd, 2020</span>
                                            </div>
                                            <div class="mt-2 text-gray-700">
                                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Tincidunt nunc ipsum tempor purus vitae id. Morbi in vestibulum nec varius. Et diam cursus quis sed purus nam. Scelerisque amet elit non sit ut tincidunt condimentum. Nisl ultrices eu venenatis diam.</p>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                    </li>
                                    </ul>
                                </div>
                                </div>
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
                                            <td class="py-4 px-6 border-b border-grey-light underline">Pre-course Quiz</td>
                                            <td class="py-4 px-6 border-b border-grey-light">

                                                <button className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Attempt
                                                <FontAwesomeIcon className="ml-2 text-purple-500" size="lg" icon={faIdCard}/></button>

                                            </td>
                                            <td class="py-4 px-6 border-b border-grey-light font-mono underline">05/08/19</td>
                                        </tr>
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