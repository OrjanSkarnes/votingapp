// App.js
import React from 'react';
import {BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Dashboard from './Dashboard';
import LoginPage from './LoginPage';
import VotingPage from './VotingPage';
import Navigation from "./Navigation";
import './App.css';
import PollPage from './Poll';
import { PollsPage } from './Polls';
import { getTempId } from './helpers/sessionStorage';
import PollResultsPage from './Result';

export const App = () => {
    // assign a temporary id to the user if they don't have one
    getTempId();

    return (
        <Router>
            <Navigation />
            <Routes>
                <Route exact path="/" element={<Dashboard/>} />
                <Route path="/login" element={<LoginPage/>} />
                <Route path="/vote" element={<VotingPage/>} />
                <Route path="/vote/:pollId" element={<VotingPage/>} />
                <Route path="/poll" element={<PollPage/>} />
                <Route path="/polls" element={<PollsPage/>} />
                <Route path="/result" element={<PollResultsPage/>} />
                {/* ... other routes ... */}
            </Routes>
        </Router>
    );
}

export default App;
