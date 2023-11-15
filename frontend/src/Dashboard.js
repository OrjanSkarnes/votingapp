import React from 'react';
import { useNavigate } from 'react-router-dom';
import { isUserLoggedIn } from './helpers/sessionStorage';
import fetchWrapper from './helpers/fetchWrapper';
import { getAllPublicPolls } from './helpers/pollService';
import { PollCard} from "./Polls";

export const Dashboard = (props) => {
    const navigate = useNavigate();
    const isLoggedIn = isUserLoggedIn();
    const [pollError, setPollError] = React.useState("");
    const [pollId, setPollId] = React.useState('');
    const [publicPolls, setPublicPolls] = React.useState([]);

    // Load in publicly visible polls
    React.useEffect(() => {
        getAllPublicPolls().then(data => {
            setPublicPolls(data);
        }).catch((error) => {
            console.error(error);
        });
    }, []);

    const handleJoinPoll = async() => {
        // Check that the pollId is not empty and that the poll exists in the database
        if (pollId === '') {
            setPollError('Please enter a poll ID');
            return;
        }
        // Call your Spring REST API to check if the poll exists
        fetchWrapper(`/polls/${pollId}`, 'GET').then(data => {
            navigate(`/vote/${pollId}`);
        }).catch((error) => {
            if (error?.status === 404) {
                setPollError('Poll not found')
            }
        });

        // Redirect to the voting page with the given pollId
    };

    return (
        <>
            <div className='container'>
                <h1>Welcome to SimplePoll</h1>
                <div className='search-field'>
                    <input type="text" placeholder="Enter Poll ID" id="pollIdInput" onChange={e => setPollId(e.target.value)}/>
                    <button onClick={() => handleJoinPoll()}>Vote on poll</button>
                </div>
                {pollError && <div className="error">{pollError}</div>}
                {!isLoggedIn ?
                <button onClick={() => navigate('/login')}>Login / Register</button> :
                    (
                        <div className='logged-in-buttons'>
                            <button onClick={() => navigate('/poll?create=true')}>Create poll</button>
                            <button onClick={() => navigate('/polls')}>Manage poll</button>
                           { /* <button onClick={() => navigate('/groups')}>Manage Groups</button> */}
                        </div>
                    )
                }

            </div>
            <div>
                {publicPolls && <PublicPolls polls={publicPolls} />}
            </div>
        </>
    );
}

const PublicPolls = ({ polls }) => {
    return (
        <div className="container">
            <h2>Pubic polls</h2>
            <div className='grid'>
                {polls.map(poll => <PollCard key={poll.id} poll={poll} onDashboard={true} />)}
            </div>
        </div>
    );
}


export default Dashboard;
