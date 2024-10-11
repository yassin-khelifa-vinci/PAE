import { register } from '../../utils/backendRequest';
import { setAuthenticatedUser } from '../../utils/auths';
import { clearPage } from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';
import showAlert from '../../utils/alert';

let isRoleSelected = false;

const RegisterPage = async () => {
  clearPage();
  renderRegisterForm();
};

async function renderRegisterForm() {
  const main = document.querySelector('main');
  main.innerHTML += `
  <div class="container d-flex align-items-center justify-content-center mainContainer">
    <div class="col-4 mx-auto">
    <h2 class="text-center">Bienvenue</h2>
      <form id="formulaire">
        <div class="row">
          <div class="col">
            <div class="form-group mt-2">
                <label>Nom</label>
                <input type="text" class="form-control" id="inputLastName" placeholder="Entrez votre nom" required>
            </div>
          </div>
          <div class="col">
              <div class="form-group mt-2">
                <label>Prénom</label>
                <input type="text" class="form-control" id="inputFirstName" placeholder="Entrez votre prénom" required>
            </div>
          </div>
        </div>
        <div class="form-group mt-2">
            <label>Numéro de téléphone</label>
            <input type="tel" class="form-control" id="inputPhoneNumber" placeholder="Entrez votre numéro de téléphone" required>
        </div>
        <div class="form-group mt-2">
          <label for="exampleInputEmail1">Email</label>
          <input type="email" class="form-control" id="inputEmail" placeholder="Entrez votre email" required>
        </div>
        <div class="form-group mt-2" id="roleSelection" style="display: none">
          <label for="role">Choisissez votre rôle :</label>
          <select name="role" class="form-select" id="role">
            <option value="" disabled selected>Choisissez un rôle</option>
            <option value="ADMINISTRATIVE">Administratif</option>
            <option value="TEACHER">Professeur</option>
          </select>
        </div>
        <div class="form-group mt-2">
          <label for="exampleInputPassword1">Mot de passe</label>
          <input type="password" class="form-control" id="inputPassword" placeholder="Entrez votre mot de passe" required>
        </div>
        <div class="form-group mt-2">
          <label for="exampleInputPassword1">Confirmation mot de passe</label>
          <input type="password" class="form-control" id="inputConfirmPassword" placeholder="Confirmez votre mot de passe" required>
        </div>
        <button type="submit" class="btn btn-success mt-3 w-100">S'inscrire</button>
      </form>
    </div>
  </div>`;

  const registerForm = document.querySelector('form');
  registerForm.addEventListener('submit', onRegister);
  registerForm.addEventListener('input', handleRoleDisplay);
}

async function onRegister(e) {
  e.preventDefault();

  if (!validateForm()) return;

  const firstName = document.getElementById('inputFirstName').value;
  const lastName = document.getElementById('inputLastName').value;
  const phoneNumber = document.getElementById('inputPhoneNumber').value;
  const email = document.getElementById('inputEmail').value;
  const password = document.getElementById('inputPassword').value;
  const role = isRoleSelected ? document.getElementById('role').value : 'STUDENT';

  try {
    const response = await register(email, password, firstName, lastName, phoneNumber, role);
    setAuthenticatedUser(response);

    Navbar();
    Navigate('/');
  } catch (error) {
    if (error.message.includes('409')) showAlert('formulaire', 'Email déjà utilisé');
    if (error.message.includes('400')) showAlert('formulaire', "Erreur lors de l'inscription");
  }
}

function validateForm() {
  const firstName = document.getElementById('inputFirstName').value.trim(); // Utilisation de trim pour enlever les espaces inutiles
  const lastName = document.getElementById('inputLastName').value.trim();
  const phoneNumber = document.getElementById('inputPhoneNumber').value.trim();
  const email = document.getElementById('inputEmail').value.trim();
  const password = document.getElementById('inputPassword').value.trim();
  const confirmPassword = document.getElementById('inputConfirmPassword').value.trim();
  const role = isRoleSelected ? document.getElementById('role').value.trim() : null;

  // Vérification que les champs ne sont pas vides
  if (
    !firstName ||
    !lastName ||
    !phoneNumber ||
    !email ||
    !password ||
    !confirmPassword ||
    (isRoleSelected && !role)
  ) {
    showAlert('formulaire', 'Veuillez remplir tous les champs');
    return false;
  }

  const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*\.[a-zA-Z0-9_+&*-]+@(?:student\.vinci\.be|vinci\.be)$/;
  if (!emailRegex.test(email)) {
    showAlert('formulaire', "Vous n'avez pas entré une adresse email valide");
    return false;
  }

  if (password !== confirmPassword) {
    showAlert('formulaire', 'Les mots de passe ne correspondent pas');
    return false;
  }

  return true;
}

function handleRoleDisplay() {
  const emailInput = document.getElementById('inputEmail');
  const divRoleSelection = document.getElementById('roleSelection');
  const role = document.getElementById('role');

  if (emailInput.value.endsWith('@vinci.be')) {
    divRoleSelection.style.display = 'block';
    role.required = true;
    isRoleSelected = true;
  } else {
    divRoleSelection.style.display = 'none';
    isRoleSelected = false;
    role.required = false;
  }
}

export default RegisterPage;
