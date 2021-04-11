import { faMarkdown } from "@fortawesome/free-brands-svg-icons";
import { faFilePdf, faFilePowerpoint, faFileWord } from "@fortawesome/free-regular-svg-icons";
import { faAlignRight, faAngleDown, faAngleUp, faDatabase, faMinus, faPaperPlane, faPenAlt, faPencilAlt, faPercent, faPercentage, faPlus, faPlusCircle} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";

//TEST DATA
import AddCourseContentModal from "./AddCourseContentModal";
import AddGradeModal from "./AddGradeModal";

class CourseContent extends Component {

    constructor(props){
        super(props)

        this.state = {
            name: this.props.course.name,
            path: this.props.course.path,
            data: this.props.course.children,
            addContentModalOpen: false,
            addGradeModal: false,
            isSubmission: false,
            cur_path: "/COMP3004/",
        }
    }

    deleteCourseContent = async (p) => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: JSON.stringify({
                courseCode: this.props.courseCode,
                path: p,
            })
        };
        
        await fetch('/api/delete-content', requestOptions)
            .then(response => response.text())
            .then(res => {
                console.log(res)
            });
    }

    buildArticleTrees = () => {
        let trees = []

        let data = this.props.course.children

        for (let i in data){
            let file = data[i].wrappee.file
            let byteStream = null

            if (file != null){
                byteStream = file.byteString
            }


            if (data[i].wrappee.wrappee){
                trees.push(<BuildArticle 
                    getUser={this.props.getUser} 
                    data={data[i].wrappee.wrappee} 
                    editable={data[i].editable} 
                    bytes={byteStream} 
                    openAddContent={this.toggleCourseContentModal}
                    openGradeItem={this.toggleGradeModal}
                    deadline={data[i].wrappee.dateString} 
                    grade={data[i].wrappee.grade}
                    deleteContent={this.deleteCourseContent} />)
            } else {
                trees.push(
                    <BuildArticle 
                        getUser={this.props.getUser}
                        data={data[i].wrappee} 
                        editable={data[i].editable} 
                        bytes={byteStream} 
                        openAddContent={this.toggleCourseContentModal}
                        openGradeItem={this.toggleGradeModal}
                        deadline={data[i].wrappee.dateString} 
                        grade={data[i].wrappee.grade}
                        deleteContent={this.deleteCourseContent} />)
            }
        }

        return trees;
    }

    toggleCourseContentModal = (new_path, isSub) => {
        if (new_path != null){
            if (isSub != null){
                this.setState({addContentModalOpen: !this.state.addContentModalOpen, cur_path: new_path, isSubmission: isSub})
            } else this.setState({addContentModalOpen: !this.state.addContentModalOpen, cur_path: new_path})
        } else this.setState({addContentModalOpen: !this.state.addContentModalOpen, cur_path: this.state.path})
    }

    toggleGradeModal = (new_path) => {
        if (new_path != null){
            this.setState({addGradeModal: !this.state.addGradeModal, cur_path: new_path})
        } else this.setState({addGradeModal: !this.state.addGradeModal, cur_path: this.state.path})
    }

    render(){

        return(
            <section class="mt-10 ml-16">
                <div className="pl-6 w-full">
                    <div className="flex justify-between">
                        <div className="col-span-1">
                            <div className="text-5xl font-semibold italic text-gray-200">Course Content:</div>
                        </div>
                        <div className="">
                            <button className={`px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold border-yellow-500 hover:shadow-md ${(this.props.getUser().type == "Student") ? "hidden" : ""}`}
                            onClick={()=>this.toggleCourseContentModal("/" + this.props.courseCode + "/")}>
                                    Add Content
                                    <span className="ml-2">
                                        <FontAwesomeIcon className="text-gray-800 hover:text-yellow-500" size="lg" icon={faPlusCircle}/>
                                    </span>
                            </button>
                        </div>
                    </div>
                </div>
                {this.buildArticleTrees()}

                <AddCourseContentModal getUser={this.props.getUser} show={this.state.addContentModalOpen} hide={this.toggleCourseContentModal} 
                cPath={this.state.cur_path} courseCode={this.props.courseCode} isSubmission={this.state.isSubmission}/>

                <AddGradeModal getUser={this.props.getUser} show={this.state.addGradeModal} hide={this.toggleGradeModal} 
                cPath={this.state.cur_path} courseCode={this.props.courseCode}/>

            </section>
        )
    }
}


class BuildArticle extends Component {

    constructor(props){
        super(props)

        this.state = {
            expanded: false
        }
    }

