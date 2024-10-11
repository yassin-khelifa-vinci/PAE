import HomePage from '../HomePage/HomePage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import Logout from '../Logout/Logout';
import DashboardPage from '../Pages/student/DashboardPage';
import TeacherDashboardPage from '../Pages/teacher/teacherDashboard';
import SettingsPage from '../Pages/SettingsPage';
import SearchPage from '../Pages/SearchPage';
import ResponsablePage from '../Pages/teacher/responsablePage';

const routes = {
  '/': { component: HomePage, roles: [] },
  '/login': { component: LoginPage, roles: ['ANONYMOUS'] }, // Allowed to all user without role
  '/register': { component: RegisterPage, roles: ['ANONYMOUS'] },
  '/logout': { component: Logout, roles: ['ANONYMOUS', 'STUDENT', 'TEACHER', 'ADMINISTRATIVE'] },
  '/dashboard': { component: DashboardPage, roles: ['STUDENT'] },
  '/teacher/dashboard': { component: TeacherDashboardPage, roles: ['TEACHER'] },
  '/settings': { component: SettingsPage, roles: ['STUDENT', 'TEACHER', 'ADMINISTRATIVE'] },
  '/search': { component: SearchPage, roles: ['TEACHER', 'ADMINISTRATIVE'] },
  '/responsable': { component: ResponsablePage, roles: ['TEACHER'] },
};

export default routes;
