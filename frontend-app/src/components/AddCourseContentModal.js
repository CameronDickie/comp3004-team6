import { faSave } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class AddCourseContentModal extends Component{
    constructor(props) {
        super(props);

        this.state = {
            name: "",
            type: "section",
            year: "2021",
            month: "01",
            day: "01",
            hour: "01",
            minutes: "00",

            file: null,
            base64URL: "",
            oldName: ""
        }
    }

    handleChange = (event) => {
        this.setState({[event.target.id]: String(event.target.value)})          
    }

    doSubmit = async () => {
        let concat_date = this.state.year + "-" + this.state.month + "-" + this.state.day + "-" + this.state.hour + "-" + this.state.minutes

        let na = this.state.name
        if (this.state.type == "file") na = this.state.file.name

        let api_path = ((this.state.type != "deliverable") ? "/api/add-content" : "/api/add-deliverable")
        if (this.state.type == "file") api_path = "/api/add-document"
        if (this.props.isSubmission) api_path = "/api/submit-deliverable"

        let type = this.state.type

        if (type == "file"){
            let extension = String(this.state.file.name).split('.')[1].toUpperCase()
            type = extension

            if (extension != "PDF" && extension != "DOCX" && extension != "PPTX"){
                alert("The file you choose is not supported. Files supported are (pdf, pptx, docx).")
                return;
            }
        }

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                name: na,
                path: this.props.cPath,
                type: type,
                userID: String((this.props.getUser().type == "Student") ? this.props.getUser().studentID : this.props.getUser().professorID),
                userType: this.props.getUser().type,
                courseCode: this.props.courseCode,
                visible: "true",
                deadline: concat_date,
                bytes: this.state.base64URL,
                oldName: this.state.oldName
            })
        };
        
        await fetch(api_path, requestOptions)
            .then(response => response.text())
            .then(res => {
                console.log(res)
                this.props.clearCurrentContent()
                this.props.hide()
            });
    };

    getBase64 = (file) => {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => {
            let encoded = reader.result.toString().replace(/^data:(.*,)?/, '');
            if ((encoded.length % 4) > 0) {
              encoded += '='.repeat(4 - (encoded.length % 4));
            }
            resolve(encoded);
          };
          reader.onerror = error => reject(error);
        });
      }

    handleFileInputChange = e => {
        let { file } = this.state;
    
        file = e.target.files[0];
    
        this.getBase64(file)
          .then(result => {
            file["base64"] = result;

            console.log("File Is", file);
            console.log(file.base64)

            this.setState({
              base64URL: result,
              file
            });
          })
          .catch(err => {
            console.log(err);
          });
    
        this.setState({
          file: e.target.files[0]
        });
    };

    buildItemTypeOptions = () => {
        let pdl = [];

        let types = ["section", "lecture", "deliverable", "file"]

        if (this.props.isSubmission == true) {
            types = ["file"]
        }

        for (let t in types){
            pdl.push(<option>{types[t]}</option>)
        }

        return pdl;
    }

    buildYearDropDown = () => {
        let pdl = []

        for (let i = 2021; i < 2034; i++){
            pdl.push(<option>{i}</option>)
        }

        return pdl;
    }

    buildMonthDropDown = () => {
        let pdl = []

        let months = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"]

        for (let m in months){
            pdl.push(<option>{months[m]}</option>)
        }

        return pdl;
    }

    buildDayDropDown = () => {
        let pdl = []

        let days = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                            "25", "26", "27", "28", "29", "30", "31"]

        for (let d in days){
            pdl.push(<option>{days[d]}</option>)
        }

        return pdl;
    }

    buildHourDropDown = () => {
        let pdl = []

        let hours = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]

        for (let h in hours){
            pdl.push(<option>{hours[h]}</option>)
        }

        return pdl;
    }

    buildMinutesDropDown = () => {
        let pdl = []

        for (let i = 0; i < 60; i++){
            if (i < 10){
                pdl.push(<option>0{i}</option>)
            } else pdl.push(<option>{i}</option>)
        }

        return pdl;
    }


    render(){

        if (this.props.isSubmission && this.state.type != "file" && (this.props.currentContent.data == null)){
            this.setState({type: "file"})
        }

        if (this.props.currentContent.data != null && this.props.currentContent.isUpdate){

            if (this.props.currentContent.deadline){
                let dateList = this.props.currentContent.deadline.split("-");

                this.setState({
                    name: this.props.currentContent.data.name,
                    type: this.props.currentContent.data.type,
                    year: dateList[0],
                    month: dateList[1],
                    day: dateList[2],
                    hour: dateList[3],
                    minutes: dateList[4],
                    oldName: this.props.currentContent.data.name
                })

            } else {
                this.setState({
                    name: this.props.currentContent.data.name,
                    type: this.props.currentContent.data.type,
                    oldName: this.props.currentContent.data.name
                })
            }

            this.props.setIsUpdate(false)
        }
        
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-7/12 h-7/12 border border-gray-300 p-4">
                    <div class="flex flex-col items-start p-6">
                        <div class="flex items-center w-full">
                            <div class="text-gray-900 font-bold text-3xl italic uppercase">Create a Content Item</div>
                            <svg onClick={() => this.props.hide("/")} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                            </svg>
                        </div>
                        <hr></hr>
                        <div class="p-2">CONTEXT: <span>{this.props.cPath}</span></div>


                        <div class="px-4 py-5 bg-white space-y-6 sm:p-6 w-full pb-8">
                                    
                            <div className={`mb-6 ${(this.state.type == "file" ? "hidden" : "")}`}>
                                <label for="name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">NAME</label>
                                <input onChange={this.handleChange.bind(this)} type="name" name="name" id="name" value={this.state.name} placeholder="ex. Lecture 1" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-green-200 focus:border-green-500 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                            </div>

                            <div className="grid grid-cols-4">
                    
                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">Type</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="type" onChange={this.handleChange.bind(this)} value={this.state.type} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildItemTypeOptions()}
                                        </select>
                                    </div>
                                </div>

                            </div>

                            <div className={`text-xl font-bold p-2 ${this.state.type == "file" ? "" : "hidden"}`}>
                                <input type="file" name="file" onChange={this.handleFileInputChange} />
                            </div>

                            <div className={`text-xl font-bold p-2 ${(this.state.type == "deliverable") ? "" : "hidden"}`}>Due Date: </div>
                            <div className={`grid grid-cols-5 ${(this.state.type == "deliverable") ? "" : "hidden"}`}>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">YEAR</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="year" onChange={this.handleChange.bind(this)} value={this.state.year} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildYearDropDown()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">MONTH</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="month" name="month" onChange={this.handleChange.bind(this)} value={this.state.month} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildMonthDropDown()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">DAY</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="day" name="day" onChange={this.handleChange.bind(this)} value={this.state.day} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildDayDropDown()}
                                        </select>
                                    </div>
                                </div>


                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">HOUR</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="hour" name="hour" onChange={this.handleChange.bind(this)} value={this.state.hour} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildHourDropDown()}
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-1">
                                    <label class="block pt-2 pb-2 text-sm text-gray-600 dark:text-gray-400 uppercase">MINUTES</label>
                                    <div class="relative inline-flex">
                                        <svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232"><path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/></svg>
                                        <select id="minutes" name="minutes" onChange={this.handleChange.bind(this)} value={this.state.minutes} class="text-center border border-gray-300 rounded-lg text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
                                            {this.buildMinutesDropDown()}
                                        </select>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <div class="px-4 py-3 w-full text-right sm:px-6 border-t">
                            <div className="pt-3">
                                <button onClick={() => {this.doSubmit()}} type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-md font-medium rounded-md border-green-500 hover:shadow-md focus:outline-none focus:ring-offset-2 focus:ring-indigo-500">
                                Save Course Item <span>
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

export default AddCourseContentModal;