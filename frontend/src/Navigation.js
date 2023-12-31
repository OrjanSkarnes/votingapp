import React from 'react';
import { Link } from 'react-router-dom';
import { isUserLoggedIn, sessionStorageService } from './helpers/sessionStorage';

export const Navigation = () => {
    const isLoggedIn = isUserLoggedIn();
    return (
        <nav className='navigation'>
            <Link to="/" className='link'>Home</Link>
            {isLoggedIn && <Link to="/polls" className='link'>Polls</Link>}
            {!isLoggedIn ?  <Link to="/login" className='link'>Login</Link> : <Link to="/" className='link' onClick={() => sessionStorageService.logout()}>Logout</Link>}
        </nav>
    );
}

export default Navigation;
