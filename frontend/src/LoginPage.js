import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { sessionStorageService } from './helpers/sessionStorage';
import { login, register } from './helpers/userService';

export const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    const handleSuccess = (data) => {
        sessionStorageService.login(true);
        sessionStorageService.setUser(JSON.stringify(data));
        navigate('/')
        window.location.reload();
    }

    // Call your Spring REST API to authenticate the user
    // On success, you can redirect to another page or provide feedback to the user
    const handleLogin = async () => {
        setErrorMessage('');

        login(username, password).then(data => {
            handleSuccess(data)
        }).catch((error) => {
            switch (error?.status) {
                case 404:
                    setErrorMessage('User not found')
                    break;
                case 401:
                    setErrorMessage('Invalid password')
                    break;
                default:
                    setErrorMessage('Something went wrong')
                    break;
            }
        });
    }

    const handleCreateAccount = async () => {
        setErrorMessage('');
        register({username, password}).then(data => {
            handleSuccess(data)
        }).catch((error) => {
            console.error(error.status);
            if (error?.status === 409) {
                // Handle 409 error
                setErrorMessage('User already exists')
            }
        });
    }

    const userNameChange = (e) => {
        setUsername(e.target.value);
    }

    const passwordChange = (e) => {
        setPassword(e.target.value);
    }

    return (
        <div className="container login-container">
            <h1>Login</h1>
            <div className="login-form">
                <input type="text" placeholder="Username:" value={username} onChange={userNameChange} />
                <input type="password" placeholder="Password:" value={password} onChange={passwordChange} />
                {errorMessage && <p className='error'>{errorMessage}</p>} 
           
                <div className="button-container">
                    <button onClick={handleCreateAccount}>Create account</button>
                    <button onClick={handleLogin}>Log in</button>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;
