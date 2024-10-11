// Function to show the loader
function showLoader() {
  const loader = document.createElement('div');
  loader.className = 'spinner-border text-success';
  loader.role = 'status';
  loader.innerHTML = '<span class="visually-hidden">Loading...</span>';

  loader.style.position = 'fixed';
  loader.style.top = '50%';
  loader.style.left = '50%';

  document.body.appendChild(loader);
}

// Function to hide the loader
function hideLoader() {
  const loader = document.querySelector('.spinner-border');
  if (loader) document.body.removeChild(loader);
}

module.exports = { showLoader, hideLoader };
