import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useNavigation } from 'react-router-dom';
import { useCountdown } from './helpers/countdown';
import { getUser } from './helpers/sessionStorage';
import PollService from './helpers/pollService';
import UserService from './helpers/userService';
import VoteService from './helpers/voteService';

function VotingPage() {
    const location = useLocation();
    const navigate = useNavigate();
    // get id based on the path in the url
    const pollId = location.pathname.split('/').pop();

    const [poll, setPoll] = useState(null); // The poll data
    const [vote, setVote] = useState(null); // The poll data
    const [errorMessage, setErrorMessage] = useState(null); // The poll data

    const timeLeft = useCountdown(poll?.endTime);


    useEffect(() => {
        PollService.getPollById(pollId).then(data => {
            setPoll(data.data);
            UserService.getUserById(data.data.creatorId)
                .then(userRes => setPoll({...data.data, creator: userRes.data}))
                .catch((error) => {setErrorMessage('Could not find creator of poll')});
        }).catch((error) => {
            setErrorMessage('Poll not found')
        });
    }, [pollId]);


  const handleSubmit = () => {
    if (vote === null) {
         alert('Please select an option before submitting.');
         return;
    }
    const reqVote = {
        choice: vote,
        userId: getUser()?.id,
        pollId: poll.id
    }

    VoteService.createVote(reqVote)
        .then(data => navigate(`/polls`))
        .catch((error) => {
            switch (error?.status) {
                case 409:
                    setErrorMessage('You have already voted on this poll')
                    break;
                case 404:
                    setErrorMessage('Poll not found')
                    break;
                case 400:
                    setErrorMessage('You cannot vote on your own poll')
                    break;
                case 401:
                    setErrorMessage('You must be logged in to vote')
                    break;
                default:
                    setErrorMessage('Something went wrong')
                    break;
            }
        });
    };

    if (!poll) return <div>Loading...</div>;

    return (
        <div className='container'>
            <div className="vote-header">
                <h1>{poll.question}</h1>
                <p>Description:{poll.description}</p>
                <p>Created by: {poll.creator?.username}</p>
                <p>Votes: {poll.voteIds?.length} </p>
                <span>Time left: {timeLeft}</span>
            </div>
            <br></br>
            <h2>Vote</h2>
            <div className="vote-options">
                 <label className="vote-option">
                    <input type="radio" name="vote" value={true} onChange={() => setVote(true)}/>
                    For
                </label>
                <label className="vote-option">
                    <input type="radio" name="vote" value={false} onChange={() => setVote(false)}/>
                    Against
                </label>
            </div>
            <button onClick={handleSubmit}>Submit vote</button>
            {errorMessage && <div className="error">{errorMessage}</div>}
        </div>
    );
}

export default VotingPage;
