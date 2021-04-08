import { faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class UploadSubmissionModal extends Component{

    constructor(props) {
        super(props);
        this.state = {
            selectedNames: []
        }
    }

    addFilesToList = (val) => {
        let nL = this.state.selectedNames;

        let cleanedName = String(val).split("\\")[2];
        nL.push(cleanedName);

        this.setState({selectedNames: nL})
    }

    removeFileFromList = (n) => {
        let nL = this.state.selectedNames
        nL.splice(n, 1);
        this.setState({selectedNames: nL})
    }

    renderFileNames = () => {
        let files = []

        for (let n in this.state.selectedNames){
            files.push(<button className="bg-white border-gray-500 ml-1 px-2 py-2 border rounded-full font-semibold shadow-sm text-sm">{this.state.selectedNames[n]}<FontAwesomeIcon 
            onClick={() => this.removeFileFromList(n)} className="ml-2 text-gray-900" size="lg" icon={faWindowClose}/></button>)
        }

        return files;
    }

    render(){
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-1/2 h-1/2 border border-gray-300">
                    <div class="flex flex-col items-start p-6">
                    <div class="flex items-center w-full">
                        <div class="text-gray-900 font-bold text-2xl uppercase">Upload File(s)</div>
                        <svg onClick={this.props.toggleIt} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                            <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                        </svg>
                    </div>
                    <hr></hr>
                    <div class="">Select file(s) for submission. Choose one at a time.</div>

                    <div className="pt-6 pl-4">
                        <div class="border border-dashed border-gray-500 relative">
                            <input onChange={(e) => this.addFilesToList(e.target.value)} type="file" multiple class="cursor-pointer relative block opacity-0 w-full h-full p-20 z-50"></input>
                            <div class="text-center p-14 absolute top-0 right-0 left-0 m-auto">
                                <h4>
                                    Drop a file anywhere to upload
                                    <br/>or
                                </h4>
                                <p class="">Select a File</p>
                            </div>
                        </div>
                    </div>

                    <div className="pl-4 pt-4 p-2">
                        <h2 className="font-mono font-bold">Selected Files:</h2>
                        <div className="pt-1">
                            <span>
                                {this.renderFileNames()}
                            </span>
                        </div>
                    </div>

                    <div class="ml-auto px-2">
                        <button onClick={this.toggleFileSubmissionModal} className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Submit<FontAwesomeIcon className="ml-2 text-green-500" size="lg" icon={faUpload}/></button>
                    </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default UploadSubmissionModal;