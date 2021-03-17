import React from "react"

class Test extends React.Component{
    render() {
        return (
            <div>
                <p>this is a test to make sure that the router is working as it should be</p>
                <p>and that it still can accept props: {this.props.user.username}</p>
            </div>
        )
    }
}
export default Test;