    buildChildren = () => {
        let list = []

        for (let i in this.props.data.children){

            let file = this.props.data.children[i].wrappee.file
            let byteStream = null

            if (file != null){
                byteStream = file.byteString
            }

            if (this.props.data.children[i].wrappee.wrappee){
                list.push(
                    <BuildArticle 
                        getUser={this.props.getUser} 
                        data={this.props.data.children[i].wrappee.wrappee} 
                        editable={this.props.data.children[i].editable} 
                        bytes={byteStream} 
                        deadline={this.props.data.children[i].wrappee.dateString} 
                        grade={this.props.data.children[i].wrappee.grade} 
                        openAddContent={this.props.openAddContent}
                        openGradeItem={this.props.openGradeItem}
                        deleteContent={this.props.deleteContent} />)
            } else {
                 list.push(
                    <BuildArticle 
                        getUser={this.props.getUser} 
                        data={this.props.data.children[i].wrappee} 
                        editable={this.props.data.children[i].editable} 
                        bytes={byteStream} 
                        deadline={this.props.data.children[i].wrappee.dateString} 
                        grade={this.props.data.children[i].wrappee.grade} 
                        openAddContent={this.props.openAddContent}
                        openGradeItem={this.props.openGradeItem}
                        deleteContent={this.props.deleteContent}  />)
            }
        }

        return list;
    }

    toggleExpanded = () => {
        this.setState({expanded: !this.state.expanded})
    }

