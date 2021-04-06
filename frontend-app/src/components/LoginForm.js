import { faSchool } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react"
import { Link, Redirect } from "react-router-dom";

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            didLogin: false
        }
    }
    handleChange = (event) => {
        this.setState({[event.target.id]: event.target.value})
    }
    doLogin = async () => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                username: this.state.username,
                password: this.state.password
            })
        };
        
        await fetch('/api/login', requestOptions)
            .then(response => response.json())
            .then(res => {
                if (res.error == true) {
                    console.log('login invalid'); 
                } else {
                    console.log('login valid')
                    this.props.setU(res);
                    this.setState({didLogin: true});
                }
            });
    };
    render() {

        let tu = this.props.getUser();

        // If login is successful or already logged in
        if (this.state.didLogin || (tu.username != null)){
            return (
                <Redirect to="/dashboard" />
            )
        }

        return (
            <div class="flex items-center min-h-screen bg-white dark:bg-gray-900">
                <div class="container mx-auto">
                    <div class="mx-auto my-10 w-1/3">
                        <div className="text-center pb-20 -mt-36">
                            <FontAwesomeIcon size="7x" icon={faSchool}/>
                        </div>
                        <div class="text-center">
                            <h1 class="my-3 text-3xl font-semibold text-gray-700 dark:text-gray-200">Sign in</h1>
                            <p class="text-gray-500 dark:text-gray-400">Sign in to access your education portal</p>
                        </div>
                        <div class="m-7">
                            <form action="">
                                <div class="mb-6">
                                    <label for="username" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">Username</label>
                                    <input type="username" name="username" id="username" placeholder="username" onChange={this.handleChange.bind(this)} class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                                </div>
                                <div class="mb-6">
                                    <div class="flex justify-between mb-2">
                                        <label for="password" class="text-sm text-gray-600 dark:text-gray-400">Password</label>
                                        <a href="#!" class="text-sm text-gray-400 focus:outline-none focus:text-indigo-500 hover:text-indigo-500 dark:hover:text-indigo-300">Forgot password?</a>
                                    </div>
                                    <input type="password" name="password" id="password" onChange={this.handleChange.bind(this)} placeholder="Your Password" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                                </div>
                                <div class="mb-6">
                                    <button type="button" class="w-full px-3 py-4 text-white bg-yellow-500 rounded-md focus:bg-yellow-600 focus:outline-none" onClick={this.doLogin}>Sign in</button>
                                </div>
                                <p class="text-sm text-center text-gray-400">Don&#x27;t have an account yet? <Link to="/signup" class="text-yellow-500 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800">Sign up</Link>.</p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default LoginForm;