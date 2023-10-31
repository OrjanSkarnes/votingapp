import fetchWrapper from './fetchWrapper';
import { getTempId } from './sessionStorage';

class PollService {
    static async getPollById(id) {
        return fetchWrapper(`/polls/${id}`, 'GET');
    }

    static async getAllPolls() {
        return fetchWrapper('/polls', 'GET');
    }

    static async createPoll(poll) {
        return fetchWrapper('/polls', 'POST', poll);
    }

    static async getPollsVotedOnByUser(userId) {
        return fetchWrapper(`/polls/user/${!!userId ? userId : getTempId()}/votes`, 'GET');
    }

    static async deletePoll(id) {
        return fetchWrapper(`/polls/${id}`, 'DELETE');
    }

    static async editPoll(id, poll) {
        return fetchWrapper(`/polls/${id}`, 'PUT', poll)
    }

    static async getPollVotes(id) {
        
    }
    // ... add other poll-related requests as needed
}

export default PollService;