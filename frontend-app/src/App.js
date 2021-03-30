import React, { useState, useEffect } from 'react';
import { BrowserRouter, Switch, Route } from "react-router-dom";
import LoginForm from './components/LoginForm.js';
import SignUp from './components/SignUp.js';
import Dashboard from './pages/Dashboard.js';

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

    // for user logout
    const clearStorage = () => {
        setUser({});
    }

    const setU = (usr) => {
        setUser(usr);
        console.log(user.username)
    }

    const getUser = () => {
        return user;
    }
  
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" >
                    <LoginForm setU={setU}/>
                </Route>
                <Route exact path="/signup">
                    <SignUp />
                </Route>
                <Route exact path="/dashboard">
                    <Dashboard getUser={getUser} logout={clearStorage} />
                </Route>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
