// LoginPage.js: Handles the rendering and functionality of the login page.

import { setAuthenticatedUser, setRememberMe } from '../../utils/auths';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';
import { clearPage } from '../../utils/render';
import { login } from '../../utils/backendRequest';
import showAlert from '../../utils/alert';

// Main function to initialize the login page by clearing the existing page and rendering the login form.
const LoginPage = async () => {
  clearPage();
  await renderLoginForm();
};

// Configuration object for form inputs to manage the input fields for email and password, and the remember me checkbox.
const formConfig = {
  email: {
    id: 'inputEmail',
    label: 'Email',
    type: 'email',
    placeholder: 'Entrez votre email',
    errorMessage: 'Please choose a username.',
  },
  password: {
    id: 'inputPassword',
    label: 'Mot de passe',
    type: 'password',
    placeholder: 'Entrez votre mot de passe',
  },
  rememberMe: {
    id: 'exampleCheck1',
    label: 'Se souvenir de moi',
  },
};

// Function to render the login form on the main content area of the page.
async function renderLoginForm() {
  const main = document.querySelector('main');
  main.innerHTML = getFormHTML();

  // Attaches event listeners to the checkbox and form submission.
  document.getElementById(formConfig.rememberMe.id).addEventListener('click', onCheckboxClicked);
  document.querySelector('form').addEventListener('submit', onLogin);
}

// Helper function to generate HTML for the login form using configurations defined in `formConfig`.
function getFormHTML() {
  return `
    <div class="container d-flex align-items-center justify-content-center mainContainer">
      <div class="col-4 mx-auto">
        <h2 class="text-center">Heureux de vous revoir !</h2>
        <form id="formulaire">
          ${getInputHTML(formConfig.email)}
          ${getInputHTML(formConfig.password)}
          <div class="form-check mt-3">
            <input type="checkbox" class="switch_1" id="${formConfig.rememberMe.id}">
            <label class="form-check-label" for="${formConfig.rememberMe.id}">${
    formConfig.rememberMe.label
  }</label>
          </div>
          <button type="submit" class="btn btn-success mt-3 w-100">Se connecter</button>
        </form>
      </div>
    </div>`;
}

// Generates individual input field HTML based on configuration passed, including any associated error messages.
function getInputHTML({ id, label, type, placeholder, errorMessage }) {
  return `
    <div class="form-group mt-3">
      <label for="${id}">${label}</label>
      <input type="${type}" class="form-control" id="${id}" placeholder="${placeholder}" required>
      ${errorMessage ? `<div class="invalid-feedback">${errorMessage}</div>` : ''}
    </div>`;
}

// Event handler for the 'Remember Me' checkbox to store the preference in local storage.
function onCheckboxClicked(e) {
  setRememberMe(e.target.checked);
}

// Event handler for form submission to perform user authentication via backend request.
async function onLogin(e) {
  e.preventDefault();
  const email = document.getElementById(formConfig.email.id).value;
  const password = document.getElementById(formConfig.password.id).value;

  try {
    const authenticatedUser = await login(email, password); // Attempts to authenticate the user.
    setAuthenticatedUser(authenticatedUser); // Stores the authenticated user's information on successful login.
    Navbar(); // Updates the navigation bar to reflect the user's logged-in state.
    Navigate('/'); // Redirects to the homepage upon successful login.
  } catch (error) {
    // Displays an alert if authentication fails due to invalid credentials.
    if (error.message.includes('401') || error.message.includes('400'))
      showAlert('formulaire', 'Email ou mot de passe incorrect');
  }
}

export default LoginPage;
