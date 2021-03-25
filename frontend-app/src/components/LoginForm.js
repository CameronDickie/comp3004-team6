import React from "react"

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
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
            .then(response => response.text())
            .then(res => {
                if(res == "true") console.log('login valid')
                else if(res == "false") console.log('login invalid');
                else console.log('unexpected error');
            });
    };
    render() {
        return (
            <div className="bg-gray-300 rounded-lg my-2 p-4">
                <h2 className="underline mx-2 uppercase text-lg">Log in with your account</h2>
                <input id="username" placeholder="username" onChange={this.handleChange.bind(this)} className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="password" type="password" onChange={this.handleChange.bind(this)} placeholder="password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <button className="mx-2 my-1 bg-gray-400 px-2 py-1 outline-none rounded-full" onClick={this.doLogin}> Log In</button>
            </div>
        );
    }
}
export default LoginForm;