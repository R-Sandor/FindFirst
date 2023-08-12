import axios from "axios";
import { alertService } from "./alert.service";
import { credentials } from "../app/account/login/page";
export interface User {
  username: string
  refreshToken: string
}

const SIGNIN_URL = "http://localhost:9000/api/auth/signin";
 
export enum AuthStatus {
  Unauthorized, 
  Authorized
}

class AuthService {
  user(): User {
    return JSON.parse(localStorage.getItem("user") || '{}');
  }
  async login(credentials: credentials): Promise<boolean> {
    let success = false;
    console.log(credentials);
    await axios({
      url: SIGNIN_URL,
      method: "POST",
      withCredentials: true,
      auth: {
        username: credentials.username,
        password: credentials.password,
      },
    }).then((response) => {
      console.log(response);
      if (response.status == 200) { 
        console.log("valid login")
        let signedinUser: User = { username: credentials.username, refreshToken: response.data.refreshToken}
        localStorage.setItem("user", JSON.stringify(signedinUser));
        success = true;    
      }
    });
    //   .catch((error) => {
    //     actions.setSubmitting(false);
    //     actions.resetForm();
    //     handleServerResponse(false, error.response);
    //   });
    return success; 
  }

  logout() {
    console.log("loggingout");
    alertService.clear();
    // remove user from local storage, publish null to user subscribers and redirect to login page
    localStorage.removeItem("user");
    // Router.push("/account/login");
  }

  // register(user) {
  //     return axios.post(API_URL + 'signup', {
  //         username: user.username,
  //         email: user.email,
  //         password: user.password,
  //     });
  // }

  authCheck(url: string): AuthStatus  {
    // redirect to login page if accessing a private page and not logged in
    const publicPaths = ["/account/login", "/account/register"];
    const path = url.split("?")[0];
    return (this.user() && !publicPaths.includes(path)) ?  
       AuthStatus.Unauthorized : AuthStatus.Authorized
      // setAuthorized(false);
      // router.push("/account/login");
      // setAuthorized(true);
  }
}
export const authService = new AuthService();
