// App.js
import React from 'react';
import {BrowserRouter as Router, Route, Routes, Switch} from 'react-router-dom';
import LandingPage from './LandingPage';
import LoginPage from './LoginPage';
import VotingPage from './VotingPage';
import Navigation from "./Navigation";

function App() {
    return (
        <Router>
            <Navigation />
            <Routes>
                <Route exact path="/" component={LandingPage} />
                <Route path="/login" component={LoginPage} />
                <Route path="/vote" component={VotingPage} />
                {/* ... other routes ... */}
            </Routes>
        </Router>
    );
}

export default App;
