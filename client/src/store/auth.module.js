import AuthService from '../services/auth.service';

const initialState = { 
  user: {},
  loggedIn: false
};
const getters = {
  isLoggedIn: state => state.isLoggedIn,
  user: state => state.user
};


const auth =  {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }, user) {
      console.info("Auth user:"  + user)
      return AuthService.login().then(
        user => {
          console.log("here")
          commit('loginSuccess', user);
          return Promise.resolve(user);
        },
        error => {
          console.log("here")
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    // register({ commit }, user) {
    //   return AuthService.register(user).then(
    //     response => {
    //       commit('registerSuccess');
    //       return Promise.resolve(response.data);
    //     },
    //     error => {
    //       commit('registerFailure');
    //       return Promise.reject(error);
    //     }
    //   );
    // }
  },
  mutations: {
    loginSuccess(state, user) {
      state.loggedIn = true;
      state.user = user;
    },
    loginFailure(state) {
      state.loggedIn = false;
      state.user = null;
    },
    logout(state) {
      state.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state) {
      state.loggedIn = false;
    },
    registerFailure(state) {
      state.loggedIn = false;
    }
  }
};

export { 
  auth,
  getters
}