import React from 'react';

function LandingPage(props) {
    const handleJoinPoll = (pollId) => {
        // Redirect to the voting page with the given pollId
        props.history.push(`/vote/${pollId}`);
    };

    return (
        <div>
            <h1>Welcome to SimplePoll</h1>
            <button onClick={() => props.history.push('/login')}>Login</button>
            <div>
                <input type="text" placeholder="Enter Poll ID" id="pollIdInput" />
                <button onClick={() => handleJoinPoll(document.getElementById('pollIdInput').value)}>Join Poll</button>
            </div>
        </div>
    );
}

export default LandingPage;
