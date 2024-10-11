// Logout.js: Manages the logout process, refreshes the navigation bar, and redirects to the login page.

import { clearAuthenticatedUser } from '../../utils/auths';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';

const Logout = () => {
  // Clear authentication details from the client's local storage or session storage.
  clearAuthenticatedUser();

  // Reinitialize or refresh the navigation bar to reflect the user's logged-out state.
  Navbar();

  // Defer the navigation to the login page to ensure all synchronous operations complete.
  setTimeout(() => {
    Navigate('/login');
  }, 0);
};

export default Logout;
