import Chart from 'chart.js/auto';
import { clearPage } from '../../../utils/render';
import {
  sortEnterprises,
  showContactList,
  showModalBlacklist,
  updateContactList,
  recepBlacklistReason,
} from '../../../models/Enterprise';
import { getRoleName } from '../../../utils/Util';
import { showLoader, hideLoader } from '../../../utils/loader';
import { getAuthenticatedUser } from '../../../utils/auths';
import {
  getAllEnterprise,
  blacklistEnterprise,
  getStats,
  getEnterprisesStats,
  getEnterpriseContactsList,
} from '../../../utils/backendRequest';
import showAlert from '../../../utils/alert';
import filterImg from '../../../img/icon/filter.svg'
import warningImg from '../../../img/icon/warning.svg'

const main = document.querySelector('main');

let acaYear;

const TeacherDashboardPage = async () => {
  clearPage();
  showLoader();
  const statistiques = await getStats();
  acaYear = Object.keys(statistiques)
    .sort((a, b) => a.localeCompare(b))
    .at(-1);
  renderUsersStats(statistiques);
  renderEnterpriseList(await getAllEnterprise(), statistiques, await getEnterprisesStats());
  hideLoader();
};

function createChart(ctx, userWithInternship, userWithoutInternship) {
  return new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Avec un stage', 'Sans stage'],
      datasets: [
        {
          data: [userWithInternship, userWithoutInternship],
          backgroundColor: ['#219653', 'rgba(151, 202, 49, 0.1)'],
          hoverOffset: 4,
        },
      ],
    },
    options: {
      plugins: {
        legend: { display: false },
      },
    },
  });
}

function renderUsersStats(data) {
  const user = getAuthenticatedUser();
  const sortedYears = Object.keys(data).sort((a, b) => a.localeCompare(b));

  const userWithInternship = data[sortedYears[sortedYears.length - 1]].withStage;
  const userWithoutInternship = data[sortedYears[sortedYears.length - 1]].withoutStage;

  main.innerHTML += `
    <h2 class="text-center mt-4">Tableau de bord <span class="badge bg-success-subtle border-success-subtle text-success-emphasis">${getRoleName(user?.role)}</span></h2>
    <section class="card-style m-4 mb-30">
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h3 class="mb-2">Statistiques des étudiants <span id="statistic-title"></span></h3>
                <p class="text-muted">Nombre total d'étudiant: <span class="fw-bold" id="total-students">${userWithInternship + userWithoutInternship}</span></p>
            </div>
            <div>
                <label for="yearSelect" class="form-label fw-semibold">Selectionner une année académique</label>
                <select class="form-select" id="yearSelect">
                ${sortedYears.map((year, index) => `<option value="${index}"${index === sortedYears.length - 1 ? ' selected' : ''}>${year === 'allYears' ? 'Toutes années confondues' : year}</option>`).join('')}
              </select>
            </div>
        </div>
        <div class="row">
            <div class="container" style="width: 300px; height: 300px">
                <canvas id="myChart"></canvas>
                <div class="donut-inner"><p class="percent"></p></div>
            </div>
        </div>
        <div class="d-flex justify-content-center">
            <p class="me-2">Etudiants sans stage: <span class="badge bg-success-subtle text-success-emphasis" id="studentCount-withoutStage">${userWithoutInternship}</span></p>
            <p>Etudiants avec stage: <span class="badge bg-success" id="studentCount-withStage">${userWithInternship}</span></p>
        </div>
    </section>`;

  const yearSelect = document.getElementById('yearSelect');
  const ctx = document.getElementById('myChart');
  const percentElement = document.querySelector('.percent');
  const studentCountWithStage = document.getElementById('studentCount-withStage');
  const studentCountWithoutStage = document.getElementById('studentCount-withoutStage');
  const totalStudents = document.getElementById('total-students');

  const chart = createChart(ctx, userWithInternship, userWithoutInternship);

  yearSelect.addEventListener('change', () => {
    // Get the data for the selected year
    const yearData = data[sortedYears[yearSelect.value]];

    // Update the data
    chart.data.datasets[0].data = [yearData.withStage, yearData.withoutStage];
    if(yearSelect.value === 'allYears'){
      percentElement.textContent = 'Toutes années confondues';
    }else{
      percentElement.textContent = sortedYears[yearSelect.value];
    }

    studentCountWithStage.textContent = yearData.withStage;
    studentCountWithoutStage.textContent = yearData.withoutStage;
    totalStudents.textContent = yearData.withStage + yearData.withoutStage;

    // Update the chart
    chart.update();
  });
}


