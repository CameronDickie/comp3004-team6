import React from "react"

class SignUp extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            firstname: '',
            lastname: '',
            bpassword: '',
            cpassword: ''
        }
    }
    handleChange = (event) => {
        this.setState({[event.target.id]: event.target.value})
    }
    doRegister = async () => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                firstname: this.state.firstname,
                lastname: this.state.lastname,
                password: this.state.bpassword
            })
        };
        
        await fetch('/api/register', requestOptions)
            .then(response => response.text())
            .then(res => {
                console.log(res)
            });
    };
    render() {
        return (
            <div className="bg-gray-300 rounded-lg my-4 p-4">
                <h2 className="underline mx-2 uppercase text-lg">Request to register in the system</h2>
                <input id="firstname" onChange={this.handleChange.bind(this)} placeholder="First Name" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="lastname" onChange={this.handleChange.bind(this)} placeholder="Last Name" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="bpassword" onChange={this.handleChange.bind(this)} type="password" placeholder="password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="cpassword" onChange={this.handleChange.bind(this)} type="password" placeholder="confirm password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <button className="mx-2 my-1 bg-gray-400 px-2 py-1 outline-none rounded-full" onClick={() => {this.doRegister()}}>Apply to be a student!</button>
            </div>
        );
    }
}
export default SignUp;