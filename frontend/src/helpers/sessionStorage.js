export const sessionStorageService = {
    setItem: (key, value) => {
        try {
            sessionStorage.setItem(key, JSON.stringify(value));
        } catch (error) {
            console.error('Error setting item in session storage:', error);
        }
    },
    getItem: (key) => {
        try {
            const item = sessionStorage.getItem(key);
            return item !== "undefined" ? JSON.parse(item) : null;
        } catch (error) {
            console.error('Error getting item from session storage:', error);
            return null;
        }
    },
    removeItem: (key) => {
        sessionStorage.removeItem(key);
    },
    clear: () => {
        sessionStorage.clear();
    },
    login: (user) => {
        sessionStorageService.setUser(user);
    },
    logout: () => {
        sessionStorageService.clear();
        window.location.reload();
        window.location.href = '/';
    },
    setUserId: (userId) => {
        sessionStorageService.setItem('userId', userId);
    },
    setUser: (user) => {
        sessionStorageService.setItem('user', user);
        console.log('user', user);
        if (user && user.id) {
            sessionStorageService.setUserId(user.id);
        }
    },
    getToken: function() {
        return this.getItem('token');
    },

    setToken: function(token) {
        this.setItem('token', token);
    },
    getTempId: () => {
        const tempId = sessionStorageService.getItem('tempId');
        if (!tempId) {
            const newTempId = Date.now();
            sessionStorageService.setItem('tempId', newTempId);
            return newTempId;
        }
        return tempId;
    },
};

export const isUserLoggedIn = () => {
    const user = sessionStorageService.getUser();
    const token = sessionStorageService.getToken();
    return !!user && !!token;
}

export const getUserId = () => {
    const user = getUser()
    return user?.id || null;
}

export const getUser = () => {
    return JSON.parse(sessionStorageService.getItem('user'));
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
