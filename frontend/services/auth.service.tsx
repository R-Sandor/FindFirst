import axios from "axios";
import { alertService } from "./alert.service";
import { credentials } from "../app/account/login/page";
import router, { useParams, usePathname, useRouter } from "next/navigation";
import { withRouter } from "next/router";
export interface user {
  username: string
  refreshToken: string
}

const SIGNIN_URL = "http://localhost:9000/api/auth/signin";


class AuthService {
  logginSuccess: boolean = false;
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
        let signedinUser: user = { username: credentials.username, refreshToken: response.data.refreshToken}
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
    // return this.logginSuccess
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
}
export const authService = new AuthService();