// This function initializes the enterprise list view and sets up sorting listeners.
function renderEnterpriseList(list, statistiques, enterpriseCountStudent) {
  setupEnterpriseTable(list, statistiques, enterpriseCountStudent);
  setupSortingListeners(list, enterpriseCountStudent);

}

// Generates the initial table structure and appends it to the main element.
function setupEnterpriseTable(list, statistiques, enterpriseCountStudent) {
  const sortedList = sortEnterprises(list);  // Default sorting key

  // Returns sorted years with the addition of "Toutes années confondues".
  const years = Object.keys(statistiques).sort((a, b) => a.localeCompare(b)).reverse();
  years.filter((year) => year !== 'allYears');
  years.push('Toutes années confondues');


  const oldEnterpriseListSection = document.getElementById('enterprise-list-section');
  if(oldEnterpriseListSection) oldEnterpriseListSection.remove();

  const newSection = document.createElement('section');
  newSection.className = 'card-style m-4 mb-30';
  newSection.id = 'enterprise-list-section';
  newSection.innerHTML = generateTableHTML(years);

  document.querySelector('main').appendChild(newSection);
  renderEnterpriseTableRows(sortedList, enterpriseCountStudent, acaYear);

  showContactList('#modal-contact-list');
  showModalBlacklist('#modal-blacklist');

  addCustomButtonListener();
  addContactListButtonListener();
  addAcadInputListener(list, statistiques, enterpriseCountStudent);
  addBlacklistButtonListener();
}

// Generates the static HTML for the table, leaving the tbody empty to be filled by another function.
function generateTableHTML(years) {
  const filteredYears = years.filter(year => year !== acaYear);

  return `
    <div class="table-container">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <h3 class="mb-2">Liste des entreprises</h3>
        <div>
          <select class="form-select" id="acad-range">
          <option value="${acaYear}" selected>${acaYear}</option>
            ${filteredYears.map((year) => `<option value="${year}">${year}</option>`).join('')}
          </select>
        </div>
      </div>
      <table class="enterprise-table">
        <thead>
          <tr class="bg-white">
            <th>
              <span id="sort-tradeName">Nom <img src="${filterImg}" alt="filter" class="ms-1"></span> 
              <span class="badge bg-success" id="sort-designation">Appelation</span>
            </th>
            <th>Coordonée</th>
            <th id="sort-postalCode">Adresse <img src="${filterImg}" alt="filter" class="ms-1"></th>
            <th id="sort-numberStudents">N° d'étudiant en stage</th>
            <th id="sort-isBlacklisted">Actions <span class="badge bg-warning fw-bold"><img src="${warningImg}" alt="filter"></span> </th>
          </tr>
        </thead>
        <tbody id="enterprise-table-body"></tbody>
        <div id="modal-contact-list"></div>
        <div id="modal-blacklist"></div>
      </table>
      <p class="position-fixed bottom-0 end-0 p-3" id="alert-id"></p>
    </div>`;
}

// Setup sorting listeners for each column.
function setupSortingListeners(list, enterpriseCountStudent) {
  const headers = ['tradeName','designation','postalCode', 'numberStudents','isBlacklisted'];
  const sortOrders = { tradeName: 'asc',designation:'asc', postalCode: 'asc', numberStudents: 'asc', isBlacklisted: 'asc'};
  
  headers.forEach(header => {
    document.getElementById(`sort-${header}`).addEventListener('click', () => {

      // Reverse the sorting order
      sortOrders[header] = sortOrders[header] === 'asc' ? 'desc' : 'asc';

      const sortedList = sortEnterprises(list, header, sortOrders[header]);
      renderEnterpriseTableRows(sortedList, enterpriseCountStudent, acaYear);
    });
  });
}

