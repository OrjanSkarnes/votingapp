import fetchWrapper from './fetchWrapper';
import { sessionStorageService } from './sessionStorage';

const getPollById = async (id) => {
    return fetchWrapper(`/polls/${id}`, 'GET');
};

const getAllPolls = async () => {
    return fetchWrapper('/polls', 'GET');
};

const getAllPublicPolls = async () => {
    return fetchWrapper('/polls/public', 'GET');
};

const createPoll = async (poll) => {
    return fetchWrapper('/polls', 'POST', poll);
};

const getPollsByUser = async (userId) => {
    return fetchWrapper(`/polls/user/${userId || ' '}/created`, 'GET');
};

const getPollsVotedOnByUser = async (userId) => {
    return fetchWrapper(`/polls/user/${userId || ' '}/votes${!userId ? "?tempId=" + sessionStorageService.getTempId() : ''}`, 'GET');
};

const deletePoll = async (id) => {
    return fetchWrapper(`/polls/${id}`, 'DELETE');
};

const finishPoll = async (id) => {
    return fetchWrapper(`/polls/${id}/finish`, 'PUT');
};

const editPoll = async (id, poll) => {
    return fetchWrapper(`/polls/${id}`, 'PUT', poll);
};

const getPollVotes = async (id) => {
    return fetchWrapper(`/polls/${id}/votes`, 'GET');
};

const getPollResults = async (id) => {
    return fetchWrapper(`/polls/${id}/result`, 'GET');
};

export {
    getPollById,
    getAllPolls,
    getAllPublicPolls,
    createPoll,
    getPollsByUser,
    getPollsVotedOnByUser,
    deletePoll,
    editPoll,
    getPollVotes,
    getPollResults,
    finishPoll
};
