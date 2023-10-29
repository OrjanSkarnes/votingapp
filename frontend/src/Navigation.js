import React from 'react';
import { Link } from 'react-router-dom';
import { isUserLoggedIn, logout } from './helpers/sessionStorage';

export const Navigation = () => {
    const isLoggedIn = isUserLoggedIn();
    return (
        <nav className='navigation'>
            <Link to="/" className='link'>Home</Link>
            {isLoggedIn && <Link to="/polls" className='link'>Polls</Link>}
            <Link to="/vote" className='link'>Vote</Link>
            {!isLoggedIn ?  <Link to="/login" className='link'>Login</Link> : <Link to="/" className='link' onClick={() => logout()}>Logout</Link>}
            {/* Add more links as needed */}
        </nav>
    );
}

export default Navigation;
