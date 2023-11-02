import fetchWrapper from './fetchWrapper';
import { sessionStorageService } from './sessionStorage';

export const createVote = async (vote) => {
    const tempId = sessionStorageService.getTempId();
    return fetchWrapper(`/votes?tempId=${tempId}`, 'POST', vote);
};

// ... add other vote-related requests as needed
