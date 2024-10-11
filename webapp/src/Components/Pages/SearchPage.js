import { clearPage } from '../../utils/render';
import { getAllUsers, getUserContact } from '../../utils/backendRequest';
import {
  renderButton,
  showModalInfo,
  showModalContact,
  updateModalInfo,
  updateModalContact,
} from '../../models/Search';

let main = document.querySelector('main');

const SearchPage = async () => {
  clearPage();

  const userLists = await getAllUsers();
  search(userLists);
  searchResults(userLists);
  setupYearSelectListener(userLists);
  setupStudentFilterListener(userLists);
};

function setupYearSelectListener(userLists) {
  const yearSelect = document.getElementById('yearSelect');
  yearSelect.addEventListener('change', async () => {
    applyFilters(userLists);
  });
}

function setupStudentFilterListener(userLists) {
  const studentOnly = document.getElementById('studentOnly');
  studentOnly.addEventListener('click', () => {
    applyFilters(userLists);
  });
}

async function applyFilters(userLists) {
  const yearSelect = document.getElementById('yearSelect');
  const studentOnly = document.getElementById('studentOnly');
  const selectedYear = yearSelect.value;
  const isStudentOnly = studentOnly.checked;

  let filteredUsers = userLists;

  if (selectedYear) {
    filteredUsers = filteredUsers.filter(user => user.schoolYear === selectedYear);
  }

  if (isStudentOnly) filteredUsers = filteredUsers.filter(user => user.role === 'STUDENT');

  clearResults();
  searchResults(filteredUsers);
}

function clearResults() {
  const resultsSection = main.querySelector('section.m-4');
  if (resultsSection) resultsSection.remove();
}

function search(list) {
  main = document.querySelector('main');
  main.innerHTML += `
    <section id="searchContainer">
      <h2 class="text-center my-4">Rechercher un utilisateur</h2>
      <div class="row">
          <div class="col-md">
          </div>
          <div class="col-md">
            <input id="searchInput" placeholder="Rechercher un utilisateur" type="search" class="input">
            <div class="switch_box box_1 pt-1 ms-2">
              <input type="checkbox" class="switch_1" id="studentOnly">
              <label for="studentOnly">Étudiants uniquement</label>
            </div>
          </div>
          <div class="col-md">
            <div class="form-floating">
              <select class="form-select" id="yearSelect"></select>
              <label for="yearSelect">Année académique</label>
            </div>
          </div>
        </div>
    </section>
    <section id="nicolae">
    </section>
  `;

  populateYearSelect(list);

  const searchInput = document.getElementById('searchInput');
  searchInput.addEventListener('input', () => {
    const searchTerm = searchInput.value.toLowerCase();
    const filteredList = list.filter(
      (user) =>
        user.firstName.toLowerCase().includes(searchTerm) ||
        user.lastName.toLowerCase().includes(searchTerm),
    );
    applyFilters(filteredList);
  });
}

function populateYearSelect(userLists) {
  const yearSelect = document.getElementById('yearSelect');
  if (!yearSelect) return;

  yearSelect.innerHTML = ''; // Effacer les options existantes

  // Ajouter une option pour toutes les années confondues
  yearSelect.add(new Option('Toute année confondue', ''));

  const academicYears = new Set();
  userLists.forEach(user => {
    academicYears.add(user.schoolYear);
  });

  academicYears.forEach(year => {
    yearSelect.add(new Option(year, year));
  });
}


async function searchResults(list) {
  const sortedList = list.sort((a, b) => {
    const rolesOrder = { ADMINISTRATIVE: 1, TEACHER: 2, STUDENT: 3 };
    return rolesOrder[a.role] - rolesOrder[b.role];
  });

  const nicolae = main.querySelector('#nicolae');
  if (!nicolae) {
    console.error('Le conteneur "nicolae" est introuvable.');
    return;
  }


  if (sortedList.length === 0) {
    nicolae.innerHTML = `<section class="m-4"><h2 class="text-center">Aucun résultat trouvé</h2></section>`;
  } else {
    const userCards = await Promise.all(sortedList.map(async (user) => `
        <div class="col-md-3 p-2">
          <div class="card">
            <div class="card-header">Date d'inscription: <span class="text-muted">${user.registrationDate}</span></div>
            <div class="card-body">         
              <h5 class="card-title">${user.firstName} ${user.lastName}<small> ${renderButton(user.role)}</small></h5>
  
              <button type="button" class="btn deactive-btn mb-1 userList" data-bs-toggle="modal" data-bs-target="#infoModal" data-attribute="${user.idUser}">Informations</button>
              ${user.role === 'STUDENT' ? `<button type="button" class="btn deactive-btn mb-1 contactList" data-bs-toggle="modal" data-bs-target="#contactModal" data-attribute="${user.idUser}">Liste des contacts</button>`:''}
            </div>
          </div>
        </div>`));
  
    nicolae.innerHTML = `
      <section class="m-4">
        <div class="row">
          ${userCards.join('')}
        </div>                  
        <div id="modalInformation"></div>
        <div id="modalStage"></div>
        <div id="modalContact"></div>
    </section>`;
  }

  showModalInfo(`#modalInformation`);
  showModalContact('#modalContact');

  addUserInfoButtonListener(list);
  addContactListButtonListener();
}

function addUserInfoButtonListener(list) {
  const contactListButtons = document.querySelectorAll('.userList');
  contactListButtons.forEach((btn) => {
    if (btn) {
      btn.addEventListener('click', async (e) => {
        e.preventDefault();

        const userId = btn.getAttribute('data-attribute');
        const userInfo = list.find((user) => user.idUser === parseInt(userId, 10));

        updateModalInfo(userInfo);
      });
    }
  });
}
function addContactListButtonListener() {
  const contactListButtons = document.querySelectorAll('.contactList');
  contactListButtons.forEach((btn) => {
    if (btn) {
      btn.addEventListener('click', async (e) => {
        e.preventDefault();

        const userId = btn.getAttribute('data-attribute');
        updateModalContact(await getUserContact(userId));
      });
    }
  });
}

export default SearchPage;
