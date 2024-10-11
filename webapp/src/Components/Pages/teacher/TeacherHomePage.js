import Navigate from '../../Router/Navigate';
import userLogo from '../../../img/icon/icons8-utilisateur-96.png';
import statsLogo from '../../../img/icon/icons8-système-erp-48.png';
import usersLogo from '../../../img/icon/icons8-groupe-d&#39;utilisateurs-64.png';
import responsablesLogo from '../../../img/icon/icons8-homme-daffaire-60.png';
import { getAuthenticatedUser } from '../../../utils/auths';

const main = document.querySelector('main');

const Teacher = () => {
  const user = getAuthenticatedUser();
  main.innerHTML = `
    <section class="homePageContainer">
        <div class="container">
            <h1 id="bienvenue">Bienvenue</h1>
        </div>
        <div id="card_wrapper">
            <div class="card dashboardCard p-3" id="card1" data-attribute="/settings">
                <img src=${userLogo} id="logoImage" alt="user">
                <div class="card_load">
                    <div class="card_load_extreme_title fw-medium">${user.lastName} ${user.firstName}</div>
                    <div class="fs-6">Modifier mes données personnelles</div>
                </div> 
            </div>
            <div class="card dashboardCard p-3" id="card2" data-attribute="/teacher/dashboard">
                <img src=${statsLogo} id="logoImage" alt="stats" class="card_load">
                <div class="card_load">
                    <div class="card_load_extreme_title fw-medium">Dashboard Professeur</div>
                </div>   
            </div>
            <div class="card dashboardCard p-3" id="card4" data-attribute="/search">
                <img src=${usersLogo} id="logoImage" alt="user" class="card_load">
                <div class="card_load_extreme_title fw-medium">Voir la liste des utilisateurs</div>
            </div>
            <div class="card dashboardCard p-3" id="card5" data-attribute="/responsable">
                <img src=${responsablesLogo} id="logoImage" alt="responsable" class="card_load">
                <div class="card_load_extreme_title fw-medium">Voir la liste des responsables de stage</div>
            </div>
        </div>
    </section>`;

  const cards = document.querySelectorAll('.dashboardCard');
  cards.forEach((card) => {
    card.addEventListener('click', () => {
      Navigate(`${card.dataset.attribute}`);
    });
  });
};

export default Teacher;
