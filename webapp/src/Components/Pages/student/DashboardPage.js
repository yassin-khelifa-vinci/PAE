import { clearPage } from '../../../utils/render';
import { getStatusClass } from '../../../models/Enterprise';
import {
  getLastStage,
  getAllContacts,
  getEnterpriseInfo,
  getAllEnterprise,
  addContact,
  getAllReponsableFromEnterprise,
  createStage,
  createResponsableStage,
  createEnterprise,
  changeInternshipProject,
} from '../../../utils/backendRequest';
import {
  renderButtons,
  showModalEnterpriseForm,
  showModalContact,
  showModalRefusedReason,
  showModalMeetingPlace,
  showModalAccept,
  showModalResponsable,
  showModalInternshipProject,
  updateShowModalAccept,
  getRefusedReason,
  getMeetingPlace,
  getResponsable,
  getStageSubject,
  getStageDate,
  handleRadioButtonChange,
} from '../../../models/Dashboards';
import { showLoader, hideLoader } from '../../../utils/loader';
import penIcon from '../../../img/icon/pen.svg';
import { getStatusName } from '../../../utils/Util';
import showAlert from '../../../utils/alert';
import {
  changeStatusAccepted,
  changeStatusTurnedDown,
  changeStatusAdmitted,
  changeStatusUnsupervised,
} from '../../../utils/requestChangeStatus';

const main = document.querySelector('main');
main.id = 'showError';

let stageInfo;
let allResponsable;
let enterpriseList;

const DashboardPage = async () => {
  clearPage();
  showLoader();
  const all = await getAllContacts();
  stageInfo = await getLastStage();
  allResponsable = await getAllReponsableFromEnterprise();
  enterpriseList = await getAllEnterprise();
  hideLoader();
  await renderStageInfo(stageInfo);
  if (!stageInfo) {
    const userStats = all.statusCount;
    renderStats(userStats.number_started, userStats.number_admitted, userStats.number_turned_down);
  }

  renderTable(all.contacts);
  addEventListeners();
};

async function renderStageInfo(stage) {
  if (stage) {
    const enterpriseInfo = await getEnterpriseInfo(stageInfo.internshipSupervisorDTO.enterprise);

    main.innerHTML += `
        <section class="mx-4">
            <nav class="nav">
                <h2 class="text-success-emphasis">Information de stage</h2>
                <button class="nav-link deactive-btn mx-2" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="false" aria-controls="collapse1">Responsable de stage</button>
            </nav>
            <div class="card bg-success-subtle text-success-emphasis rounded-3 mt-2">
                <div class="card-header text-center border-0 pb-0">
                ${
                  stage.internshipProject
                    ? `<h4>Sujet du stage: <p class="text-secondary">${stage.internshipProject} <a id="edit-stage" data-bs-toggle="modal" data-bs-target="#internshipProject"><img src="${penIcon}"></a></p></h4>`
                    : `<div class="d-flex justify-content-center">
                            <div class="col-md-3">
                                <form class="d-flex">
                                    <input type="text" class="form-control rounded-pill" id="newProjectText" placeholder="Objet du stage" stageID="${stage.id_stage}" required>
                                    <button class="btn btn-success ms-1" type="submit" id="newProject">Ajouter</button>
                                </form>
                            </div>
                        </div>`
                }
                    <small class="card-text text-center">Date de signature: <span class="text-muted">${stage.signatureDate}</span></small>
                </div>
                <!-- Responsable de stage -->
                <div class="card-body bg-success-succes collapse border-top" id="collapse1">
                    <form class="row g-3">
                        <h5>Information du responsable de stage</h5>
                        <div class="col-md-4 mt-0">
                            <label class="form-label mb-0">Nom</label>
                            <input type="text" class="form-control" value="${stage.internshipSupervisorDTO.firstName}" readonly>
                        </div>
                        <div class="col-md-4 mt-0">
                            <label class="form-label mb-0">Prénom</label>
                            <input type="text" class="form-control" value="${stage.internshipSupervisorDTO.lastName}" readonly>
                        </div>
                        <div class="col-md-4 mt-0">
                            <label class="form-label mb-0">Entreprise</label>
                            <input type="text" class="form-control" value="${enterpriseInfo.tradeName} ${enterpriseInfo.designation ? `- ${enterpriseInfo.designation}` : ''}" readonly>                        </div>
                        <div class="col-md-6">
                            <label class="form-label mb-0">Mail</label>
                            <div class="input-group">
                                <span class="input-group-text" id="basic-addon1">@</span>
                                <input type="text" class="form-control" value="${stage.internshipSupervisorDTO.email}" readonly>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label mb-0">Téléphone</label>
                            <div class="input-group">
                                <span class="input-group-text">+32</span>
                                <input type="text" class="form-control" value="${stage.internshipSupervisorDTO.phoneNumber}" readonly>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div id="internshipProject-modal"></div>
        </section>`;
    showModalInternshipProject('#internshipProject-modal');
  } else {
    main.innerHTML += `
        <section class="mx-4">
            <div class="d-flex justify-content-end my-3">
                <div>
                    <button type="button" class="btn btn-success rounded-pill fs-5" data-bs-toggle="modal" data-bs-target="#contactModal">Initier contact</button>
                    <div id="contact-modal"></div>
                    <div id="enterprise-modal"></div>
                    <div id="refusedReason-modal"></div>
                    <div id="meetingPlace-modal"></div>
                    <div id="acceptStage-modal"></div>
                    <div id="addResponsable-modal"></div>
                </div>
            </div>
        </section>`;

    showModalEnterpriseForm('#enterprise-modal');
    showModalContact('#contact-modal', enterpriseList);
    showModalRefusedReason('#refusedReason-modal');
    showModalMeetingPlace('#meetingPlace-modal');
    showModalAccept('#acceptStage-modal');
    showModalResponsable('#addResponsable-modal');
  }
}

