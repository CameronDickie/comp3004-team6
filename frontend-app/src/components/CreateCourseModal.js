import { faUpload, faWindowClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";


class CreateCourseModal extends Component{

    constructor(props) {
        super(props);
        this.state = {
            cool: true
        }
    }


    render(){
        return (
            <div className={`flex items-center justify-center fixed left-0 bottom-0 w-full h-full bg-gray-300 bg-opacity-60 z-50 ${(this.props.show == true) ? "" : "hidden"}`}>
                <div class="bg-white rounded-lg w-1/2 h-5/6 border border-gray-300">
                    <div class="flex flex-col items-start p-6">
                        <div class="flex items-center w-full">
                            <div class="text-gray-900 font-bold text-3xl uppercase">Create a Course</div>
                            <svg onClick={this.props.hide} class="ml-auto fill-current text-gray-700 w-6 h-6 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 18 18">
                                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z"/>
                            </svg>
                        </div>
                        <hr></hr>
                        {/* <div class="">Create a NEW Course for the University</div> */}


                        <div class="px-4 py-5 bg-white space-y-6 sm:p-6 w-full">
                                    
                                    <div class="mb-6">
                                        <label for="username" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">COURSE CODE</label>
                                        <input type="username" name="username" id="username" placeholder="ex. COMP3004" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                                    </div>

                                    <div class="mb-6">
                                        <label for="course_name" class="block mb-2 text-sm text-gray-600 dark:text-gray-400">COURSE NAME</label>
                                        <input type="username" name="course_name" id="course_name" placeholder="ex. Object Oriented Software Engineering" class="w-full px-3 py-2 placeholder-gray-300 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-gray-700 dark:text-white dark:placeholder-gray-500 dark:border-gray-600 dark:focus:ring-gray-900 dark:focus:border-gray-500" />
                                    </div>

                                    <div>
                                    <label for="about" class="block text-sm uppercase text-gray-700">
                                        Description
                                    </label>
                                    <div class="mt-1">
                                        <textarea id="about" name="about" rows="3" class="p-1 shadow-sm focus:ring-indigo-500 focus:border-indigo-500 mt-1 block w-full sm:text-sm border-gray-300 rounded-md" placeholder=" Brief description for your profile. URLs are hyperlinked."></textarea>
                                    </div>
                                  
                                    </div>

                                    <div>
                                    <label class="block text-sm font-medium text-gray-700">
                                        Photo
                                    </label>
                                    <div class="mt-1 flex items-center">
                                        <span class="inline-block h-12 w-12 rounded-full overflow-hidden bg-gray-100">
                                        <svg class="h-full w-full text-gray-300" fill="currentColor" viewBox="0 0 24 24">
                                            <path d="M24 20.993V24H0v-2.996A14.977 14.977 0 0112.004 15c4.904 0 9.26 2.354 11.996 5.993zM16.002 8.999a4 4 0 11-8 0 4 4 0 018 0z" />
                                        </svg>
                                        </span>
                                        <button type="button" class="ml-5 bg-white py-2 px-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                        Change
                                        </button>
                                    </div>
                                    </div>

                                    <div>
                                    <label class="block text-sm font-medium text-gray-700">
                                        Cover photo
                                    </label>
                                    <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                                        <div class="space-y-1 text-center">
                                        <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                            <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <div class="flex text-sm text-gray-600">
                                            <label for="file-upload" class="relative cursor-pointer bg-white rounded-md font-medium text-indigo-600 hover:text-indigo-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-indigo-500">
                                            <span>Upload a file</span>
                                            <input id="file-upload" name="file-upload" type="file" class="sr-only"></input>
                                            </label>
                                            <p class="pl-1">or drag and drop</p>
                                        </div>
                                        <p class="text-xs text-gray-500">
                                            PNG, JPG, GIF up to 10MB
                                        </p>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                                <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
                                    <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                    Save
                                    </button>
                                </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default CreateCourseModal;