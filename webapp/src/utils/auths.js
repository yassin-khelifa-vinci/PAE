const STORE_NAME = 'token';
const REMEMBER_ME = 'remembered';

let currentUser;

const getAuthenticatedUser = () => currentUser;

const setAuthenticatedUser = (authenticatedUser) => {
  const remembered = getRememberMe();
  if (remembered) {
    localStorage.setItem(STORE_NAME, authenticatedUser.token);
  } else {
    sessionStorage.setItem(STORE_NAME, authenticatedUser.token);
  }
  currentUser = authenticatedUser.user;
};

const isAuthenticated = () =>
  sessionStorage.getItem(STORE_NAME) || localStorage.getItem(STORE_NAME);

const clearAuthenticatedUser = () => {
  localStorage.clear();
  sessionStorage.clear();
  currentUser = undefined;
};

function getRememberMe() {
  const rememberedSerialized = localStorage.getItem(REMEMBER_ME);
  const remembered = JSON.parse(rememberedSerialized);
  return remembered;
}

function setRememberMe(remembered) {
  const rememberedSerialized = JSON.stringify(remembered);
  localStorage.setItem(REMEMBER_ME, rememberedSerialized);
}

function refreshUser(user) {
  currentUser = user;
}

function getToken() {
  return sessionStorage.getItem(STORE_NAME) || localStorage.getItem(STORE_NAME);
}

export {
  getAuthenticatedUser,
  setAuthenticatedUser,
  isAuthenticated,
  clearAuthenticatedUser,
  getRememberMe,
  setRememberMe,
  refreshUser,
  getToken,
};
