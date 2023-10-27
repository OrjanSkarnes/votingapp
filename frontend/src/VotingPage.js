import React, { useEffect, useState } from 'react';

function VotingPage(props) {
    const pollId = props.match.params.pollId;
    const [poll, setPoll] = useState(null); // The poll data

    useEffect(() => {
        // Fetch the poll data from your Spring REST API using the pollId
        // Then set it to the poll state
    }, [pollId]);

    const handleVote = (voteOption) => {
        // Call your Spring REST API to record the vote
    };

    if (!poll) return <div>Loading...</div>;

    return (
        <div>
            <h1>{poll.title}</h1>
            {poll.options.map(option => (
                <button key={option.id} onClick={() => handleVote(option)}>{option.text}</button>
            ))}
        </div>
    );
}

export default VotingPage;
