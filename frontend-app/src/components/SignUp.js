import React from "react"

class SignUp extends React.Component {
    
    doRegister = async () => {
        let ffield = document.getElementById("fname");
        let lfield = document.getElementById("lname");
        let bpfield = document.getElementById("bpword");
        let cpfield = document.getElementById("cpword");
        if(bpfield.value !== cpfield.value) {
            alert("The two passwords entered do not match");
            return;
        }
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                firstname: ffield.value,
                lastname: lfield.value,
                password: bpfield.value
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
                <input id="fname" placeholder="First Name" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="lname" placeholder="Last Name" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="bpword" type="password" placeholder="password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <input id="cpword" type="password" placeholder="confirm password" className="w-full px-4 text-gray-300 focus:text-gray-900 border border-blue-200 outline-none focus:border-blue-500 rounded-full p-2 my-3" />
                <button className="mx-2 my-1 bg-gray-400 px-2 py-1 outline-none rounded-full" onClick={() => {this.doRegister()}}>Apply to be a student!</button>
            </div>
        );
    }
}
export default SignUp;