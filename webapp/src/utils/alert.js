function showAlert(parentDivId, text) {
  const parentDiv = document.getElementById(parentDivId);

  if (!parentDiv) {
    /* eslint no-console: ["error", { allow: ["warn", "error"] }] */
    console.error(`No element found with id "${parentDivId}"`);
    return;
  }

  const alertDiv = document.createElement('div');
  alertDiv.className = 'alert alert-warning alert-dismissible fade show mt-2';
  alertDiv.role = 'alert';
  alertDiv.innerHTML = `${text}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;

  parentDiv.appendChild(alertDiv);

  setTimeout(() => {
    alertDiv.remove();
  }, 10000);
}

export default showAlert;
