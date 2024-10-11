import 'bootstrap/dist/css/bootstrap.min.css';
import './stylesheets/main.css';
import './stylesheets/vendor.css';

import Navbar from './Components/Navbar/Navbar';
import Router from './Components/Router/Router';
import checkTokenValidity from './utils/session';

await checkTokenValidity();

Navbar();

Router();