// Renders rows based on the current list, which can be sorted by various properties.
function renderEnterpriseTableRows(list, enterpriseCountStudent, academicYear) {
  const tbody = document.getElementById('enterprise-table-body');
  tbody.innerHTML = list.map(enterprise => {
    const studentCount = calculateStudentCount(enterpriseCountStudent, enterprise.idEnterprise, academicYear);
    return `
      <tr class="table-row">
        <td>${enterprise.tradeName} ${enterprise.designation ? `<span class="badge bg-success">${enterprise.designation}</span>` : ''}</td>
        <td>${enterprise.email ? `<a href="mailto:${enterprise.email}">${enterprise.email}</a>` : ''} ${enterprise.phoneNumber || ''}</td>
        <td>${enterprise.street} ${enterprise.streetNumber}, <br>${enterprise.postalCode} ${enterprise.city} ${enterprise.country || ''}</td>
        <td>${studentCount}</td>
        <td>
          <div class="d-flex">
            <button class="btn btn-success rounded-pill mx-1 contactList" data-bs-toggle="modal" data-attribute="${enterprise.idEnterprise}" data-bs-target="#contactModal">Liste des contacts</button>
            ${enterprise.isBlacklisted
              ? `<div class="blacklist-buttons" data-attribute="${enterprise.tradeName}" data-reason="${enterprise.blacklistedReason}"><svg xmlns="http://www.w3.org/2000/svg" width="35" height="35" fill="#ffc109" class="bi bi-exclamation-triangle-fill" viewBox="0 0 16 16">
                  <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5m.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2"/>
              </svg></div>`
              : `<button class="custom-button" data-attribute="${enterprise.idEnterprise}" data-attribute-numVersion="${enterprise.versionNumber}" data-bs-toggle="modal" data-bs-target="#blacklistReasonModal">
                  <svg viewBox="0 0 448 512" class="svgIcon"><path d="M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64S14.3 96 32 96H416c17.7 0 32-14.3 32-32s-14.3-32-32-32H320l-7.2-14.3C307.4 6.8 296.3 0 284.2 0H163.8c-12.1 0-23.2 6.8-28.6 17.7zM416 128H32L53.2 467c1.6 25.3 22.6 45 47.9 45H346.9c25.3 0 46.3-19.7 47.9-45L416 128z"></path></svg>
              </button>`
            }
          </div>
        </td>
      </tr>`;
  }).join('');
}

// Helper function to calculate student count for each enterprise based on the selected academic year.
function calculateStudentCount(enterpriseCountStudent, enterpriseId, academicYear) {
  const count = enterpriseCountStudent[enterpriseId] || {};
  return academicYear === 'Toutes années confondues' ?
    Object.values(count).reduce((sum, num) => sum + num, 0) :
    count[academicYear] || 0;
}


function addCustomButtonListener() {
  const customButtons = document.querySelectorAll('.custom-button');
  customButtons.forEach((btn) => {
    if (btn) {
      btn.addEventListener('click', async () => {
        const enterpriseId = btn.getAttribute('data-attribute');
        const versionNumber = btn.getAttribute('data-attribute-numVersion');

        const blacklistReason = await recepBlacklistReason();
        await blacklistEnterprise(enterpriseId, blacklistReason, versionNumber);
        await TeacherDashboardPage();
      });
    }
  });
}

function addContactListButtonListener() {
  const contactListButtons = document.querySelectorAll('.contactList');
  contactListButtons.forEach((btn) => {
    if (btn) {
      btn.addEventListener('click', async () => {
        const enterpriseId = btn.getAttribute('data-attribute');
        const enterpriseContacts = await getEnterpriseContactsList(enterpriseId);

        enterpriseContacts.sort((a, b) => a.userDTO.lastName.localeCompare(b.userDTO.lastName));
        updateContactList(enterpriseContacts);
      });
    }
  });
}

function addAcadInputListener(list, statistiques, enterpriseCountStudent) {
  const acadInput = document.getElementById('acad-range');
  acadInput.addEventListener('change', () => {
    acaYear = acadInput.value;
    renderEnterpriseList(list, statistiques, enterpriseCountStudent);
  });
}

function addBlacklistButtonListener() {
  const blacklistButton = document.querySelectorAll('.blacklist-buttons');
  blacklistButton.forEach((btn) => {
    btn.addEventListener('mouseenter', (e) => {
      e.preventDefault();
      const blacklistReason = btn.getAttribute('data-reason');
      const enterpriseName = btn.getAttribute('data-attribute');

      document.getElementById('alert-id').innerHTML = '';

      showAlert(
        'alert-id',
        `<h6><strong>${enterpriseName}</strong> a été blacklisté pour: </h6>${blacklistReason}`,
      );
      setTimeout(() => {
        document.getElementById('alert-id').innerHTML = '';
      }, 10000);
    });
  });
}

export default TeacherDashboardPage;
