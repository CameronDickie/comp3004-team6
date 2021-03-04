import React, {Component} from 'react';
import logo from './logo.svg';
import "./tailwind.css"; //replace with 'import "./tailwind.generated.css"' when ready for production
import "./main.css"; //this is the css file containing all of the tailwind classes... we're gonna need to modify this for production using postcss; Cam will modify the scripts none of you worry about this :)
import './App.css';

class App extends Component {

    state = {};

        componentDidMount() {
            this.members()
        }

    members = () => {
        fetch('/api/members')
            .then(response => response.text())
            .then(message => {
                this.setState({message: message});
            });
    };

    render() {
        return (
            <div className="App bg-black h-screen">
                <header className="App-header justify-center bg-black">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>Education System Portal</h1>
                    <h2>built by</h2>
                    <h3 className="App-title">{this.state.message}</h3>
                </header>
                <div class="h-10 p-5 bg-black">
                    <p className="App-intro text-white">
                    To get started, edit <code>src/App.js</code> and save to reload it.
                    </p>
                </div>
            </div>
    );
    } 
}

export default App;
