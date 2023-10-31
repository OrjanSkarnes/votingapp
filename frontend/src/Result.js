import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import PollService from './helpers/pollService';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
  } from 'chart.js';

import { Bar } from "react-chartjs-2";
import UserService from './helpers/userService';
import { Countdown } from './Polls';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

function PollResultsPage() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const pollId = queryParams.get('pollId');
    const [poll, setPoll] = useState(null);
    const [votes, setVotes] = useState(null);
    const [creator, setCreator] = useState(null);

    useEffect(() => {
        PollService.getPollById(pollId).then(data => setPoll(data.data));
        PollService.getPollVotes(pollId).then(data => setVotes(data.data));
    }, [pollId]);

    useEffect(() => {
        if (!poll) return;
        UserService.getUserById(poll?.creatorId).then(data => setCreator(data.data));
    }, [poll]);

    if (!poll || !votes) return <div>Loading...</div>;

    const votesFor = votes.filter(v => v.choice).length;
    const votesAgainst = votes.filter(v => !v.choice).length;

    const data = {
        labels: ["Votes"],
        datasets: [
            {
                label: ['For'],
                data: [votesFor],
                backgroundColor: ['rgba(75, 192, 192, 0.6)'],
            },
            {
                label: ['Against'],
                data: [votesAgainst],
                backgroundColor: ['rgba(255, 99, 132, 0.5)'],
            }
        ],
    };

    return (
        <div className='container'>
            <h1>{poll.question}</h1>
            <p>{poll.description}</p>
            <p>Created by: {creator?.username}</p>
            <h2>Results</h2>
            <p><strong>Total Votes:</strong> {votes.length}</p>
            <p><strong>Time Left:</strong> <Countdown endTime={poll.endTime}></Countdown></p>
            <Bar data={data} />
        </div>
    );
}

export default PollResultsPage;