import React, {Component} from 'react';
import logo from './logo.svg';
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
            <div className="App" style={{backgroundColor:"black"}}>
                <header className="App-header" style={{backgroundColor:"black"}}>
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>Education System Portal</h1>
                    <h2>built by</h2>
                    <h3 className="App-title">{this.state.message}</h3>
                </header>
                <div style={{height:"1000px", backgroundColor:"black", padding:"100px"}}>
                    <p className="App-intro" style={{color:"white"}}>
                    To get started, edit <code>src/App.js</code> and save to reload it.
                    </p>
                </div>
            </div>
    );
    } 
}

export default App;
