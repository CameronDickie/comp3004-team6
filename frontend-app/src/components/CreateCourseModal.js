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
            num_of_students: "1",
            professor_name: "",
            professor_id: "",
            start_time: "8:00",
            class_duration: "1",
            daysChoosen: {
                'monday': false,
                'tuesday': false,
                'wednesday': false,
                'thursday': false,
                'friday': false
            },
            prerequisites_choosen:[],
            how_many_days_choose: 0,
        }
    }

    handleChange = (event) => {
        this.setState({[event.target.id]: String(event.target.value)})
        console.log(event.target.id  + ": " + event.target.value)
    }

    doSubmit = async () => {
        let pname = this.state.professor_name;
        let pid = "";

        if(pname == "") {
            pname = this.props.professors[0].name;
            pid = this.props.professors[0].id;
        }

        for(let prof in this.props.professors) {
            if(this.props.professors[prof].name == pname) {
                pid = this.props.professors[prof].id;
                break;
            }
        }
        
        // this.setState({professor_name: this.props.professors[0].name, professor_id: this.props.professors[0].id})
        //checking if a course code has been provided
        if(this.state.course_code == "") {
            alert("You must provide a course code for this course");
            return;
        }
        //checking if a course name has been provided
        if(this.state.course_name == "") {
            alert("You must provide a name for this course")
            return;
        }
        let _choose_days = []

        for (let d in this.state.daysChoosen){
            if (this.state.daysChoosen[d]){
                _choose_days.push(d);                }
            }
        //checking if there are days assigned to the course
        if(_choose_days.length == 0) {
            alert("You must provide days this course takes place on.");
            return;
        }
        if(this.state.course_code.length != 9) {
            alert("Format must be 9 characters long (e.g. COMP3008A)")
            return;
        }
        let reqs = this.state.prerequisites_choosen;
        for(let i in reqs) {
            reqs[i] = reqs[i].substring(0, reqs[i].length-1);
        }
        //ensuring that no duplicate courses are provided
        for(let i = 0; i < reqs.length; i++) {
            for(let j = i+1; j < reqs.length; j++) {
                if(reqs[i] == reqs[j]) {
                    //remove all duplicates of reqs[i]
                    reqs = reqs.splice(j);
                    j--;
                }
            }
        }

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                courseCode: this.state.course_code,
                courseName: this.state.course_name,
                maxStudents: this.state.num_of_students,
                professorID: pid,
                prerequisites: reqs,
                professorName: pname,
                days: _choose_days,
                startTime: this.state.start_time,
                classDuration: this.state.class_duration,
            })
        };
        console.log("professor id:" + pid);
        await fetch('/api/create-course', requestOptions)
            .then(response => response.text())
            .then(res => {
                alert(res)
                this.props.hide()
            });
        
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

    toggleDayChoosen = (day) => {
        let new_days = {};

        let num_days = this.state.how_many_days_choose;
        
        for (let d in this.state.daysChoosen){
            if ((d == day) && this.state.daysChoosen[d]){
                new_days[d] = false;
                num_days--;
            } else if (d == day){
                new_days[d] = true;
                num_days++;
            } else if(this.state.daysChoosen[d]){
                new_days[d] = true;
            }else new_days[d] = false;
        }

        if (num_days > 2){
            alert("Unselect a day to select a different day!")
        } else this.setState({daysChoosen: new_days, how_many_days_choose: num_days})
    }

    buildDaysRow = () => {
        let days = []

        for (let day in this.state.daysChoosen){
            let hasBeenChoosen = this.state.daysChoosen[day]

            days.push(
                <button className={`col-sm p-2 border-2 rounded-full w-24 ${hasBeenChoosen ? "border-gray-900" : ""}`} onClick={()=>{this.toggleDayChoosen(day)}}>
                    <div className={`w-full text-center ${hasBeenChoosen ? "text-gray-900" : "text-gray-300"}`}>{day}</div>
                </button>
            )
        }

        return days;
    }

    buildTimeSlotOptions = () => {
        let pdl = [];

        let hours = ['8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22']
        let minutes = ['00', '15', '30', '45']

        for (let h in hours){
            for (let m in minutes){
                pdl.push(<option>{hours[h] + ":" + minutes[m]}</option>)
            }
        }

        return pdl;
    }

    buildDurationOptions = () => {
        let pdl = [];

        let hours = [1, 2, 3, 4, 5, 6]

        for (let h in hours){
            pdl.push(<option>{hours[h]}</option>)
        }

        return pdl;
    }

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

                            <div className="grid grid-cols-4">

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Max Students</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="num_of_students" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildMaxStudentOptions()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Professor</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="professor_name" name="professor_name" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildProfessorDropDown()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Time Slot (24h)</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="start_time" name="start_time" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildTimeSlotOptions()}
                                        </select>
                                    </div>
                                </div>


                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Duration (h)</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="class_duration" name="class_duration" onChange={this.handleChange.bind(this)} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildDurationOptions()}
                                        </select>
                                    </div>
                                </div>

                            </div>

                            <div class="mb-6">
                                <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">CHOOSE (2) DAYS</label>
                                <div class="container">
                                    <div class="row space-x-1">
                                        {this.buildDaysRow()}
                                    </div>
                                </div>
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