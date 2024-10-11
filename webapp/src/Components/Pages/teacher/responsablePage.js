import { clearPage } from '../../../utils/render';
import { isAuthenticated } from '../../../utils/auths';
import Navigate from '../../Router/Navigate';
import { getAllReponsableFromEnterprise } from '../../../utils/backendRequest';

const main = document.querySelector('main');




const ResponsablePage = async () => {
    if (!isAuthenticated()) {
        Navigate('/login');
    }else{
        clearPage();
        searchResults(await getAllReponsableFromEnterprise());
    }
};

function searchResults(list) {
    list.sort((a, b) => a.enterprise - b.enterprise);

    main.innerHTML += `
    <section class="m-4">
        <h2 class="text-center">Liste des responsable de stage</h2>
        <div class="row">
            ${list.map(resp => `
            <div class="col-md-4 p-2">
                <div class="card">
                    <div class="card-body">                  
                        <h5 class="card-title">${resp.firstName} ${resp.lastName} <span class="badge bg-success" style="--bs-bg-opacity: .7;">${resp.enterpriseDTO.tradeName} ${resp.enterpriseDTO.designation ? `- ${resp.enterpriseDTO.designation}` : ''}</span></h5>
                        
                        <div class="row g-3 align-items-center">
                            <div class="col-auto">
                                <label for="inputPassword6" class="col-form-label">Email:</label>
                            </div>
                            <div class="col">
                                <input type="email" id="inputPassword6" class="form-control form-control-sm" placeholder="${resp.email || ''}" disabled readonly>
                            </div>
                        </div>
                        <div class="row g-3 align-items-center">
                            <div class="col-auto">
                                <label for="inputPassword6" class="col-form-label">Téléphone:</label>
                            </div>
                            <div class="col">
                                <input type="tel" id="inputPassword6" class="form-control form-control-sm" placeholder="${resp.phoneNumber}" disabled readonly>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">${resp.enterpriseDTO.street} ${resp.enterpriseDTO.streetNumber}, ${resp.enterpriseDTO.postalCode} ${resp.enterpriseDTO.city}</div>
                </div>
            </div>
        `).join('')}
        </div>
    </section>`;
}


export default ResponsablePage;
