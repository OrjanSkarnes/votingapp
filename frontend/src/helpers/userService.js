import fetchWrapper from './fetchWrapper';
import { getTempId } from './sessionStorage';

class UserService {
    static async login(username, password) {
        return fetchWrapper('/user/login', 'POST', {username, password});
    }

    static async register(user) {
        const tempId = getTempId();
        return fetchWrapper(`/user?tempId=${tempId}`, 'POST', user);
    }

    static async getAllUsers() {
        return fetchWrapper('/user', 'GET');
    }

    static async getUserById(id) {
        return fetchWrapper(`/user/${id}`, 'GET');
    }

    // ... add other user-related requests as needed
}

export default UserService;