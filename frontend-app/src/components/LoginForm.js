import React from "react"

class LoginForm extends React.Component {

    render() {
        return (
            <div className="bg-gray-300 rounded-lg my-2 p-4">
                <h2 className="underline mx-2 uppercase text-lg">Log in with your account</h2>
                <input placeholder="username" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input type="password" placeholder="password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <button className="mx-2 my-1 bg-gray-400 px-2 py-1 outline-none rounded-full"> Log In</button>
            </div>
        );
    }
}
export default LoginForm;