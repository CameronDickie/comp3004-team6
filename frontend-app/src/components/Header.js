import React from 'react';

import logo from '../logo.svg';

const Header = ({isTrue}) => {
    return <header className="App-header justify-center bg-black">
        <img src={logo} className="App-logo mx-auto" alt="logo"/>
        <div className="pt-10">
            <h1 className="text-2xl p-2">Education System Portal</h1>
            <h2 className="text-xl p-2">built by</h2>
        </div>
    </header>;
};

export default Header