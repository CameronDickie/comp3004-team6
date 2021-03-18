import React, { useState, useEffect } from 'react';
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Splash from "./pages/splash.js"
import Test from "./pages/test.js"

function useStickyState(defaultValue, key) {

    // Checks local storage and sets values
    const [value, setValue] = useState(() => {
      const stickyValue = window.localStorage.getItem(key);
      return stickyValue !== null
        ? JSON.parse(stickyValue)
        : defaultValue;
    });

    // Used to set the value
    useEffect(() => {
      window.localStorage.setItem(key, JSON.stringify(value));
    }, [key, value]);
    return [value, setValue];
}

function App() {
    // This data will be persistent
    const [
      user,
      setUser
    ] = useStickyState({}, "user");

    // Basically like beforeComponentLoad
    useEffect(() => {
        // Update the document title using the browser API
        console.log("App is starting...")
    });

    const doLogin = async () => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'text/html' },
            body: "Billy Bob"
        };
        
        await fetch('/api/login', requestOptions)
            .then(response => response.json())
            .then(user => {
                setUser(user);
            });
    };

    // for user logout
    const clearStorage = () => {
        setUser({});
    }
  
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" >
                    <Splash user={user} setUser={setUser} doLogin={doLogin} clearStorage={clearStorage}/>
                </Route>
                <Route exact path="/test">
                    <Test user={user} />
                </Route>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
