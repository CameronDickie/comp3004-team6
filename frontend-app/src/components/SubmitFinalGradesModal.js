import { faSave, faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class SubmitFinalGradeModal extends Component{
    constructor(props) {
        super(props);

        this.state = {
            grades: {}
        }
    }

    handleChange = (event) => {
        let newgrades = this.state.grades;
        newgrades[event.target.id] = String(event.target.value);
        this.setState({grades: newgrades})          
    }

    doSubmit = async () => {
        let stuIDS = Object.keys(this.state.grades);
        let allGrades = Object.values(this.state.grades);
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                studentIDs: stuIDS,
                courseCode: this.props.courseCode,
                grades: allGrades
            })
        };
        
        await fetch("/api/submit-final-grade", requestOptions)
            .then(response => response.json())
            .then(res => {
                if(res.error) {
                    alert(res.error)
                    return;
                }
                alert(res.finished);
                this.props.hide()
            });
    };

    buildGradeDropDown = () => {
        let pdl = []

        for (let grade = 0; grade < 100; grade++){
            pdl.push(<option>{grade}</option>)
        }

        return pdl;
    }

    listAllStudents = () => {
        let html = [];
        let ids = Object.keys(this.props.students);
        let names = Object.values(this.props.students);

        for(let i in ids) {
            html.push (
                <div className={`grid grid-cols-5`}>

                                <div className="col-span-1">
                                    <div>{ids[i]}</div>
                                    <div>{names[i]}</div>
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Grade</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id={ids[i]} onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildGradeDropDown()}
                                        </select>
                                    </div>
                                </div>

                            </div>
            )
        }
        return html
    }

    render(){
        
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-7/12 h-7/12 border border-gray-300 p-4">
                    <div class="flex flex-col items-start p-6">
                        <div class="flex items-center w-full">
                            <div class="text-gray-900 font-bold text-3xl italic uppercase">Add Grade</div>
                            <svg onClick={() => this.props.hide()} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                            </svg>
                        </div>
                        <hr></hr>
                        <div class="p-2">STUDENTS: <span>{this.props.cPath}</span></div>

                        <div class="px-4 py-5 bg-white space-y-6 sm:p-6 w-full pb-8">    
                            {this.listAllStudents()}

                        </div>

                        <div class="px-4 py-3 w-full text-right sm:px-6 border-t">
                            <div className="pt-3">
                                <button onClick={() => {this.doSubmit()}} type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-md font-medium rounded-md border-green-500 hover:shadow-md focus:outline-none focus:ring-offset-2 focus:ring-indigo-500">
                                Submit Grades <span>
                                <FontAwesomeIcon className="ml-2" size="lg" icon={faSave}/>
                                </span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default SubmitFinalGradeModal;