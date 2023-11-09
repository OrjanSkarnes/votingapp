import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import {getUser, getUserId} from './helpers/sessionStorage';
import { createPoll, deletePoll, editPoll, getPollById } from './helpers/pollService';

const PollPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const queryParams = new URLSearchParams(location.search);

    const pollId = queryParams.get('pollId');
    const create = queryParams.get('create');

    const [errorMessage, setErrorMessage] = useState(null); 
    const [isCreator, setIsCrator] = useState(false);

    const user = getUser();

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
            getPollById(pollId)
                .then(data => setPoll(data))
                .catch(error => console.error(error?.data))
        }
    }, [pollId]);

    useEffect(() => {
        if(create) return;
        if (poll.creatorId === user?.id) {
            setIsCrator(true);
            setErrorMessage(null);
        } else {
            setErrorMessage('You are not the creator of this poll');
            setIsCrator(false);
        }
    }, [poll, user, create]);



  const handleSubmit = async() => {
     const reqPoll = {
        ...poll,
        creatorId: user.id
    }
    console.log(getUserId())
    createPoll(reqPoll)
        .then((data) => navigate("/polls"))
        .catch((error) => console.error(error?.data))
  };

    const handleEdit = async() => { 
        if (!isCreator) return;
        const reqPoll = {
            ...poll,
            creatorId: user.id
        }
        editPoll(poll.id,reqPoll)
            .then(() => navigate("/polls"))
            .catch((error) => console.error(error?.data))
    }

    const handleDelete = async() => {
        if (!isCreator) return;
        deletePoll(poll.id)
        .then(data => navigate("/polls"))
        .catch((error) => console.error(error?.data));
}

    return (
        <div className='container poll-container'>
            <h1>{create ? "Create a poll" : "Edit poll"}</h1>
            <div className='poll'>
                <input type="text" placeholder="Enter Poll question" value={poll.question} onChange={e => setPoll({...poll, question: e.target.value})}/>
                <input type="text" placeholder="Enter Poll description" value={poll.description} onChange={e => setPoll({...poll, description: e.target.value})}/>
                <input type="datetime-local" placeholder="Enter Poll start time" value={poll.startTime} onChange={e => setPoll({...poll, startTime: e.target.value})}/>
                <input type="datetime-local" placeholder="Enter Poll end time" value={poll.endTime} onChange={e => setPoll({...poll, endTime: e.target.value})}/>

                {/* <div className='checkbox'>
                    <label htmlFor="pollActiveInput">Active</label>
                    <input type="checkbox" id="pollActiveInput" checked={poll.active} onChange={e => setPoll({...poll, active: e.target.checked})}/>
                    <span className='checkmark'></span>
                </div> */}

                <div className='checkbox'>
                    <label htmlFor="pollPrivateAccessInput">Private access</label>
                    <input type="checkbox" id="pollPrivateAccessInput" checked={poll.privateAccess} onChange={e => setPoll({...poll, privateAccess: e.target.checked})}/>
                    <span className='checkmark'></span>
                </div>
                { create ? 
                <button onClick={() => handleSubmit()}>Create poll</button> : 
                (<>
                    <button onClick={() => handleEdit()} disabled={!isCreator}>Save changes</button>
                    <button className='delete-button' onClick={() => handleDelete()} disabled={!isCreator}>Delete poll</button>
                </>)
                }
                {errorMessage && <div className="error">{errorMessage}</div>}
            </div>
        </div>
    );
}

export default PollPage;
