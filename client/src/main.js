import { createApp } from 'vue';
import App from './App.vue';
import router from "./router";
import store from "./store";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from './plugins/font-awesome'
import 'bootstrap-icons/font/bootstrap-icons.css'

createApp(App)
  .use(router)
  .use(store)
  .component("font-awesome-icon", FontAwesomeIcon)
  .mount('#app')
