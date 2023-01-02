import axios from 'axios';
const API_URL = 'http://localhost:9000/api/auth/';

class AuthService {
    login(user) {
        return axios
            .post(
                API_URL + 'signin',
                {},
                {
                    auth: {
                        username: 'user',
                        password: 'password',
                    },
                }
            )
            .then((response) => {
                if (response.data) {
                    let myuser = user.accessToken = JSON.stringify(response.data)
                    localStorage.setItem('user', myuser);
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
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
