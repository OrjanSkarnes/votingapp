import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import fetchWrapper, { getUser } from './helpers/fetchWrapper';
import { getUserId } from './helpers/sessionStorage';

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
            console.log(data);
            setPollsVotedOnByUser(data.data);
        }).catch((error) => {
            console.error(error?.data);
        });
    }, [userId]);

    const handleDelete = async(poll) => {
        // Call your Spring REST API to delete the poll
        console.log('Poll deleted:', poll);

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
                <button onClick={() => { /* Voting logic here */ }}>Vote</button>
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
    const [timeLeft, setTimeLeft] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const intervalId = setInterval(() => {
            const now = new Date();
            const endTime = new Date(poll.endTime); // assuming poll.endTime is in a format that can be passed to the Date constructor
            const diff = endTime - now;
    
            if (diff > 0) {
                const days = Math.floor(diff / (1000 * 60 * 60 * 24));
                const hours = Math.floor(diff / (1000 * 60 * 60));
                const minutes = Math.floor((diff / (1000 * 60)) % 60);
                const seconds = Math.floor((diff / 1000) % 60);
    
                let timeLeftString = "";
                if (days !== 0) {
                    timeLeftString += `${days} ${days > 1 ? "days" : "day"} `;
                }
                timeLeftString += `${hours}h ${minutes}m ${seconds}s`;
                setTimeLeft(timeLeftString);
            } else {
                setTimeLeft("Poll ended");
                clearInterval(intervalId);
            }
        }, 1000);
    
        return () => clearInterval(intervalId); // cleanup on component unmount
    }, [poll.endTime]);

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