import { v4 as uuidv4 } from 'uuid';

export const sessionStorageService = {
    setItem: (key, value) => {
        sessionStorage.setItem(key, JSON.stringify(value));
    },
    getItem: (key) => {
        const item = sessionStorage.getItem(key);
        return !!item ? JSON.parse(item) : null;
    },
    removeItem: (key) => {
        sessionStorage.removeItem(key);
    },
    clear: () => {
        sessionStorage.clear();
    },
    login: (isLoggedIn) => {
        sessionStorage.setItem('user', isLoggedIn);
    },
    setUserId: (userId) => {
        sessionStorage.setItem('userId', userId);
    },
    setUser: (user) => {
        sessionStorage.setItem('user', user);
        sessionStorageService.setUserId(user.id);
    },
};

export const isUserLoggedIn = () => {
    return !!sessionStorageService.getItem('user');
}

export const getUserId = () => {
    const user = sessionStorageService.getItem('user');
    return user?.id;
}

export const getUser = () => {
    const user = sessionStorageService.getItem('user');
    return user;
}

export const logout = () => {
    sessionStorageService.clear();
    window.location.reload();
    window.location.href = '/';
}

export const getTempId = () => {
    const tempId = sessionStorageService.getItem('tempId');
    if (!tempId) {
        const newTempId = Date.now();
        sessionStorageService.setItem('tempId', newTempId);
    }
    return tempId;
}

export const getUserStatus = () => {
    const user = getUser();
    const tempId = sessionStorageService.getItem('tempId');
    if (user) {
        return 'loggedIn';
    } else if (tempId) {
        return 'anonymous';
    } else {
        return 'notLoggedIn';
    }
}