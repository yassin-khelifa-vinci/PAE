import {getAuthenticatedUser, isAuthenticated} from '../../utils/auths';
import { clearPage } from '../../utils/render';
import Teacher from '../Pages/teacher/TeacherHomePage';
import Navigate from '../Router/Navigate';

const HomePage = () => {
  clearPage();
  if (!isAuthenticated()) {
    Navigate('/login');
  } else {
    const user = getAuthenticatedUser(); // Récupère le rôle de l'utilisateur
    const {role} = user; // Fix the syntax error by accessing the role property correctly
    renderHomePage(role);
  }
};

function renderHomePage(role) {
  const main = document.querySelector('main');

  if (!role || role === 'ANONYMOUS') {
    main.innerHTML = `<h3>Rôle non reconnu</h3>`;
    return;
  }
  if(role === 'STUDENT') Navigate('/dashboard');
  if(role === 'TEACHER') Teacher();
  if(role === 'ADMINISTRATIVE') Navigate('/search');
};

export default HomePage;
