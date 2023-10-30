import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import fetchWrapper, { getUser } from './helpers/fetchWrapper';
import { getUserId } from './helpers/sessionStorage';
import { useCountdown } from './helpers/countdown';

export const PollsPage = () => {
    const navigate = useNavigate();
    const userId = getUserId();
    const [polls, setPolls] = useState([]);
    const [pollsVotedOnByUser, setPollsVotedOnByUser] = useState([]);

    useEffect(() => {
        fetchWrapper('/polls', 'GET').then(data => {
            setPolls(data.data);
        }).catch((error) => {
            console.error(error?.data);
        });

        fetchWrapper(`/polls/user/${userId}/votes`, 'GET').then(data => {
            setPollsVotedOnByUser(data.data);
        }).catch((error) => {
            console.error(error?.data);
        });
    }, [userId]);

    const handleDelete = async(poll) => {
        // Call your Spring REST API to delete the poll
        await fetchWrapper(`/polls/${poll.id}`, 'DELETE').then(data => {
            console.log(data);
            setPolls(polls.filter(p => p.id !== poll.id));
        }).catch((error) => {
            console.error(error?.data);

        });
    }    

    return (
        <div className='polls-container'>
            <h1>Polls</h1>
            <div className='poll-sections'>
                <MyVotes polls={pollsVotedOnByUser} />
                <ManagePolls polls={polls} handleDelete={handleDelete}/>
            </div>
            <button onClick={() => navigate('/poll?create=true')}>Create poll</button>
        </div>
    );
}

const MyVotes = ({ polls }) => (
    <div className='my-votes'>
        <h2>My Votes</h2>
        {polls.map(poll => (
            <div className='poll-item' key={poll.id}>
                <span>{poll.question}</span>
                <span>Show voted for</span>
                <span>Time left</span>
            </div>
        ))}
    </div>
);

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

const PollCard = ({poll, handleDelete}) => {
    const timeLeft = useCountdown(poll.endDate);
    const navigate = useNavigate();

    return (
        <div className='poll-card' key={poll.id}>
            <h2>{poll.question}</h2>
            <p><strong>id:</strong> {poll.id}</p>
            <p><strong>Description:</strong> {poll.description}</p>
            {/* Should be icons */}
            <p><strong>Active:</strong> {poll.active ? 'Yes' : 'No'}</p>
            <p><strong>Private:</strong> {poll.privateAccess ? 'Yes' : 'No'}</p>
            <p><strong>Time Left:</strong> {timeLeft}</p>
            <button onClick={() => navigate(`/poll?pollId=${poll.id}`)}>Edit</button>
            <button className='delete-button' onClick={() => handleDelete(poll)}>Delete</button>
            <button onClick={() => navigate(`/results?pollId=${poll.id}`)}>Results</button>
        </div>
    );
}