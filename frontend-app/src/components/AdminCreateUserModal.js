import { faSave, faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class CreateCourseModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            firstname: "",
            lastname: "",
            password: "",
            prerequisites_choosen:[],
        }
    }

    handleChange = (event) => {
        this.setState({[event.target.id]: String(event.target.value)})
        console.log(event.target.id  + ": " + event.target.value)
    }

    doSubmit = async () => {
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'text/html' },
                body: JSON.stringify({
                    firstname: this.state.firstname,
                    lastname: this.state.lastname,
                    password: this.state.password,
                    prerequisites: this.state.prerequisites_choosen
                })
            };
            await fetch('/api/register-user-prerequisites', requestOptions)
                .then(response => response.json())
                .then(res => {
                    console.log(res)
                    this.props.hide()
                });
    };


    buildCourseOptions = () => {
        let c = []

        let dummyCourses = [
            {
                code: "COMP3004"
            },
            {
                code: "COMP3006"
            },
            {
                code: "COMP2003"
            }
        ]

        for (let n in this.props.courses){

            let course = this.props.courses[n];
            let hasBeenChoosen = false;

            for (let cc in this.state.prerequisites_choosen){
                if (this.state.prerequisites_choosen[cc] == course.code){
                    hasBeenChoosen = true;
                }
            }

            c.push(
                <button className={`col-sm p-2 border-2 rounded-full w-28 ${hasBeenChoosen ? "border-gray-900" : ""}`} onClick={()=>{this.toggleCourseChoosen(course.code)}}>
                    <div className={`w-full text-center ${hasBeenChoosen ? "text-gray-900" : "text-gray-300"}`}>{course.code}</div>
                </button>
            )
        }

        return c;
    }


    toggleCourseChoosen = (code) => {
        let new_codes = this.state.prerequisites_choosen;

        if (new_codes.includes(code)){
            new_codes = new_codes.filter(item => item !== code)
        } else new_codes.push(code)

        this.setState({prerequisites_choosen: new_codes})
    }

    render(){
        //check to see if default professor_name and professor_id has changed
        
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-7/12 h-7/12 border border-gray-300 p-4">
                    <div class="flex flex-col items-start p-6">
                        <div class="flex items-center w-full">
                            <div class="text-gray-900 font-bold text-3xl italic uppercase">Create a User</div>
                            <svg onClick={this.props.hide} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                            </svg>
                        </div>
                        <hr></hr>
                        <div class="p-2">Create a NEW Student in the University</div>


                        <div class="px-4 py-5 bg-white space-y-6 sm:p-6 w-full pb-8">
                                    
                            <div class="mb-6">
                                <label for="username" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">FIRST NAME</label>
                                <input onChange={this.handleChange.bind(this)} type="username" name="firstname" id="firstname" placeholder="ex. John" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-green-200 focus:border-green-500 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>

                            <div class="mb-6">
                                <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">LAST NAME</label>
                                <input onChange={this.handleChange.bind(this)} type="username" name="lastname" id="lastname" placeholder="ex. Smith" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-yellow-100 focus:border-yellow-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>

                            <div class="mb-6">
                                <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">PASSWORD</label>
                                <input onChange={this.handleChange.bind(this)} type="password" name="password" id="password"  class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-yellow-100 focus:border-yellow-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>


                            <div class="mb-6">
                                <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">CHOOSE PREREQUISITES</label>
                                <div class="container">
                                    <div class="row space-x-1">
                                        {this.buildCourseOptions()}
                                    </div>
                                </div>
                                {/* <div className="grid grid-cols-5 gap-2 items-center">
                                    {this.buildCourseOptions()}
                                </div> */}
                            </div>


                        </div>

                        <div class="px-4 py-3 w-full text-right sm:px-6 border-t">
                            <div className="pt-3">
                                <button onClick={() => {this.doSubmit()}} type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-md font-medium rounded-md border-green-500 hover:shadow-md focus:outline-none focus:ring-offset-2 focus:ring-indigo-500">
                                Save User <span>
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