function renderStats(initiated, taken, denied) {
  main.innerHTML += `
    <section class="row m-4">
        <div class="col-xl-4 col-lg-3 col-sm-6">
            <div class="icon-card mb-30">
                <h6 class="mb-10">Nombre de contacts initiés</h6>
                <h3 class="text-bold mb-10">${initiated}</h3>
            </div>
        </div>
        <div class="col-xl-4 col-lg-3 col-sm-6">
            <div class="icon-card mb-30">
                <h6 class="mb-10">Nombre de contacts pris</h6>
                <h3 class="text-bold mb-10">${taken}</h3>
            </div>
        </div>
        <div class="col-xl-4 col-lg-3 col-sm-6">
            <div class="icon-card mb-30">
                <h6 class="mb-10">Nombre de contacts refusés</h6>
                <h3 class="text-bold mb-10">${denied}</h3>
            </div>
        </div>
    </section>`;
}

function renderTable(list) {
  main.innerHTML += `
    <section class="card-style m-4 mb-30">
    <h2 class="mb-2">Liste des contacts</h2>
    <div class="table-container">
        <table class="table enterprise-table">
        <thead>
            <tr>
                <th class="enterprise-name"><h6>Entreprise</h6></th>
                <th class="coordonates"><h6>Coordonées</h6></th>
                <th><h6>Adresse</h6></th>
                <th class="contactState"><h6>État contact</h6></th>
                ${!stageInfo ? '<th class="actionth"><h6>Action</h6></th>' : ''}
            </tr>
        </thead>
        <!-- table body -->
        <tbody>
          ${list.map((enterprise) => `
            <tr>
                <td>
                    ${enterprise.enterpriseDTO.tradeName}
                    ${enterprise.enterpriseDTO.designation ? `<span class="badge bg-success" style="--bs-bg-opacity: .8;">${enterprise.enterpriseDTO.designation}</span>`: ''}
                    ${enterprise.enterpriseDTO.isBlacklisted ? '<span class="badge bg-danger">Blacklisté</span>': ''}
                </td>                        
                <td>
                    ${enterprise.enterpriseDTO.email
                        ? `<a href="mailto:${enterprise.enterpriseDTO.email}">${enterprise.enterpriseDTO.email}</a><br>`
                        : ''
                    }
                    ${enterprise.enterpriseDTO.phoneNumber
                        ? `<a href="tel:${enterprise.enterpriseDTO.phoneNumber}">${enterprise.enterpriseDTO.phoneNumber}</a>`
                        : ''
                    }
                </td>
                <td>
                  <p>
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="green" class="bi bi-geo-alt-fill" viewBox="0 0 16 16"><path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10m0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6"/></svg> 
                    ${enterprise.enterpriseDTO.street} ${enterprise.enterpriseDTO.streetNumber}, 
                    ${enterprise.enterpriseDTO.city} ${enterprise.enterpriseDTO.postalCode}
                  </p>
                </td>
                <td>
                  <span class="d-flex justify-content-center status-btn ${getStatusClass(enterprise.contactStatus)}">${getStatusName(enterprise.contactStatus)}</span>
                  ${enterprise.contactStatus === 'ADMITTED'
                      ? `<small class="d-flex justify-content-center">${enterprise.meetingPlace}</small>`
                      : ''
                  }
                  ${enterprise.contactStatus === 'TURNED_DOWN'
                      ? `<small class="d-flex justify-content-center">${enterprise.reasonForRefusal}</small>`
                      : ''
                  }
                </td>
                ${!stageInfo
                    ? `<td>
                        <div class="d-flex" data-numVersion="${ enterprise.versionNumber}" data-id="${enterprise.idContact}" data-idEntreprise="${enterprise.enterpriseDTO.idEnterprise}">${renderButtons(enterprise.contactStatus)}</div>
                      </td>`
                    : ''
                }
            </tr>`).join('')}
            </tbody>
        </table>
    </section>`;
}

