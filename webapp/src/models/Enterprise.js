const { getStatusName } = require('../utils/Util');

/**
 * function who returns the class of the status
 * @param {String} status
 * @returns {String}
 */
function getStatusClass(status) {
  if (status === 'ADMITTED') return 'active-btn';
  if (status === 'ACCEPTED') return 'success-btn';
  if (status === 'STARTED') return 'info-btn';
  if (status === 'TURNED_DOWN') return 'close-btn';
  if (status === 'UNSUPERVISED') return 'primary-btn';
  if (status === 'ON_HOLD') return 'warning-btn';
  return '';
}

/**
 * function who sorts the enterprises by tradeName and designation
 * @param {Array} enterprises
 * @returns {Array}
 */
function sortEnterprises(enterprises, sortKey = 'tradeName', order = 'asc') {
  return enterprises.sort((a, b) => {
    const valueA = a[sortKey] !== null && a[sortKey] !== undefined ? a[sortKey].toString().toLowerCase() : '';
    const valueB = b[sortKey] !== null && b[sortKey] !== undefined ? b[sortKey].toString().toLowerCase() : '';

    if (valueA < valueB) return order === 'asc' ? -1 : 1;
    if (valueA > valueB) return order === 'asc' ? 1 : -1;
    return 0;
  });
}


/**
 * function who shows the modal to add a reason of blacklist
 * @param {String} id
 */
function showModalBlacklist(id) {
  const modalContainer = document.querySelector(id);
  if (!modalContainer) throw new Error(`Element with id ${id} not found`);

  modalContainer.innerHTML = `
    <div class="modal fade" id="blacklistReasonModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3>Ajouter une raison de blacklist</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="blacklistForm">
                        <div class="input-style">
                            <label for="blacklistReasonInput" class="form-label">Raison de la blacklist</label>
                            <input type="text" id="blacklistReasonInput" placeholder="Raison du blacklist" required/>
                            <button type="submit" class="btn btn-lg btn-success mt-3 w-100" data-bs-dismiss="modal" id="submit-reason">Ajouter</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>`;
}

/**
 * function who shows the contact list
 * @param {String} divIdentifier
 */
function showContactList(divIdentifier) {
  const parent = document.querySelector(`${divIdentifier}`);
  if (!parent) throw new Error(`Element with id: ${divIdentifier} not found`);

  const childDiv = document.createElement('div');
  childDiv.innerHTML = `
    <div class="modal fade" id="contactModal" tabindex="-1" aria-hidden="true" data-bs-keyboard="true">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-xl">
            <div class="modal-content rounded-4 shadow">
                <div class="modal-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h3 id="modal-title">Contacts pris avec l'entreprise</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="table-wrapper table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Nom de l'étudiant</th>
                                    <th>Année académique</th>
                                    <th>État contact</th>
                                    <th>Lieu de rencontre</th>
                                    <th>Raison du refus</th>
                                </tr>
                            </thead>
                            <tbody id="contactList">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>`;

  parent.appendChild(childDiv);
}

/**
 * function who updates the contact list
 * @param {Array} list
 */
function updateContactList(list) {
  const contactList = document.querySelector('#contactList');
  if (!contactList) throw new Error(`Element with id: contactList not found`);

  contactList.innerHTML = list.map((usr) => `
      <tr>
          <td>${usr.userDTO.lastName} ${usr.userDTO.firstName}</td>
          <td>${usr.schoolYear}</td>
          <td><span class="status-btn ${getStatusClass(usr.contactStatus)}">${getStatusName(usr.contactStatus)}</span></td>
          <td>${usr.meetingPlace || ''}</td>
          <td>${usr.reasonForRefusal || ''}</td>
      </tr>
    `).join('');

  const modalTitle = document.querySelector('#modal-title');
  if (!modalTitle) throw new Error(`Element with id: modal-title not found`);
  modalTitle.innerHTML = `Contacts pris avec l'entreprise: ${list[0].enterpriseDTO.tradeName}`;
}

/**
 * function who receives the reason of blacklist
 * @returns {Promise}
 */
async function recepBlacklistReason() {
  return new Promise((resolve) => {
    const blacklistForm = document.querySelector('#blacklistForm');
    if (blacklistForm) {
      blacklistForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const blacklistReasonInput = document.getElementById('blacklistReasonInput');
        const blackListReasonValue = blacklistReasonInput.value;
        resolve(blackListReasonValue);
      });
    }
  });
}

module.exports = {
  getStatusClass,
  sortEnterprises,
  showModalBlacklist,
  showContactList,
  updateContactList,
  recepBlacklistReason,
};
