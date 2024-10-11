/**
 * Renders buttons corresponding to the specified state.
 * @param {string} status - The current state.
 * @returns {string} - A string of HTML representing the buttons corresponding to the specified state.
 */
function renderButtons(status) {
  // If a status is in the initiated state, we display a button to accept or unfollow
  if (status === 'STARTED') {
    return `
        <button class="Btn btn-primary me-2" data-action="ADMITTED" data-bs-toggle="modal" data-bs-target="#meetingPlace">
            <div class="sign">
                <svg fill="white" viewBox="0 0 16 16"><path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/></svg>
            </div>
            <div class="text">Pris</div>
        </button>
        <button class="Btn btn-unfollow" data-action="UNSUPERVISED">
            <div class="sign">
                <svg fill="white" viewBox="0 0 16 16"><path d="M13.879 10.414a2.501 2.501 0 0 0-3.465 3.465zm.707.707-3.465 3.465a2.501 2.501 0 0 0 3.465-3.465m-4.56-1.096a3.5 3.5 0 1 1 4.949 4.95 3.5 3.5 0 0 1-4.95-4.95ZM11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m.256 7a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1z"/></svg>
            </div>
            <div class="text">Ne plus suivre</div>
        </button>`;
  }
  // If a status is in the taken state, we display a button to refuse, accept, suspend or unfollow
  if (status === 'ADMITTED') {
    return `
        <button class="Btn btn-success me-2" data-action="ACCEPTED" data-bs-toggle="modal" data-bs-target="#acceptStage">
            <div class="sign">
                <svg fill="white" viewBox="0 0 16 16"><path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/></svg>
            </div>
            <div class="text">Accepter</div>
        </button>
        <button class="Btn btn-denied me-2" data-action="TURNED_DOWN" data-bs-toggle="modal" data-bs-target="#refusedReason">
            <div class="sign">
                <svg fill="white" viewBox="0 0 16 16"><path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/></svg>
            </div>
            <div class="text">Refuser</div>
        </button>

        <button class="Btn btn-unfollow" data-action="UNSUPERVISED">
            <div class="sign">
                <svg fill="white" viewBox="0 0 16 16"><path d="M13.879 10.414a2.501 2.501 0 0 0-3.465 3.465zm.707.707-3.465 3.465a2.501 2.501 0 0 0 3.465-3.465m-4.56-1.096a3.5 3.5 0 1 1 4.949 4.95 3.5 3.5 0 0 1-4.95-4.95ZM11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m.256 7a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1z"/></svg>
            </div>
            <div class="text">Ne plus suivre</div>
        </button>
        `;
  }

  // If a status is in the refused, accepted, suspended or unfollowed state, we no longer display any buttons
  return '';
}

/**
 * Displays a modal to initiate a new contact with a company.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @param {Array} list - The list of companies to display in the modal.
 * @returns {void}
 */
function showModalContact(id, list) {
  const ContactModal = document.querySelector(`${id}`);
  if (!ContactModal) {
    throw new Error(`Element with id: ${id} not found`);
  }
  
  const enterpriseContactList = list.map((enterprise) => {
  const designation = ` <span class="badge bg-success">${enterprise.designation || ''}</span>`;

  return `<li class="list-group-item enterpriseRow">
                <input  id="addContact" class="form-check-input me-1" type="radio" name="enterprise" data-attribute="${enterprise.idEnterprise}">
                ${enterprise.tradeName}${designation}
                ${enterprise.isBlacklisted
                    ? '<span class="badge bg-danger ms-2">Blacklisté</span>'
                    : ''
                }
            </li>`;
  }).join('');

  ContactModal.innerHTML = `
    <div class="modal fade" id="contactModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-body p-4">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h3 class="">Initier un nouveau contact</h3>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <span class="text-muted">Veuillez séléctionner une entreprise</span>
                <div class="enterprise-list">
                    <ul class="list-group mt-1">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <button type="button" data-bs-toggle="modal" class="btn w-100" data-bs-target="#enterpriseModal">Ajouter une entreprise    
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-square" viewBox="0 0 16 16"><path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/><path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/></svg> 
                            </button>
                        </li>
                        <li><input type="search" class="form-control rounded-0" id="filterEnterprise" placeholder="Rechercher" aria-label="Search" aria-describedby="search-addon"/></li>
                        ${enterpriseContactList}
                    </ul>
                    <small class="text-muted">Attention si l'entreprise a été <strong>blacklisté</strong> les professeurs vous conseils de ne plus prendre contact avec celle-ci</small>
                </div>
                <button disabled type="button" id="valid-button" class="btn btn-lg btn-success mt-3 w-100" data-bs-dismiss="modal">Valider</button>
            </div>
        </div>
      </div>
    </div>`;
}

