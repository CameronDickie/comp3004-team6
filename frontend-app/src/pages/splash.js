import { faSchool } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import React from "react"
import { Link } from "react-router-dom"

import LoginForm from "../components/LoginForm.js"
import SignUp from "../components/SignUp.js"

class Splash extends React.Component {
    render() {
        return (
            <div className="w-full h-full">
                <div className="mx-auto -mt-10">
                    <LoginForm/>
                    {/* <SignUp /> */}
                </div>
            </div>
        )
    }
}

export default Splash;