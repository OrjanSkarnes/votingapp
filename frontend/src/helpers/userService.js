import fetchWrapper from './fetchWrapper';
import { sessionStorageService } from './sessionStorage';

export const login = async (username, password) => {
    return fetchWrapper('/user/login', 'POST', {username, password});
};

export const register = async (user) => {
    const tempId = sessionStorageService.getTempId();
    return fetchWrapper(`/user/register?tempId=${tempId}`, 'POST', user);
};

export const getAllUsers = async () => {
    return fetchWrapper('/user', 'GET');
};

export const getUserById = async (id) => {
    return fetchWrapper(`/user/${id}`, 'GET');
};

// ... add other user-related requests as needed
