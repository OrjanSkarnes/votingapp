// fetchWrapper.js
const fetchWrapper = async (url, method, payload = null, headers = {}) => {
    const token = localStorage.getItem('jwToken');
    const authHeader = token ? { Authorization: `Bearer ${token}` } : {};
    const defaultHeaders = {
        'Content-Type': 'application/json',
        ...authHeader,
    };
  
    const requestOptions = {
      method,
      headers: { ...defaultHeaders, ...headers },
    };
  
    if (payload) {
      requestOptions.body = JSON.stringify(payload);
    }
  
    try {
      const response = await fetch("http://localhost:8080/api" + url, requestOptions)
      
      const contentType = response.headers.get("content-type");

      let data;
      if (contentType && contentType.includes("application/json")) {
        data = await response.json();
      } else {
        data = await response.text();
      }
  
      if (response.ok) {
        return Promise.resolve(data);
      } else {
        const error = new Error('An error occurred');
        error.status = response.status;
        error.data = data;
        return Promise.reject(error);
      }
    } catch (error) {
      return Promise.reject(error);
    }
  };
  

export default fetchWrapper;

export const getUser = () => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user;
}