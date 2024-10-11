// import Navbar from "../Components/Navbar/Navbar";
import Navigate from '../Components/Router/Navigate';

const { refreshUser, isAuthenticated } = require('./auths');

/**
 * Check if the token is still valid by sending a request to the server
 */
async function checkTokenValidity() {
  if (isAuthenticated()) {
    // get the token from the authenticated user
    const token = sessionStorage.getItem('token') || localStorage.getItem('token');
    try {
      // send a request to the server to check if the token is still valid
      const response = await fetch(`${process.env.API_BASE_URL}/auths/refresh`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${token}`,
        },
      });

      if (!response.ok) {
        // if the response is not ok, token is not valid anymore
        Navigate('/logout');
      } else {
        const user = await response.json();
        refreshUser(user);
      }
    } catch (error) {
      Navigate('/');
    }
  }
}

export default checkTokenValidity;
