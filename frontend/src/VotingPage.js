import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import fetchWrapper from './helpers/fetchWrapper';

function VotingPage() {
    const location = useLocation();
    // get id based on the path in the url
    const pollId = location.pathname.split('/').pop();

    const [poll, setPoll] = useState(null); // The poll data
    const [vote, setVote] = useState(null); // The poll data

    useEffect(() => {
        // Fetch the poll data from your Spring REST API using the pollId
        // Then set it to the poll state
        console.log(pollId);
        fetchWrapper(`/polls/${pollId}`, 'GET').then(data => {
            console.log(data);
            setPoll(data.data);
        }
        ).catch((error) => {
            console.error(error?.data);
            // Could not find the poll
        });

    }, [pollId]);


  const handleSubmit = () => {
    if (!vote) {
      alert('Please select an option before submitting.');
      return;
    }
     // Call your Spring REST API to record the vote
    console.log('Vote submitted:', vote);
  };

    if (!poll) return <div>Loading...</div>;

    return (
        <div>
             <div className="vote-header">
                <h1>Poll title</h1>
                <span>Time left</span>
            </div>
            <div className="vote-options">
             {poll?.options?.map(option => (
                <label key={option.id} className="vote-option">
                    <input type="radio" name="vote" value={option.id} onChange={() => setVote(option.id)}/>
                    {option.name}
                </label>
            ))}
            </div>
            <button onClick={handleSubmit}>Submit vote</button>
        </div>
    );
}

export default VotingPage;