    render() {
        let article = this.props.data;

        let isSection = (article.type == "section");
        let isContent = (article.type == "content");
        let isDefault = (article.type == "default");
        let isDeliverable = (article.type == "deliverable");
        let isLecture = (article.type == "lecture");
        let hasGrade =  ((this.props.grade != -1.0) && (this.props.grade != null));

        let studentViewing = (this.props.getUser().type == "Student");
        let professorCreated = (article.userType == "Professor")

        // If the student is viewing, and professor did not create, and studentID!=article-userID
        if (studentViewing && !professorCreated && (article.userID != this.props.getUser().studentID)){
            return (
                <div className="hidden"></div>
            )
        }


        // If it is a file show different UI
        if(article.type == "PPTX" || article.type == "PDF" || article.type == "DOCX"){

            let icon = faFilePowerpoint;
            let color = "text-purple-500";

            if (article.type == "PDF"){
                icon = faFilePdf
                color = "text-red-500";
            }

            if (article.type == "DOCX"){
                icon = faFileWord
                color = "text-blue-500";
            }

            return (
                <article class="m-4">
                    <div className={`pt-1 pb-1 border-b-2 ${this.state.expanded ? "border-gray-500" : ""}`}>
                        <header class="flex h-16 justify-between items-center p-2 pl-6 pr-6 cursor-pointer select-none">
                            <a download={article.name} target="_blank" href={'data:application/octet-stream;base64,' + this.props.bytes}>
                                <span className={`text-grey-600 text-xl font-normal`}>
                                    <FontAwesomeIcon className={color} size="2x" icon={icon}/>
                                    <span className="pl-6 hover:text-gray-800 hover:underline">
                                        {article.name}
                                    </span>
                                </span>
                            </a>
                            <span className={`pl-4 ${(!professorCreated) ? "" : "hidden"}`}>
                                <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                    <div class="absolute flex-shrink-0 flex items-center justify-center">
                                    <span className="h-1.5 w-1.5 rounded-full bg-green-500" aria-hidden="true"></span>
                                    </div>
                                    <div class="ml-3.5 font-medium text-gray-900">submission</div>
                                </a>
                            </span>
                            <span className={`pl-4 ${(((professorCreated && !studentViewing) || (!professorCreated && studentViewing)) && !hasGrade) ? "" : "hidden"}`}>
                                <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold hover:border-red-500 hover:shadow-md"
                                onClick={() => this.props.deleteContent(article.path + article.name + "/")}>
                                    Del
                                    <span className="ml-2">
                                        <FontAwesomeIcon className="text-red-500" size="lg" icon={faMinus}/>
                                    </span>
                                </button>
                            </span>
                            <span className={`pl-4 ${hasGrade ? "" : "hidden"}`}>
                                <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold">
                                    Grade={this.props.grade}
                                </button>
                            </span>

                            <span className={`pl-4 ${(!hasGrade && !studentViewing && !professorCreated) ? "" : "hidden"}`}>
                            <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold border-green-500 hover:shadow-md"
                            onClick={() => this.props.openGradeItem(article.path + article.name + "/")}>
                                Add Grade
                                <span className="ml-2">
                                    <FontAwesomeIcon className="text-gray-800 hover:text-green-500" size="lg" icon={faPercent}/>
                                </span>
                            </button>
                    </span>
                            
                        </header>
                    </div>
                </article>
            )
        }

        let expandedContent = <div id="expand-content">
            {this.buildChildren()}
        </div>

        if (isContent){
            expandedContent = <div id="expand-content">
            <div className="ml-6 mt-4 text-lg" >Content:</div>
            <div className="grid grid-cols-2 m-4">
                {this.buildChildren()}
            </div>
        </div>
        }

        // Wait to be expanded
        if (!this.state.expanded){
            expandedContent = null;
        }

        let expandButton = <button onClick={()=>{this.toggleExpanded()}} class="h-10 w-10 bg-gray-100 rounded-full ring-8 ring-white flex items-center justify-center">
            <FontAwesomeIcon className="text-gray-600" size="lg" icon={faAngleDown}/>
        </button>

        if (this.state.expanded){
            expandButton = <button onClick={()=>{this.toggleExpanded()}} class="h-10 w-10 bg-gray-800 rounded-full ring-8 ring-white flex items-center justify-center">
                <FontAwesomeIcon className="text-gray-100" size="lg" icon={faAngleUp}/>
            </button>
        }

        let pillClass = `h-1.5 w-1.5 rounded-full bg-yellow-500`

        if (isSection) pillClass = `h-1.5 w-1.5 rounded-full bg-gray-500`
        if (isDefault) pillClass = `h-1.5 w-1.5 rounded-full bg-pink-500`;
        // if (isSubmission) pillClass = `h-1.5 w-1.5 rounded-full bg-green-500`;
        if (isDeliverable) pillClass = `h-1.5 w-1.5 rounded-full bg-blue-500`;

        console.log(article.name + ": " + this.props.editable)

        return(
        <article class="m-6">
            <div className={`pt-1 pb-1 border-2 rounded-lg ${this.state.expanded ? "border-gray-500" : ""}`}>
                <header class="flex justify-between items-center p-2 pl-6 pr-6 cursor-pointer select-none">
                    <span className={`text-grey-darkest text-xl ${this.state.expanded ? "font-bold underline" : "font-semibold"}`}>
                        {article.name}
                        <span className="pl-4">
                            <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                <div class="absolute flex-shrink-0 flex items-center justify-center">
                                <span className={pillClass} aria-hidden="true"></span>
                                </div>
                                <div class="ml-3.5 font-medium text-gray-900">{article.type}</div>
                            </a>
                        </span>
                    </span>
                    <span className={`pl-4 ${hasGrade ? "" : "hidden"}`}>
                            <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold">
                                Grade={this.props.grade}
                            </button>
                    </span>
                    <span className={`pl-4 ${((professorCreated && !studentViewing) || (!professorCreated && studentViewing)) ? "" : "hidden"}`}>
                            <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold hover:border-red-500 hover:shadow-md"
                            onClick={() => this.props.deleteContent(article.path + article.name + "/")}>
                                Del
                                <span className="ml-2">
                                    <FontAwesomeIcon className="text-red-500" size="lg" icon={faMinus}/>
                                </span>
                            </button>
                    </span>
                    <span className={`pl-4 ${(isDeliverable && studentViewing) ? "" : "hidden"}`}>
                            <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono border-green-500 hover:shadow-md"
                            onClick={() => this.props.openAddContent((article.path + article.name + "/"), true)}>
                                Add Submission
                                <span className="ml-2">
                                    <FontAwesomeIcon className="text-gray-800 hover:text-green-500" size="lg" icon={faPlus}/>
                                </span>
                            </button>
                    </span>

                    <span className={`pl-4 ${((isSection || isLecture || isDeliverable) && !studentViewing) ? "" : "hidden"}`}>      
                        <button className="px-3 rounded-lg py-2 border-2 text-sm font-mono font-semibold border-yellow-500 hover:shadow-md"
                        onClick={() => this.props.openAddContent(article.path + article.name + "/")}>
                                Add Content
                                <span className="ml-2">
                                    <FontAwesomeIcon className="text-gray-800 hover:text-yellow-500" size="lg" icon={faPlusCircle}/>
                                </span>
                        </button>
                    </span>
                    {expandButton}
                </header>
                <div className="p-5" className={`p-5 ${isDeliverable ? "" : "hidden"}`}>
                    DUE AT: {this.props.deadline}
                </div>
                {expandedContent}
            </div>
        </article>
    )}
}

export default CourseContent;