import React from "react"
import { Link, Redirect } from "react-router-dom"

class SignUp extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            firstname: '',
            lastname: '',
            bpassword: '',
            cpassword: '',
            toggle: false,
            didWork: false
        }
    }
    handleChange = (event) => {
        this.setState({[event.target.id]: event.target.value})
    }
    doRegister = async () => {

        let thisType = "student"

        if (this.state.toggle){
            thisType = "professor"
        }

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                firstname: this.state.firstname,
                lastname: this.state.lastname,
                password: this.state.bpassword,
                type: thisType
            })
        };
        if(this.state.firstname == "") {
            alert("Please enter a first name")
            return;
        }
        if(this.state.lastname == "") {
            alert("Please enter a last name")
            return;
        }
        if(this.state.bpassword != this.state.cpassword) {
            alert("Your passwords do not match")
            return;
        }
        if(this.state.bpassword == "" || this.state.cpassword == "") {
            alert("You must enter a password");
            return;
        }
        await fetch('/api/register', requestOptions)
            .then(response => response.text())
            .then(res => {
                alert(res)
                this.setState({didWork: true})
            });
    };
    render() {

        if (this.state.didWork == true){
            return (
                <Redirect to="/" />
            )
        }

        return (
            <div class="min-w-screen min-h-screen flex items-center justify-center px-5 py-5">
                <div class="bg-gray-100 text-gray-500 rounded-3xl shadow-xl w-1/3 overflow-hidden">
                    <div class="md:flex w-full">
                        <div class="w-full md:w-1/2 py-10 px-5 md:px-10">
                            <div class="text-center mb-10">
                                <h1 class="font-bold text-3xl text-gray-900">REGISTER</h1>
                                <p>Enter your information to register</p>
                            </div>
                            <div>
                                <div class="flex -mx-3">
                                    <div class="w-1/2 px-3 mb-5">
                                        <label for="" class="text-xs font-semibold px-1">First name</label>
                                        <div class="flex">
                                            <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center"><i class="mdi mdi-account-outline text-gray-400 text-lg"></i></div>
                                            <input type="text" id="firstname" onChange={this.handleChange.bind(this)} class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2 border-gray-200 outline-none focus:border-indigo-500" placeholder="John"></input>
                                        </div>
                                    </div>
                                    <div class="w-1/2 px-3 mb-5">
                                        <label for="" class="text-xs font-semibold px-1">Last name</label>
                                        <div class="flex">
                                            <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center"><i class="mdi mdi-account-outline text-gray-400 text-lg"></i></div>
                                            <input type="text" id="lastname" onChange={this.handleChange.bind(this)} class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2 border-gray-200 outline-none focus:border-indigo-500" placeholder="Smith"></input>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex -mx-3">
                                    <div class="w-full px-3 mb-5">
                                        <label for="" class="text-xs font-semibold px-1">Password</label>
                                        <div class="flex">
                                            <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center"><i class="mdi mdi-email-outline text-gray-400 text-lg"></i></div>
                                            <input type="password" id="bpassword" onChange={this.handleChange.bind(this)} class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2 border-gray-200 outline-none focus:border-indigo-500" placeholder="************"></input>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex -mx-3">
                                    <div class="w-full px-3 mb-12">
                                        <label for="" class="text-xs font-semibold px-1">Confirm Password</label>
                                        <div class="flex">
                                            <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center"><i class="mdi mdi-lock-outline text-gray-400 text-lg"></i></div>
                                            <input type="password" id="cpassword" onChange={this.handleChange.bind(this)} class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2 border-gray-200 outline-none focus:border-indigo-500" placeholder="************"></input>
                                        </div>
                                    </div>
                                </div>
                                <div className="-mx-3 -mt-4 mb-6">
                                    <div class="w-full h-full flex flex-col justify-center items-center">
                                        <div class="flex justify-center items-center">
                                            <span class="">
                                                Student
                                            </span>
                                        
                                            <div class="w-14 h-7 flex items-center bg-gray-300 rounded-full mx-3 px-1" onClick={()=>{this.setState({toggle: !this.state.toggle})}}>
                                                <div className={`bg-white w-5 h-5 rounded-full shadow-md transform ${this.state.toggle ? "translate-x-7" : ""}`}></div>
                                            </div>
                                            <span class="">
                                                Professor
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex -mx-3">
                                    <div class="w-full px-3 mb-5">
                                        <button class="block w-full max-w-xs mx-auto bg-yellow-500 hover:bg-yellow-600 focus:bg-yellow-700 text-white rounded-lg px-3 py-3 font-semibold " onClick={() => {this.doRegister()}}>REGISTER NOW</button>
                                    </div>
                                    
                                </div>
                                <p class="text-sm text-center text-gray-400">Already have an account yet? <Link to="/" class="text-yellow-500 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800">Sign In</Link>.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        );
    }
}
export default SignUp;
