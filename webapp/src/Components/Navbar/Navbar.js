// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
import $ from 'jquery';
import { getAuthenticatedUser, isAuthenticated } from '../../utils/auths';
import Navigate from '../Router/Navigate';
import logoImage from '../../img/icon.jpg'

const Navbar = () => {
  renderNavbar();
};

function renderNavbar() {
  const authenticatedUser = getAuthenticatedUser();

  const anonymousUserNavbar = `
    <div class="navigation-wrap bg-light start-header start-style px-4">
			<div class="row">
				<div class="col-12">
					<nav class="navbar navbar-expand-md navbar-light">
						<a class="navbar-brand" href="/" target="_blank"><img src="${logoImage}" alt=""></a>	
						<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
							<span class="navbar-toggler-icon"></span>
						</button>
						
						<div class="collapse navbar-collapse" id="navbarSupportedContent">
							<ul class="navbar-nav ml-auto py-4 py-md-0">
								<li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
									<a class="nav-link" href="#" data-uri="/login">Se connecter</a>
								</li>
                <li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
									<a class="nav-link" href="#" data-uri="/register">S'inscrire</a>
								</li>
							</ul>
						</div>
					</nav>		
				</div>
			</div>
		</div>
	</div>`;

  const authenticatedUserNavbar = `
  <div class="navigation-wrap bg-light start-header start-style px-4" >
    <div class="row">
      <div class="col-12">
        <nav class="navbar navbar-expand-md navbar-light">
          <a class="navbar-brand" href="/" target="_blank"><img src="${logoImage}" alt=""></a>	
          
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ml-auto py-4 py-md-0">
              <li class="nav-item pl-4 pl-md-0 ml-0 ml-md-4">
              <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">${authenticatedUser?.lastName} ${authenticatedUser?.firstName}</a>
									<div class="dropdown-menu">
                    <a class="dropdown-item" href="#" data-uri="/settings">Paramètres</a>
										<a class="dropdown-item" href="#" data-uri="/logout">Se déconnecter</a>
									</div>
              </li>
            </ul>
          </div>
        </nav>		
      </div>
    </div>
  </div>
</div>`;



  const navbar = document.querySelector('#navbarWrapper');

  navbar.innerHTML = isAuthenticated() ? authenticatedUserNavbar : anonymousUserNavbar;

  const logo = document.querySelector('.navbar-brand')
  logo.addEventListener('click', () => {
    Navigate('/');
  });

  const adjustHeaderOnScroll = () => {
    const header = $('.start-style');
    $(window).scroll(() => {
      const scroll = $(window).scrollTop();
      if (scroll >= 10) {
        header.removeClass('start-style').addClass('scroll-on');
      } else {
        header.removeClass('scroll-on').addClass('start-style');
      }
    });
  };
  
  const setupMenuHoverEffect = () => {
    $('body').on('mouseenter mouseleave', '.nav-item', (e) => {
      if ($(window).width() > 750) {
        // eslint-disable-next-line no-underscore-dangle
        const _d = $(e.target).closest('.nav-item');
        _d.addClass('show');
        setTimeout(() => {
          _d[_d.is(':hover') ? 'addClass' : 'removeClass']('show');
        }, 1);
      }
    });
  };
  

  
  const initializeApp = () => {
    adjustHeaderOnScroll();
    setupMenuHoverEffect();
  };
  
  initializeApp();
}

export default Navbar;
