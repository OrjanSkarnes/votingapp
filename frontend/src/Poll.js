import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import fetchWrapper from './helpers/fetchWrapper';
import { getUser } from './helpers/sessionStorage';

const PollPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const queryParams = new URLSearchParams(location.search);

    const pollId = queryParams.get('pollId');
    const create = queryParams.get('create');

    const [poll , setPoll] = useState({
        question: '',
        description: '',
        startTime: '',
        endTime: '',
        active: false,
        privateAccess: false
    });

    useEffect(() => {
        if (pollId) {
            // Call your Spring REST API to get the poll
            fetchWrapper(`/polls/${pollId}`, 'GET').then(data => {
                console.log(data);
                setPoll(data.data);
            }).catch((error) => {
                console.error(error?.data);
                // Could not find the poll
            });
        }
    }, [pollId]);



  const handleSubmit = async() => {
     // Call your Spring REST API to create the poll
     console.log('Poll submitted:', poll);
     const reqPoll = {
        ...poll,
        creator: getUser()
    }

     await fetchWrapper('/polls', 'POST', reqPoll).then(data => {
        console.log(data);
        // redirect to the polls view
        navigate(`/polls`);
    }).catch((error) => {
        console.error(error?.data);

    });
  };

    const handleEdit = async() => { 
        // Call your Spring REST API to edit the poll
        console.log('Poll edited:', poll);

        await fetchWrapper(`/polls/${poll.id}`, 'PUT', poll).then(data => {
            console.log(data);
            navigate(`/polls`);
        }).catch((error) => {
            console.error(error?.data);

        });
    }

    const handleDelete = async() => {
        // Call your Spring REST API to delete the poll
        console.log('Poll deleted:', poll);

        await fetchWrapper(`/polls/${poll.id}`, 'DELETE').then(data => {
            console.log(data);
            navigate(`/polls`);
        }).catch((error) => {
            console.error(error?.data);

        });
    }

    return (
        <div className='container poll-container'>
            <h1>{create ? "Create a poll" : "Edit poll"}</h1>
            <div className='poll'>
                <input type="text" placeholder="Enter Poll question" value={poll.question} onChange={e => setPoll({...poll, question: e.target.value})}/>
                <input type="text" placeholder="Enter Poll description" value={poll.description} onChange={e => setPoll({...poll, description: e.target.value})}/>
                <input type="datetime-local" placeholder="Enter Poll start time" value={poll.startTime} onChange={e => setPoll({...poll, startTime: e.target.value})}/>
                <input type="datetime-local" placeholder="Enter Poll end time" value={poll.endTime} onChange={e => setPoll({...poll, endTime: e.target.value})}/>

                <div className='checkbox'>
                    <label htmlFor="pollActiveInput">Active</label>
                    <input type="checkbox" id="pollActiveInput" checked={poll.active} onChange={e => setPoll({...poll, active: e.target.checked})}/>
                    <span className='checkmark'></span>
                </div>

                <div className='checkbox'>
                    <label htmlFor="pollPrivateAccessInput">Private access</label>
                    <input type="checkbox" id="pollPrivateAccessInput" checked={poll.privateAccess} onChange={e => setPoll({...poll, privateAccess: e.target.checked})}/>
                    <span className='checkmark'></span>
                </div>
                { create ? 
                <button onClick={() => handleSubmit()}>Create poll</button> : 
                (<>
                    <button onClick={() => handleEdit()}>Save changes</button>
                    <button className='delete-button' onClick={() => handleDelete()}>Delete poll</button>
                </>)
                }
            </div>
        </div>
    );
}

export default PollPage;
