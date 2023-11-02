import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserId } from './helpers/sessionStorage';
import { useCountdown } from './helpers/countdown';
import { deletePoll, getPollsByUser, getPollsVotedOnByUser } from './helpers/pollService';

export const PollsPage = () => {
    const navigate = useNavigate();
    const userId = getUserId();
    const [polls, setPolls] = useState([]);
    const [pollsVotedOnByUser, setPollsVotedOnByUser] = useState([]);

    useEffect(() => {
        getPollsByUser(userId)
            .then(data => setPolls(data))
            .catch((error) => console.error(error?.data));

        getPollsVotedOnByUser(userId)
            .then(data => setPollsVotedOnByUser(data))
            .catch((error) => console.error(error?.data));
    }, [userId]);

    const handleDelete = async(poll) => {
        deletePoll(poll.id)
            .then(data => setPolls(polls.filter(p => p.id !== poll.id)))
            .catch((error) => console.error(error?.data));
    }    
;

    return (
        <div className='polls-container'>
            <h1>Polls</h1>
            <div className='poll-sections'>
                <MyVotes polls={pollsVotedOnByUser} />
                {userId && <ManagePolls polls={polls} handleDelete={handleDelete}/>}
            </div>
            {userId && <button onClick={() => navigate('/poll?create=true')}>Create poll</button> }
        </div>
    );
}

const MyVotes = ({ polls }) => {
    const navigate = useNavigate();

    return (
    <div className='my-votes'>
        <h2>My Votes</h2>
        {polls.length === 0 && <p>You have not voted on any polls</p>}
        {polls.length > 0 && (
        <table>
            <thead>
                <tr>
                    <th>Question</th>
                    <th>Your Vote</th>
                    <th>Time Left</th>
                    <th>Total Votes</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                {polls.map(poll => (
                    <tr key={poll.id}>
                        <td>{poll.question}</td>
                        <td>{poll.vote ? 'For' : 'Against'}</td>
                        <td><Countdown endTime={poll.endTime}></Countdown></td>
                        <td>{poll.votes.length}</td>
                        <td><button className='results-button' onClick={() => navigate(`/result?pollId=${poll.id}`)}>Results</button></td>
                    </tr>
                ))}
            </tbody>
        </table>
        )}
    </div>
    )
}

const ManagePolls = ({ polls, handleDelete }) => {
    return (
        <div className='manage-polls'>
            <h2>Manage polls</h2>
            {polls.map(poll => (
                <PollCard key={poll.id} poll={poll} handleDelete={handleDelete} />
            ))}
        </div>
    );
}

export const PollCard = ({poll, handleDelete}) => {
    const navigate = useNavigate();
    const isCreator = poll.creatorId === getUserId();

    return (
        <div className='poll-card' key={poll.id}>
            <div className='poll-header'>
                <h2>{poll.question}</h2>
                <p><strong>id:</strong> {poll.id}</p>
            </div>
            <div className='poll-info'>
                <p><strong>Active:</strong> {poll.active ? 'Yes' : 'No'}</p>
                <p><strong>Time Left:</strong> <Countdown endTime={poll.endTime}/></p>
                <p><strong>Votes:</strong>{poll.votes?.length}</p>
            </div>

            <div className='poll-actions'>
                {isCreator && 
                        <>
                            <button onClick={() => navigate(`/poll?pollId=${poll.id}`)}>Edit</button>
                            <button className='delete-button' onClick={() => handleDelete(poll)}>Delete</button>
                        </>
                }
                <button className='results-button' onClick={() => navigate(`/result?pollId=${poll.id}`)}>Results</button>
            </div>
        </div>
    );
}

export const Countdown = ({ endTime }) => {
    const timeLeft = useCountdown(endTime)
    return (
        <span>{timeLeft}</span>
    );
}