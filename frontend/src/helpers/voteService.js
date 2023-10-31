import fetchWrapper from './fetchWrapper';
import { getTempId } from './sessionStorage';

class VoteService {
    static async createVote(vote) {
        const tempId = getTempId();
        return fetchWrapper(`/votes?tempId=${tempId}`, 'POST', vote);
    }

    // ... add other vote-related requests as needed
}

export default VoteService;