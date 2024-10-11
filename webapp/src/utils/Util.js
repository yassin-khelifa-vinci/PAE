function getRoleName(role) {
  switch (role) {
    case 'ADMINISTRATIVE':
      return 'Administratif';
    case 'TEACHER':
      return 'Professeur';
    case 'STUDENT':
      return 'Étudiant';
    default:
      return 'Anonyme';
  }
}

function getStatusName(status) {
  switch (status) {
    case 'STARTED':
      return 'initié';
    case 'ADMITTED':
      return 'pris';
    case 'UNSUPERVISED':
      return 'plus suivi';
    case 'TURNED_DOWN':
      return 'refusé';
    case 'ACCEPTED':
      return 'accepté';
    case 'ON_HOLD':
      return 'suspendu';
    default:
      return 'Inconnu';
  }
}

module.exports = { getRoleName, getStatusName };
