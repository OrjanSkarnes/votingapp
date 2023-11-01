import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useNavigation } from 'react-router-dom';
import { useCountdown } from './helpers/countdown';
import { getUser, isUserLoggedIn } from './helpers/sessionStorage';
import PollService from './helpers/pollService';
import UserService from './helpers/userService';
import VoteService from './helpers/voteService';

function VotingPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const pollId = location.pathname.split('/').pop();
    const isLoggedIn = isUserLoggedIn();
    const [poll, setPoll] = useState(null);
    const [vote, setVote] = useState(null); 
    const [errorMessage, setErrorMessage] = useState(null);
    const timeLeft = useCountdown(poll?.endTime);


    useEffect(() => {
        const fetchData = async () => {
            try {
                const pollData = (await PollService.getPollById(pollId));
                const creatorData = await UserService.getUserById(pollData.creatorId);
                setPoll({ ...pollData, creator: creatorData });
            } catch (error) {
                setErrorMessage('Could not fetch data');
            }
          };
          fetchData();
    }, [pollId]);


  const handleSubmit = async () => {
        const errorMessages = {
            409: 'You have already voted on this poll',
            404: 'Poll not found',
            400: 'You cannot vote on your own poll',
            401: 'You must be logged in to vote',
        };

        if (vote === null) {
            alert('Please select an option before submitting.');
            return;
        }
        if (new Date(poll.endTime) < new Date()) {
            setErrorMessage('This poll has expired.');
            return;
        }
        const reqVote = {
            choice: vote,
            userId: getUser()?.id,
            pollId: poll.id
        }

        try {
            await VoteService.createVote(reqVote);
            navigate('/polls');
        } catch (error) {
            setErrorMessage(errorMessages[error?.status] || 'Something went wrong');
        }
    };

    if (!poll) return <div>Loading...</div>;

    const mustBeLoggedIn = poll?.privateAccess && !isLoggedIn;
    const isClosed = new Date(poll.endTime) < new Date();

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
            {mustBeLoggedIn && !isLoggedIn && <p>You must be logged in to vote on this poll.</p>}
            {mustBeLoggedIn && <button onClick={() => navigate('/login')}>Login / Register</button>}
            {!poll.active && <p>This poll is not active.</p>}
            {isClosed && <p>This poll is closed.</p>}
            {(isClosed || !poll.active) && <button onClick={() => navigate(`/result?pollId=${poll.id}`)}>View Results</button>}

            
            {!mustBeLoggedIn && !isClosed && poll.active &&
            (<>
                <p>Choose an option below to vote on this poll.</p>
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
            </>
            )}
            {errorMessage && <div className="error">{errorMessage}</div>}
        </div>
    );
}

export default VotingPage;
