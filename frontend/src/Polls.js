import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserId } from './helpers/sessionStorage';
import { useCountdown } from './helpers/countdown';
import { deletePoll, getPollsByUser, getPollsVotedOnByUser, finishPoll } from './helpers/pollService';
import analyticsService from './helpers/analyticsService';


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

    const handleDelete = async (poll) => {
        try {
            console.log(poll)

            await deletePoll(poll.id);
            setPolls(polls.filter(p => p.id !== poll.id));

            // Structure the analytics event data
            const analyticsEventData = {
                eventName: 'Poll Deleted',
                eventData: {
                    pollId: poll.id,
                    creatorId: poll.creatorId,
                    question: poll.question,
                    // ... any other relevant data ...
                },
                // timestamp can be omitted, server can handle it
            };

            // Track poll deletion event
            analyticsService.trackEvent(analyticsEventData);
        } catch (error) {
            console.error(error?.data);
        }
    };



    const handleFinish = async(poll) => {
        finishPoll(poll.id)
            .then(data => setPolls(polls.map(p => p.id === poll.id ? data : p)))
            .catch((error) => console.error(error?.data));
    }

    return (
        <div className='polls-container'>
            <h1>Polls</h1>
            <MyVotes polls={pollsVotedOnByUser} />

            <div className='poll-sections'>
                {userId && <ManagePolls polls={polls} handleDelete={handleDelete} handleFinish={handleFinish}/>}
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

const ManagePolls = ({ polls, handleDelete, handleFinish }) => {
    return (
        <div className='manage-polls'>
            <h2>Manage polls</h2>
            <div className='grid'>
                {polls.map(poll => (
                    <PollCard key={poll.id} poll={poll} handleDelete={handleDelete} handleFinish={handleFinish} />
                ))}
            </div>
        </div>
    );
}

export const PollCard = ({poll, handleDelete, onDashboard, handleFinish}) => {
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
                <Countdown endTime={poll.endTime}/>
                <p><strong>Votes:</strong> {poll.votes?.length}</p>
            </div>

            <div className='poll-actions'>
                {isCreator && !onDashboard && poll.active &&
                    <>
                        <button onClick={() => navigate(`/poll?pollId=${poll.id}`)}>Edit</button>
                        {poll.active && handleFinish && <button onClick={() => handleFinish(poll)}>Finish</button>}
                    </>
                }
                {onDashboard && poll.active && <button onClick={() => navigate(`/vote/${poll.id}`)}>Vote</button>}
                <button className='results-button' onClick={() => navigate(`/result?pollId=${poll.id}`)}>Results</button>
                {isCreator && !onDashboard &&<button className='delete-button' onClick={() => handleDelete(poll)}>Delete</button>}
            </div>
        </div>
    );
}

export const Countdown = ({ endTime }) => {
    const {timeLeft, ended} = useCountdown(endTime)
    const title = ended ? <strong>Ended:</strong> : <strong>Time Left:</strong>;
    return (
        <p>{title} {timeLeft}</p>
    );
}