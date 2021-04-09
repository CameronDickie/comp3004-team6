import { faFilePdf, faFilePowerpoint } from "@fortawesome/free-regular-svg-icons";
import { faAngleDown, faAngleUp} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { Component } from "react";

class CourseContent extends Component {

    constructor(props){
        super(props)

        this.state = {
            data: [
                {
                    name: "Week 1",
                    path: "/COMP3004/",
                    type: "section",
                    children: []
                },
                {
                    name: "Week 2",
                    path: "/COMP3004/",
                    type: "section",
                    children: [
                        {
                            name: "Intro to OO Design",
                            path: "/COMP3004/Week 2/",
                            type: "content",
                            children: []
                        },
                        {
                            name: "UML Review From Last Year",
                            path: "/COMP3004/Week 2/",
                            type: "content",
                            children: [
                                {
                                    name: "UML Slide Show.pptx",
                                    path: "/COMP3004/Week 2/UML Review From Last Year/",
                                    type: "PPTX",
                                    bytes: []
                                },
                                {
                                    name: "UML Diagrams.pdf",
                                    path: "/COMP3004/Week 2/UML Review From Last Year/",
                                    type: "PDF",
                                    bytes: []
                                }
                            ]
                        }
                    ]
                },
                {
                    name: "Week 3",
                    path: "/COMP3004/",
                    type: "section",
                    children: []
                }
            ]
        }
    }

    buildArticleTrees = () => {
        let trees = []

        for (let i in this.state.data){
            trees.push(<BuildArticle data={this.state.data[i]} />)
        }

        return trees;
    }

    render(){

        return(
            <section class="mt-16 ml-10">
                {this.buildArticleTrees()}
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
            list.push(<BuildArticle data={this.props.data.children[i]} />)
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

        // If it is a file show different UI
        if(article.type == "PPTX" || article.type == "PDF"){

            let icon = faFilePowerpoint;
            let color = "text-purple-500";

            if (article.type == "PDF"){
                icon = faFilePdf
                color = "text-red-500";
            }

            return (
                <article className="m-2"> 
                    <button class="hover:border-gray-400 w-56 border-b-2 flex flex-col items-center px-4 py-6 bg-white text-blue tracking-wide uppercase cursor-pointer">
                        <FontAwesomeIcon className={color} size="4x" icon={icon}/>
                        <span class="mt-3 text-sm leading-normal">{article.name}</span>
                    </button>
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

        return(
        <article class="m-4">
            <div className={`pt-1 pb-1 border-2 rounded-lg ${this.state.expanded ? "border-gray-600" : ""}`}>
                <header class="flex justify-between items-center p-2 pl-6 pr-6 cursor-pointer select-none">
                    <span className={`text-grey-darkest text-xl ${this.state.expanded ? "font-bold underline" : "font-semibold"}`}>
                        {article.name}
                        <span className="pl-4">
                            <a href="#" class="my-0.5 relative inline-flex items-center bg-white rounded-full border border-gray-300 px-3 py-0.5 text-sm">
                                <div class="absolute flex-shrink-0 flex items-center justify-center">
                                <span className={`h-1.5 w-1.5 rounded-full ${isSection ? "bg-gray-500" : "bg-yellow-500"}`} aria-hidden="true"></span>
                                </div>
                                <div class="ml-3.5 font-medium text-gray-900">{article.type}</div>
                            </a>
                        </span>
                    </span>
                    {expandButton}
                </header>
                {expandedContent}
            </div>
        </article>
    )}
}

export default CourseContent;