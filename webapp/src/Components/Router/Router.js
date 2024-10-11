import { removePathPrefix, usePathPrefix } from '../../utils/path-prefix';
import routes from './routes';
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from './Navigate';
import { getRoleName } from '../../utils/Util';

const Router = () => {
  onFrontendLoad();
  onNavBarClick();
  onHistoryChange();
};

function onNavBarClick() {
  const navbarWrapper = document.querySelector('#navbarWrapper');

  navbarWrapper.addEventListener('click', (e) => {
    e.preventDefault();
    const navBarItemClicked = e.target;
    const uri = navBarItemClicked?.dataset?.uri;
    if (uri) {
      const route = routes[uri];
      if (!route) throw Error(`The ${uri} ressource does not exist.`);

      const user = getAuthenticatedUser();
      if (
        route.roles.length === 0 ||
        (!user && route.roles.includes('ANONYMOUS')) ||
        (user && route.roles.includes(user.role))
      ) {
        route.component();
        window.history.pushState({}, '', usePathPrefix(uri));
      } else {
        // show alert and redirect to home page
        showBootstrapAlert(
          `[Role: ${getRoleName(user?.role)}]: Vous n'êtes pas autorisé à accéder à cette page.`,
          'danger',
          '/',
        );
      }
    }
  });
}

function onHistoryChange() {
  window.addEventListener('popstate', () => {
    const uri = removePathPrefix(window.location.pathname);
    const route = routes[uri];

    const user = getAuthenticatedUser();
    if (
      route.roles.length === 0 ||
      (!user && route.roles.includes('ANONYMOUS')) ||
      (user && route.roles.includes(user.role))
    ) {
      route.component();
    } else {
      // show alert and redirect to home page
      showBootstrapAlert(
        `[Role: ${getRoleName(user?.role)}]: Vous n'êtes pas autorisé à accéder à cette page.`,
        'danger',
        '/',
      );
    }
  });
}

function onFrontendLoad() {
  const uri = removePathPrefix(window.location.pathname);
  const route = routes[uri];
  if (!route) throw Error(`The ${uri} ressource does not exist.`);

  const user = getAuthenticatedUser();
  if (
    route.roles.length === 0 ||
    (!user && route.roles.includes('ANONYMOUS')) ||
    (user && route.roles.includes(user.role))
  ) {
    const loaded = document.readyState === 'complete';
    if (loaded) route.component();
    else window.addEventListener('load', route.component);
  } else {
    // show alert and redirect to home page
    showBootstrapAlert(
      `[Role: ${getRoleName(user?.role)}] Vous n'êtes pas autorisé à accéder à cette page.`,
      'danger',
      '/',
    );
  }
}

/**
 *  Shows a bootstrap alert message
 * @param {*} message  The message to display
 * @param {*} alertType  The alert type (success, danger, warning, info)
 * @param {*} redirectUrl  The url to redirect to after the alert is removed
 */
function showBootstrapAlert(message, alertType = 'warning', redirectUrl = null) {
  const alertContainer = document.querySelector('main');

  const alertElement = document.createElement('div');
  alertElement.className = `alert alert-${alertType}`;

  alertElement.textContent = message;
  alertElement.style.position = 'fixed';
  alertElement.style.top = '50%';
  alertElement.style.left = '50%';
  alertElement.style.transform = 'translate(-50%, -50%)';

  alertContainer.appendChild(alertElement);

  setTimeout(() => {
    alertElement.remove();
    if (redirectUrl) Navigate(redirectUrl);
  }, 3000);
}

export default Router;
