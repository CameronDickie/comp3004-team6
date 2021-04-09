import { faSave, faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class CreateCourseModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            cool: true,
            course_code: "",
            course_name: "",
            num_of_students: 1,
            professor_name: "",
            professor_id: ""
        }
    }

    handleChange = (event) => {
        this.setState({[event.target.id]: String(event.target.value)})
        console.log(event.target.id  + ": " + event.target.value)
    }

    doSubmit = async () => {
        if(this.state.professor_name == "") {
            this.setState({professor_name: this.props.professors[0].name, professor_id: this.props.professors[0].id}, () => {
                this.doSubmit();
            })
        } else {
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'text/html' },
                body: JSON.stringify({
                    courseCode: this.state.course_code,
                    courseName: this.state.course_name,
                    maxStudents: this.state.num_of_students,
                    professorID: this.state.professor_id,
                    prerequisites: [],
                    professorName: this.state.professor_name
                })
            };
            console.log(requestOptions.body);
            await fetch('/api/create-course', requestOptions)
                .then(response => response.text())
                .then(res => {
                    console.log(res)
                });
        }
        
    };

    buildMaxStudentOptions = () => {
        let msol = []

        for (let i = 1; i < 301; i++){
            msol.push(
                <option>{i}</option>
            )
        }

        return msol;
    }

    buildProfessorDropDown = () => {
        let pdl = [];
        for(let i in this.props.professors) {
            pdl.push(<option>{this.props.professors[i].name}</option>)
        }
        return pdl;
    }
    render(){
        //check to see if default professor_name and professor_id has changed
        
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-5/12 h-7/12 border border-gray-300 p-4">
                    <div class="flex flex-col items-start p-6">
                        <div class="flex items-center w-full">
                            <div class="text-gray-900 font-bold text-3xl italic uppercase">Create a Course</div>
                            <svg onClick={this.props.hide} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                            </svg>
                        </div>
                        <hr></hr>
                        <div class="p-2">Create a NEW Course for the University</div>


                        <div class="px-4 py-5 bg-white space-y-6 sm:p-6 w-full pb-8">
                                    
                            <div class="mb-6">
                                <label for="username" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">COURSE CODE</label>
                                <input onChange={this.handleChange.bind(this)} type="username" name="course_code" id="course_code" placeholder="ex. COMP3004" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-green-200 focus:border-green-500 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>

                            <div class="mb-6">
                                <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">COURSE NAME</label>
                                <input onChange={this.handleChange.bind(this)} type="username" name="course_name" id="course_name" placeholder="ex. Object Oriented Software Engineering" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-yellow-100 focus:border-yellow-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>

                            <div className="grid grid-cols-2">

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Max # of Students</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="num_of_students" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildMaxStudentOptions()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Assigned Professor</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="professor_name" name="professor_name" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildProfessorDropDown()}
                                        </select>
                                    </div>
                                </div>

                            </div>

                        </div>

                        <div class="px-4 py-3 w-full text-right sm:px-6 border-t">
                            <div className="pt-3">
                                <button onClick={() => {this.doSubmit()}} type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-md font-medium rounded-md border-green-500 hover:shadow-md focus:outline-none focus:ring-offset-2 focus:ring-indigo-500">
                                Save Course <span>
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

export default CreateCourseModal;