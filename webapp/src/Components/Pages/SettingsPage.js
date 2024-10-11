import { clearPage } from '../../utils/render';
import { getAuthenticatedUser, refreshUser } from '../../utils/auths';
import { getRoleName } from '../../utils/Util';
import { editData } from '../../utils/backendRequest';
import showAlert from '../../utils/alert';

const main = document.querySelector('main');

const SettingsPage = () => {
  clearPage();
  const userInfo = getAuthenticatedUser();
  renderSettings(userInfo);
};

function renderSettings(user) {
  main.innerHTML += `
    <div class="container vh-85">
        <div class="my-3">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Mon profil <span class="badge text-bg-success">${getRoleName(
                  user.role,
                )}</span></h3>
                <div class="alert alert-success">Compte créé le ${user?.registrationDate}</div>
            </div>
            <span>Modifiez vos informations personnelles</span>
            <span><span style="color: blue; margin-left: 10%">*</span> Champs non modifiables</span>
        </div>
        <form id="user-form">
            <div class="row">
                <div class="col-md-6">
                    <div class="input-style-1">
                        <label>Nom <span style="color: blue">*</span></label>
                        <input type="text" placeholder="${
                          user?.lastName
                        }" id="inputLastname" disabled readonly/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="input-style-1">
                        <label>Prénom <span style="color: blue">*</span></label>
                        <input type="text" placeholder="${
                          user?.firstName
                        }" id="inputFirstname" disabled readonly/>
                    </div>
                </div>
                <div class="col-12">
                    <div class="input-style-1 mt-2">
                        <label>Email <span style="color: blue">*</span></label>
                        <input type="email" placeholder="${
                          user?.email
                        }" id="inputEmail" disabled readonly/>
                    </div>
                </div>
                <div class="col-12">
                    <div class="input-style-1 mt-2">
                        <label>Numero de téléphone</label>
                        <input type="text" placeholder="${
                          user?.phoneNumber
                        }" id="inputPhoneNumber"/>
                    </div>
                </div>
                <div class="col-12">
                    <div class="input-style-1 mt-2">
                        <label>Mot de passe actuel (À remplir pour chaque modification)</label>
                        <input type="password" placeholder="***********" id="inputLastPassword-1" autocomplete="current-password"/>
                    </div>
                </div>
                <div class="col-12">
                    <div class="input-style-1 mt-2">
                        <label>Nouveau mot de passe</label>
                        <input type="password" placeholder="***********" id="inputLastPassword-2" autocomplete="new-password"/>
                    </div>
                </div>
                <div class="col-12">
                    <div class="input-style-1 mt-2">
                        <label>Confirmer nouveau mot de passe</label>
                        <input type="password" placeholder="***********" id="inputNewPassword" autocomplete="new-password"/>
                    </div>
                </div>
                <div class="d-flex justify-content-center">
                    <button type="submit" class="btn btn-lg btn-success mt-3" disabled>Valider</button>
                </div>
                <div class="toast-container" id="TEstser">
            </div>
        </form>
    </div>`;

  // Add event listener to form
  const form = document.querySelector('form');
  form.addEventListener('submit', async (e) => {
    e.preventDefault(); // Prévient le comportement par défaut du formulaire (soumission/rechargement de la page)

    // Récupération des valeurs depuis les champs corrects
    const oldPassword = document.getElementById('inputLastPassword-1').value; // L'ancien mot de passe
    const phoneNumber = document.getElementById('inputPhoneNumber').value;
    const newPassword = document.getElementById('inputLastPassword-2').value; // Le nouveau mot de passe

    const body = {};
    if (phoneNumber.trim() !== '') {
      body.phoneNumber = phoneNumber;
    }
    if (oldPassword.trim() !== '') {
      body.currentPassword = oldPassword;
    }
    if (newPassword.trim() !== '') {
      body.newPassword = newPassword;
    }
    body.versionNumber = user.versionNumber;
    try {
      const response = await editData(body)
      refreshUser(response);
      SettingsPage();
      showAlert('user-form', 'Vos informations ont été mises à jour avec succès.');
    } catch (error) {
      showAlert('user-form', 'Mot de passe incorrect');
    }
  });

  // Add event listener to inputs
  const inputs = form.querySelectorAll('input');
  const button = form.querySelector('button');

  inputs.forEach((currentInput) => {
    currentInput.addEventListener('input', () => {
      const oldPassword = document.getElementById('inputLastPassword-1').value;
      const newPassword = document.getElementById('inputLastPassword-2').value;
      const confirmPassword = document.getElementById('inputNewPassword').value;
      const phoneNumber = document.getElementById('inputPhoneNumber').value;

      // Vérifie si les champs requis sont remplis
      const isFirstComboFilled = phoneNumber !== '' && oldPassword !== '';
      const isSecondComboFilled =
        oldPassword !== '' && newPassword !== '' && confirmPassword !== '';
      const areAllFieldsFilled = isFirstComboFilled && isSecondComboFilled;

      // Vérifie si les mots de passe correspondent
      const passwordsMatch = newPassword === confirmPassword;

      // Vérifie la validité du numéro de téléphone
      const phoneNumberIsValid = /^\d+$/.test(phoneNumber) || phoneNumber === '';

      // Active ou désactive le bouton
      if(isFirstComboFilled && phoneNumberIsValid){
        button.disabled = false;
      }else if(isSecondComboFilled && passwordsMatch){
        button.disabled = false;
      }else if(areAllFieldsFilled && passwordsMatch && phoneNumberIsValid){
        button.disabled = false;
      }else{
        button.disabled = true;
      }
    });
  });
}

export default SettingsPage;