/**
 * Displays a modal to change the subject of an internship project.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @returns {void}
 */
function showModalInternshipProject(id) {
  const internshipProjectModal = document.querySelector(`${id}`);
  if (!internshipProjectModal) {
    throw new Error(`Element with id: ${id} not found`);
  }
  internshipProjectModal.innerHTML = `
    <div class="modal fade" id="internshipProject" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Changer le sujet de stage</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="projectFrom">
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Nouveau sujet</label>
                            <input type="text" id = "newProjectText" placeholder="Sujet" required/>
                        <button type="submit" class="btn btn-lg btn-success mt-3 w-100" id="newProject" data-bs-dismiss="modal">Changer</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    `;
}

/**
 * Displays a modal to add a new company.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @returns {void}
 */
function showModalEnterpriseForm(id) {
  const EnterpriseModal = document.querySelector(`${id}`);
  if (!EnterpriseModal) {
    throw new Error(`Element with id: ${id} not found`);
  }
  EnterpriseModal.innerHTML = `
    <div class="modal fade" id="enterpriseModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Ajouter une nouvelle entreprise</h3>
                        <h5 class="text-muted"><span style="color: red">*</span> Champs obligatoires</h5>
                        <button type="button" id="close-enterprise-modal" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="enterpriseForm">
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Nom Complet<span style="color: red">*</span></label>
                            <input type="text" name="tradeName" placeholder="Nom Complet" required/>
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Appelation</label>
                            <input type="text" name="designation" placeholder="Appelation"/>
                        </div>
                        <div class="row">
                            <div class="col">
                                <div class="input-style">
                                <label for="exampleFormControlInput1" class="form-label">Rue<span style="color: red">*</span></label>
                                <input type="text" name="street" placeholder="Rue" required/>
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-style">
                                    <label for="exampleFormControlInput1" class="form-label">Numéro<span style="color: red">*</span></label>
                                    <input type="text" name="streetNumber" placeholder="Numéro" required/>
                                </div>
                        </div>
                        </div>
                        <div class="row">
                            <div class="col">
                            <div class="input-style">
                                <label for="exampleFormControlInput1" class="form-label">Ville<span style="color: red">*</span></label>
                                <input type="text" name="city" class="form-control" placeholder="ville" required/>
                            </div>
                            </div>
                            <div class="col">
                            <div class="input-style">
                                <label for="exampleFormControlInput2" class="form-label">Code Postal<span style="color: red">*</span></label>
                                <input type="text" name="postalCode" class="form-control" placeholder="Code Postal" required/>
                            </div>
                            </div>
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Pays<span style="color: red">*</span></label>
                            <input type="text" name="country" placeholder="Pays"/>    
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Email</label>
                            <input type="email" name="email" placeholder="Email"/>    
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Telephone</label>
                            <input type="tel" name="phoneNumber" placeholder="Telephone"/>    
                        </div>
                        <button type="submit" class="btn btn-lg btn-success mt-3 w-100" >Ajouter</button>
                    </form>
                </div>
            </div>
        </div>
    </div>`;
}

/**
 * Displays a modal to add a refusal reason.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @returns {void}
 */
function showModalRefusedReason(id) {
  const refusedReason = document.querySelector(`${id}`);
  if (!refusedReason) {
    throw new Error(`Element with id: ${id} not found`);
  }
  refusedReason.innerHTML = `
    <div class="modal fade" id="refusedReason" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Ajouter une raison de refus</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="refusedForm">
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Raison de refus</label>
                            <input type="text" id = "refusedReasonTexte" placeholder="J'ai déjà pris quelqu'un" required/>
                        <button type="submit" class="btn btn-lg btn-success mt-3 w-100" data-bs-dismiss="modal">Ajouter</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>`;
}

async function getRefusedReason() {
  return new Promise((resolve) => {
    const refusedForm = document.querySelector('#refusedForm');
    if (refusedForm) {
      refusedForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const refusedReasonTexte = document.getElementById('refusedReasonTexte').value;
        resolve(refusedReasonTexte);
      });
    }
  });
}

/**
 * Displays a modal to determine the meeting place.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @returns {void}
 */
