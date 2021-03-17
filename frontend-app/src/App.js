import React, { useState, useEffect } from 'react';

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
        <div className="w-full h-full flex">
            <div className="mt-20 mx-auto">
                <h1 className="text-2xl text-gray-600">USER LOGIN</h1>
                <p>USER ID: {user.userID}</p>
                <div className="p-2">
                    <button className="p-2 border-2 font-medium text-lg text-green-500" onClick={doLogin}>Login</button>
                    <div className="pt-2">
                        <button className="p-2 border-2 font-medium text-lg text-red-500" 
                        onClick={clearStorage}>Logout / Clear Storage</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default App;
