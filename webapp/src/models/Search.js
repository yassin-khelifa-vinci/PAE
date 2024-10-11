const { getStatusClass } = require('./Enterprise');
const { getRoleName, getStatusName } = require('../utils/Util');
const { getStage } = require('../utils/backendRequest');

function renderButton(role) {
  // If a status is in the initiated state, we display a button to accept or unfollow
  if (role === 'ADMINISTRATIVE')
    return `<span class="badge rounded-pill text-bg-primary" style="--bs-bg-opacity: .7;">${getRoleName(
      role,
    )}</span>`;

  // If a status is in the taken state, we display a button to refuse, accept, suspend or unfollow
  if (role === 'TEACHER')
    return `<span class="badge rounded-pill bg-success" style="--bs-bg-opacity: .7;">${getRoleName(
      role,
    )}</span>`;
  return `<span class="badge rounded-pill bg-secondary "style="--bs-bg-opacity: .7;">${getRoleName(
    role,
  )}</span>`;
}

function showModalInfo(id) {
  const modalInformation = document.querySelector(id);
  if (!modalInformation) {
    return;
  }

  modalInformation.innerHTML = `
    <div class="modal fade" id="infoModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-keyboard="true">
        <div class="modal-dialog">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Information de l'utilisateur</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form>
                    </form>
                </div>
            </div>
        </div>
    </div>`;
}

function updateModalInfo(user) {
  const modalInformation = document.querySelector('#infoModal .modal-body form');
  if (!modalInformation)
    throw new Error('Le formulaire du modal "infoModal" est introuvable dans le DOM.');

  modalInformation.innerHTML = `
        <div class="input-style">
            <label for="exampleFormControlInput1" class="form-label">Nom</label>
            <input type="text" placeholder="${user?.lastName}" readonly/>
        </div>
        <div class="input-style">
            <label for="exampleFormControlInput1" class="form-label">Prénom</label>
            <input type="text" placeholder="${user?.firstName}" readonly/>
        </div>
        <div class="input-style">
            <label for="exampleFormControlInput1" class="form-label">Email</label>
            <input type="email" placeholder="${user?.email}" readonly/>
        </div>`;
}

function showModalContact(id) {
  const modalContact = document.querySelector(id);
  if (!modalContact) return;

  modalContact.innerHTML = `
    <div class="modal fade" id="contactModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-keyboard="true">
      <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-body p-4">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h3>Contacts de l'utilisateur</h3>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="list-group">
                </div>
            </div>
        </div>
      </div>
    </div>`;
}

async function updateModalContact(contacts) {
  const modalContact = document.querySelector('#contactModal .modal-body .list-group');
  if (!modalContact) {
    return;
  }

  const respInfo = async (id) => {
    const stageInfo = await getStage(id);
    if (!stageInfo) return '';

    return `
    <h6 class="mb-1">Informations sur le stage</h6>
    <div class="input-group mx-2 mb-2">
      <div class="form-floating">
        <input type="text" class="form-control" id="floatingInputGroup1" value="${stageInfo.internshipProject || 'Aucun sujet de stage'}" disabled readonly>
        <label for="floatingInputGroup1">Sujet du stage</label>
      </div>
      <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
          Voir le responsable
        </button>
    </div>
    <div class="collapse" id="collapseExample">
      <div class="card">
        <div class="card-body">                  
            <h5 class="card-title">${stageInfo.internshipSupervisorDTO.firstName} ${stageInfo.internshipSupervisorDTO.lastName}</h5>
            
            <div class="row g-3 align-items-center">
                <div class="col-auto">
                    <label for="inputPassword6" class="col-form-label">Email:</label>
                </div>
                <div class="col">
                    <input type="email" id="inputPassword6" class="form-control form-control-sm" placeholder="${stageInfo.internshipSupervisorDTO.email || ''}" disabled readonly>
                </div>
            </div>
            <div class="row g-3 align-items-center">
                <div class="col-auto">
                    <label for="inputPassword6" class="col-form-label">Téléphone:</label>
                </div>
                <div class="col">
                    <input type="tel" id="inputPassword6" class="form-control form-control-sm" placeholder="${stageInfo.internshipSupervisorDTO.phoneNumber}" disabled readonly>
                </div>
            </div>
        </div>
      </div>
    </div>`;
  };

  const contactsListHTML = await Promise.all(contacts.map(async (contact) => {
    let respInfoHTML = '';
    if (contact.contactStatus === 'ACCEPTED') {
        respInfoHTML = await respInfo(contact.userId);
    }
    return `
      <a class="list-group-item list-group-item-action">
        <div class="d-flex w-100 justify-content-between mb-1">
            <div>
                <p class="fs-5 fw-semibold mb-1 d-inline-block">${contact.enterpriseDTO.tradeName} ${contact.enterpriseDTO.designation ? `- ${contact.enterpriseDTO.designation}` : ''}</p>
                <span class="badge status-btn ${getStatusClass(contact?.contactStatus)}">${getStatusName(contact?.contactStatus)}</span>
            </div>
            <p>Année académique: <small class="text-body-secondary">${contact.schoolYear}</small></p>
        </div>
        <div class="px-1">
          <h6 class="mb-1">
            Adresse 
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="green" class="bi bi-geo-alt-fill" viewBox="0 0 16 16"><path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10m0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6"/></svg> 
          </h6>
          <div class="mb-2 px-2">
            <p class="mb-1 text-muted">${contact.enterpriseDTO.street} ${contact.enterpriseDTO.streetNumber}, ${contact.enterpriseDTO.postalCode} ${contact.enterpriseDTO.city}</p>
            <p class="mb-1 text-muted">${contact.enterpriseDTO.phoneNumber}</p>
            <p class="mb-1 text-muted">${contact.enterpriseDTO.email ?? ''}</p>
          </div>
          ${respInfoHTML}
          <h6 class="border-top my-1 pt-2">Information sur le contact</h6>
          <div class="text-muted fs-6 d-flex justify-content-between mb-2 px-2">
            <p class="mb-1">Lieux de rencontre: ${contact.meetingPlace ? `<span class="text-warning">${contact.meetingPlace}</span>` : '<span class="text-dark">Non spécifié</span>'}</p>
            <p class="mb-1">Raison de refus: ${contact.reasonForRefusal ? `<span class="text-danger">${contact.reasonForRefusal}</span>` : '<span class="text-dark">Non spécifié</span>'}</p>
          </div>
        </div>
      </a>`;
  }));

  if (contactsListHTML.length === 0) {
    modalContact.innerHTML = `<h4 class="text-center text-muted">Aucun résultat trouvé</h4>`;
    return;
  }


  modalContact.innerHTML = contactsListHTML.join('');
}

module.exports = {
  renderButton,
  showModalInfo,
  updateModalInfo,
  showModalContact,
  updateModalContact,
};
