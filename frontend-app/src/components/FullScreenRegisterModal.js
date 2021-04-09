import { faChevronCircleLeft, faPlus, faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";

class FullScreenRegisterModal extends Component{
    constructor(props) {
        super(props);
        
    }

    

    
    render(){

        let cards = []

        for (let i in this.props.courses){
            let c = this.props.courses[i]

            cards.push(
               <div className="p-2">
                    <div className="p-3 rounded-md h-40 w-80 bg-gray-50 border-2 hover:border-green-500 flex flex-wrap content-between">
                    <div className="w-full">
                        <div className="p-1 pl-2 font-semibold text-xl">{c.code}</div>
                        <div className="pl-2 text-kg">{c.name}</div>
                    </div>
                    <div>
                        <div class="text-center leading-none flex justify-between w-full">
                            <span class=" mr-3 inline-flex items-center leading-none text-sm  py-1 ">
                                <button onClick={() => {this.props.registerInCourse(c.code)}} className="bg-white border-gray-800 ml-1 px-2 py-2 border rounded-md  font-bold shadow-sm text-sm hover:shadow-md">Register<FontAwesomeIcon className="ml-2 text-green-500" size="lg" icon={faPlus}/></button>
                            </span>
                            <span class=" inline-flex items-center leading-none text-sm pl-28">
                                <button className="text-md font-semibold hover:underline">View</button>
                            </span>
                        </div>
                    </div>
                </div>
               </div>
            )
        }

        return (
            <div id="myModal" className={`border-t-4 border-green-500 ${this.props.dashboard.state.modalOpen ? 
            "flex h-screen w-screen bg-white rounded-sm" : ""}`}>
                <div class="flex flex-col w-full h-auto pt-2">
                    <div class="sm:px-6 lg:px-8">
                        <div class="flex items-end p-4 pt-8 pl-12">
                            <div className="pr-28">
                                <FontAwesomeIcon className="cursor-pointer text-gray-500 hover:text-gray-900" size="3x" icon={faChevronCircleLeft} 
                                onClick={() => this.props.dashboard.closeModal()} /></div>
                            <div className="text-5xl">
                                <span className="font-semibold pr-10">Register in Course(s)</span>
                            </div>
                        </div>
                        <div className="mt-12 ml-36 w-1/2 border-2 rounded-full mx-auto">
                            <div class="relative text-gray-600">
                                <input type="search" name="serch" placeholder="Search for a Course" class="bg-white w-11/12 h-14 px-5 pr-10 rounded-full text-md 
                                font-semibold focus:outline-none focus:text-gray-900"></input>
                                <button type="submit" class="absolute right-0 top-0 mt-3 mr-4 p-1">
                                    <FontAwesomeIcon className="cursor-pointer text-gray-500 hover:text-gray-900" size="lg" icon={faSearch} />
                                </button>
                            </div>
                        </div>
                        <div class="m-36 mt-10 flex flex-wrap">{cards}</div>
                    </div>
                </div>
            </div>
        );
    }
}

export default FullScreenRegisterModal