function showModalMeetingPlace(id) {
  const meetingPlace = document.querySelector(`${id}`);
  if (!meetingPlace) throw new Error(`Element with id: ${id} not found`);

  meetingPlace.innerHTML = `
    <div class="modal fade" id="meetingPlace" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Où s'est déroulé la rencontre ?</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="meetingPlaceForm">
                    <div class="input-style d-flex justify-content-center align-items-center">
                        <div class="btn-group" role="group" aria-label="Options de réunion">
                            <input type="radio" class="btn-check" name="meetingOption" id="surPlaceRadio" value="Dans l'entreprise" autocomplete="off" checked>
                            <label class="btn btn-outline-success  " for="surPlaceRadio">Dans l'entreprise</label>
                            
                            <input type="radio" class="btn-check" name="meetingOption" id="enDistancielRadio" value="A distance" autocomplete="off">
                            <label class="btn btn-outline-success" for="enDistancielRadio">A distance</label>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-success mt-3 d-block w-100" data-bs-dismiss="modal">Ajouter</button>
                </form>
                
                </div>
            </div>
        </div>
    </div>`;
}

/**
 * Gets the selected meeting place from the modal form.
 * @returns {Promise<string>} - A promise resolving to the selected meeting place.
 */
async function getMeetingPlace() {
  return new Promise((resolve) => {
    const meetingPlaceForm = document.querySelector('#meetingPlaceForm');
    if (meetingPlaceForm) {
      meetingPlaceForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const meetingOption = document.querySelector('input[name="meetingOption"]:checked');
        if (meetingOption) {
          const meetingPlaceValue = meetingOption.value;
          resolve(meetingPlaceValue);
        }
      });
    }
  });
}

/**
 * Displays a modal to accept a stage with a list of responsible persons.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @param {Array} listResponsable - The list of responsible persons.
 * @returns {void}
 */
function showModalAccept(id) {
  const acceptStage = document.querySelector(`${id}`);

  acceptStage.innerHTML = `
    <div class="modal fade" id="acceptStage" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-body p-4">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h3>Création d'un stage</h3>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="enterprise-list" id="test">
                    <div class="input-group mb-3">
                        <div class="form-floating">
                            <input type="search" class="form-control rounded" id="filterResponsable" placeholder="Rechercher" aria-label="Search" aria-describedby="search-addon">
                            <label for="filterEnterprise">Rechercher un responsable</label>
                        </div>
                        <button class="btn btn-success" type="button" id="button-addon2" data-bs-toggle="modal" data-bs-target="#responsableModal">Ajouter un responsable
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-square" viewBox="0 0 16 16">
                                <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
                            </svg> 
                        </button>
                    </div>

                    <h6 class="text-muted">Veuillez sélectionner un responsable</h6>
                    <ul class="list-group mt-2 px-2" id="changeList">
                    </ul>

                    <h6 class="text-muted mt-3">Veuillez sélectionner une date et un sujet</h6>
                    <div class="mt-1 px-2">
                        <div class="form-floating my-1">
                            <input type="date" class="form-control" id="dateStage" placeholder="Date du stage" required>
                            <label for="dateStage">Date du stage</label>
                        </div>
                        <div class="form-floating my-1">
                            <textarea class="form-control" placeholder="Leave a comment here" id="subjectStage"></textarea>
                            <label for="subjectStage">Sujet du stage</label>
                        </div>
                    </div>
                    <button type="button" id="valid-buttonResponsable" class="btn btn-lg btn-success mt-3 w-100" data-bs-dismiss="modal" disabled>Valider</button>
                </div>
            </div>
        </div>
      </div>
    </div>
  `;
}

async function updateShowModalAccept(listResponsable, idEnterprise) {
  const triedList = listResponsable.filter(
    (responsable) => parseInt(responsable.enterprise, 10) === parseInt(idEnterprise, 10),
  );

  const div = document.getElementById('changeList');

  if (triedList.length === 0) {
    div.innerHTML = '<li class="list-group-item text-muted">Aucun responsable disponible</li>';
  } else {
    div.innerHTML = triedList.map((responsable) => `
      <li class="list-group-item responsableRow" id="responsableRow">
          <input class="choiceResponsable" type="radio" name="responsableRadio" id="${responsable.responsableId}" value="${responsable.responsableId}">
          ${responsable.firstName} ${responsable.lastName}
      </li>`).join('');
  }
  // Set the date input's value to today's date
  const dateStage = document.getElementById('dateStage');
  const today = new Date().toISOString().split('T')[0];
  dateStage.value = today;

  // Add the filter functionality
  const responsableRow = document.querySelectorAll('.responsableRow');
  const filterResponsable = document.querySelector('#filterResponsable');
  if (filterResponsable) {
    filterResponsable.addEventListener('keyup', () => {
      const name = filterResponsable.value.toUpperCase();

      responsableRow.forEach((item) => {
        const textValue = item.innerText;
        const isVisible = textValue.toUpperCase().indexOf(name) > -1;
        const displayValue = isVisible ? 'block' : 'none';
        // eslint-disable-next-line no-param-reassign
        item.style.display = displayValue;
      });
    });
  }
}

