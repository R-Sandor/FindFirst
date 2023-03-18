import axios from 'axios';
const API_URL = 'http://localhost:9000/api/auth/';

class AuthService {
    login() {
        return axios
            .post(
                API_URL + 'signin',
                {},
                {
                      withCredentials: true,
                    auth: {
                        username: 'user',
                        password: 'password',
                    },
                }
            ).then(res => { res.data
                console.log("here")
            });
    }

    logout() {
        console.log("loggingout")
    }

    // register(user) {
    //     return axios.post(API_URL + 'signup', {
    //         username: user.username,
    //         email: user.email,
    //         password: user.password,
    //     });
    // }
}

export default new AuthService();
