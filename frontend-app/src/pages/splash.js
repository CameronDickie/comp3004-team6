import React from "react"
import { Link } from "react-router-dom"

class Splash extends React.Component {
    render() {
        return (
            <div className="w-full h-full flex">
                <div className="mt-20 mx-auto">
                    <h1 className="text-2xl text-gray-600">USER LOGIN</h1>
                    <p>USER ID: {this.props.user.userID}</p>
                    <div className="p-2">
                        <button className="p-2 border-2 font-medium text-lg text-green-500" onClick={this.props.doLogin}>Login</button>
                        <div className="pt-2">
                            <button className="p-2 border-2 font-medium text-lg text-red-500" 
                            onClick={this.props.clearStorage}>Logout / Clear Storage</button>
                        </div>
                    </div>
                </div>
                <Link to="/test" >
                    <button className="p-2 bg-blue-500">
                        Go to test
                    </button>
                </Link>
            </div>
        )
    }
}

export default Splash;