/**
 * Gets the subject of the internship project from the modal form.
 * @returns {Promise<string>} - A promise resolving to the subject of the internship project.
 */
async function getStageSubject() {
  return new Promise((resolve) => {
    const subjectStage = document.querySelector('#subjectStage');
    if (subjectStage) {
      const subjectStageValue = subjectStage.value;
      resolve(subjectStageValue);
    }
  });
}

/**
 * Gets the date of the internship project from the modal form.
 * @returns {Promise<string>} - A promise resolving to the date of the internship project.
 */
async function getStageDate() {
  return new Promise((resolve) => {
    const dateStage = document.querySelector('#dateStage');
    if (dateStage) {
      const dateStageValue = dateStage.value;
      resolve(dateStageValue);
    }
  });
}

/**
 * Gets the ID of the selected responsible person from the modal form.
 * @returns {Promise<string>} - A promise resolving to the ID of the selected responsible person.
 */
async function getResponsable() {
  const validButtonResponsable = document.querySelector('#valid-buttonResponsable');
  return new Promise((resolve) => {
    validButtonResponsable.addEventListener('click', () => {
      const selectedResponsableRadio = document.querySelector(
        'input[name="responsableRadio"]:checked',
      );
      if (selectedResponsableRadio) {
        const responsableValue = selectedResponsableRadio.value;
        resolve(responsableValue);
      }
    });
  });
}

/**
 * Displays a modal to add a new responsible person.
 * @param {string} id - The ID of the HTML element containing the modal.
 * @returns {void}
 */
function showModalResponsable(id) {
  const responsableModal = document.querySelector(`${id}`);
  if (!responsableModal) {
    throw new Error(`Element with id: ${id} not found`);
  }

  responsableModal.innerHTML = `
    <div class="modal fade" id="responsableModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Ajouter un nouveau Responsable</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="responsableForm">
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Nom*</label>
                            <input type="text" id = "responsableName" placeholder="Nom" required/>
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Prénom*</label>
                            <input type="text" id = "responsableFirstname" placeholder="Prénom" required/>
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Telephone*</label>
                            <input type="tel" id = "responsablePhone" placeholder="Telephone"/ required>    
                        </div>
                        <div class="input-style">
                            <label for="exampleFormControlInput1" class="form-label">Email</label>
                            <input type="email" id= "responsableEmail" placeholder="Email"/>    
                        </div>
                        <button type="submit" class="btn btn-lg btn-success mt-3 w-100" data-bs-dismiss="modal">Ajouter</button>
                    </form>
                </div>
            </div>
        </div>
    </div>`;
}

/**
 * Handles the change event for a set of radio buttons.
 * @param {Element[]} radioButtons - The radio buttons.
 * @returns {Promise<boolean>} A promise that resolves to true when a radio button is clicked.
 */
function handleRadioButtonChange(radioButtons) {
  return new Promise((resolve) => {
    radioButtons.forEach((buttn) => {
      buttn.addEventListener('change', (event) => {
        event.preventDefault();
        if (event.target.checked) {
          resolve(true);
        }
      });
    });
  });
}

/**
 * Handles the change event for a date input element.
 * @param {Element} dateElement - The date input element.
 * @returns {Promise<boolean>} A promise that resolves to true when the date input changes.
 */
function handleDateChange(dateElement) {
  return new Promise((resolve) => {
    dateElement.addEventListener('change', (event) => {
      event.preventDefault();
      resolve(true);
    });
  });
}

module.exports = {
  renderButtons,
  showModalContact,
  showModalEnterpriseForm,
  showModalRefusedReason,
  showModalInternshipProject,
  showModalMeetingPlace,
  showModalAccept,
  showModalResponsable,
  updateShowModalAccept,
  getRefusedReason,
  getMeetingPlace,
  getStageSubject,
  getStageDate,
  getResponsable,
  handleRadioButtonChange,
  handleDateChange,
};