function addEventListeners() {

  // Add contact to the contact list
  const validButton = document.querySelector('#valid-button');
  if (validButton) {
    validButton.addEventListener('click', async () => {
      const selectedEnterpriseRadio = document.querySelector('input[name="enterprise"]:checked');
      if (selectedEnterpriseRadio) {
        const enterpriseId = selectedEnterpriseRadio.getAttribute('data-attribute');
        try {
          await addContact(enterpriseId);
        } catch (error) {
          if (error.message.includes('409')) showAlert('showError', 'Entreprise déjà contactée');
          return;
        }
        await DashboardPage();
      }
    });
  }

  // undisable the button when a radio button is checked
  const enterpriseRadio = document.querySelectorAll('#addContact');
  enterpriseRadio.forEach((button) => {
    button.addEventListener('change', () => {
      if (button.checked) {
        validButton.disabled = false;
      }
    });
  });

  // Filter the enterprise list by name
  const enterpriseRow = document.querySelectorAll('.enterpriseRow');
  const filterEnterprise = document.querySelector('#filterEnterprise');
  if (filterEnterprise) {
    filterEnterprise.addEventListener('keyup', () => {
      const name = filterEnterprise.value.toUpperCase();

      enterpriseRow.forEach((item) => {
        const textValue = item.innerText;
        const isVisible = textValue.toUpperCase().indexOf(name) > -1;
        const displayValue = isVisible ? 'block' : 'none';
        // eslint-disable-next-line no-param-reassign
        item.style.display = displayValue;
      });
    });
  }

  // Add a new project to the stage
  const newProjectButton = document.querySelector('#newProject');
  if (newProjectButton) {
    newProjectButton.addEventListener('click', async (e) => {
      e.preventDefault();
      const text = document.querySelector('#newProjectText');
      try {
        if (text.value) await changeInternshipProject(text.value, stageInfo.versionNumber);
        DashboardPage();
      } catch (error) {
        showAlert('showError', 'Erreur lors de la modification du sujet');
      }
    });
  }

  let enterpriseId;

  const buttons = document.querySelectorAll('.Btn');
  if (buttons.length === 0) return;
  buttons.forEach((button) => {
    button.addEventListener('click', async () => {
      const action = button.getAttribute('data-action');
      const id = button.parentElement.getAttribute('data-id');
      const versionNumber = button.parentElement.getAttribute('data-numVersion');
      
      enterpriseId = button.parentElement.getAttribute('data-idEntreprise');

      if (action === 'TURNED_DOWN') {
        const refusedReason = await getRefusedReason();
        await changeStatusTurnedDown(id, refusedReason, versionNumber);

      } else if (action === 'ADMITTED') {
        const meetingPlace = await getMeetingPlace();
        await changeStatusAdmitted(id, meetingPlace, versionNumber);

      } else if (action === 'ACCEPTED') {
        updateShowModalAccept(allResponsable, enterpriseId);
        const radioButtons = document.querySelectorAll('.choiceResponsable');

        if (handleRadioButtonChange(radioButtons)) {
          const validButtonResponsable = document.getElementById('valid-buttonResponsable');
          validButtonResponsable.disabled = false;
        }

        const responsable = await getResponsable();
        const subjectStage = await getStageSubject();
        const dateStage = await getStageDate();

        await createStage(subjectStage, responsable, id, dateStage);
        await changeStatusAccepted(id, versionNumber);
      } else if (action === 'UNSUPERVISED') {
        await changeStatusUnsupervised(id, versionNumber);
      }

      await DashboardPage();
    });
  });


  // Add a new responsable to the enterprise
  const responsableForm = document.querySelector('#responsableForm');
  responsableForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const nom = document.getElementById('responsableName').value;
    const prenom = document.getElementById('responsableFirstname').value;
    const telephone = document.getElementById('responsablePhone').value;
    const email = document.getElementById('responsableEmail').value;

    await createResponsableStage(nom, prenom, telephone, email, enterpriseId);

    DashboardPage();
  });


  // Add a new enterprise if the form is submitted
  const form = document.getElementById('enterpriseForm');
  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const dataBody = {};
      const inputs = form.querySelectorAll('input');

      inputs.forEach((input) => {
        if (input.value !== undefined && input.value.trim() !== '')
          dataBody[input.name] = input.value;
      });

      try {
        // We create the enterprise 
        const newEnterprise = await createEnterprise(dataBody);
        // and then we add the contact to the contact list
        await addContact(newEnterprise.idEnterprise);
      } catch (error) {
        if (error.message.includes('409') && error.message.includes('Email'))
          showAlert('enterpriseForm', 'Email déjà utilisé');
        else if (error.message.includes('409') && error.message.includes('Trade'))
          showAlert('enterpriseForm', 'Nom complet et appelation déjà utilisés');
        else if (error.message.includes('400')) showAlert('enterpriseForm', 'Données invalides');
        else if (error.message.includes('500')) showAlert('enterpriseForm', 'Erreur serveur');
        return;
      }
      const closeButton = document.querySelector('#close-enterprise-modal');
      closeButton.click();
      await DashboardPage();
    });
  }
}

export default DashboardPage;
