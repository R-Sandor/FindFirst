import axios from "axios";
import { alertService } from "./alert.service";
import { credentials } from "../app/account/login/page";
import useStorage from '@components/useStorage'
import React from "react";
export interface User {
  username: string;
  refreshToken: string;
}

const SIGNIN_URL = "http://localhost:9000/api/auth/signin";

// Used to declare if user is logged-in.
export enum AuthStatus {
  Unauthorized,
  Authorized,
}

export type AuthObserver = (autherizedState: AuthStatus) => void;

class AuthService  {
  private observers: AuthObserver[] = [];

  private authorizedState: AuthStatus = AuthStatus.Unauthorized;
  
  public attach(observer: AuthObserver) {
    this.observers.push(observer);
  }

  public detach(observer: AuthObserver) {
    this.observers = this.observers.filter((obs) => obs !== observer);
  }

  public getUser(): User | null {
     let user = localStorage.getItem("user");
     return user ? JSON.parse(user) : null;
  }
  public setUser(user: User | null){ 
    localStorage.setItem("user", JSON.stringify(user)) 
  }

  public getAuthorized(): AuthStatus {
    return this.getUser() ? AuthStatus.Authorized : AuthStatus.Unauthorized;
  }
  public async login(credentials: credentials): Promise<boolean> {
    let success = false;
    await axios({
      url: SIGNIN_URL,
      method: "POST",
      withCredentials: true,
      auth: {
        username: credentials.username,
        password: credentials.password,
      },
    }).then((response) => {
      if (response.status == 200) {
        let signedinUser: User = {
          username: credentials.username,
          refreshToken: response.data.refreshToken,
        };
        this.notify(this.authorizedState = AuthStatus.Authorized);
        localStorage.setItem("user", JSON.stringify(signedinUser));
        success = true;
      }
    });
    // TODO: some error handling here.
    //   .catch((error) => {
    //     actions.setSubmitting(false);
    //     actions.resetForm();
    //     handleServerResponse(false, error.response);
    //   });
    return success;
  }

  public logout() {
    // alertService.clear();
    // remove user from local storage, publish null to user subscribers and redirect to login page
    localStorage.removeItem("user");
    this.authorizedState = AuthStatus.Unauthorized
    this.notify(AuthStatus.Unauthorized);
  }

  // TODO: Handle user registration.
  public register(user: User) {}
  //     return axios.post(API_URL + 'signup', {
  //         username: user.username,
  //         email: user.email,
  //         password: user.password,
  //     });
  // }

  // TODO: Stub to handle when the user has been logged out at some point.
  handleAuth() {}

  public authCheck(url: string): AuthStatus {
    console.log("checking Path")
    // redirect to login page if accessing a private page and not logged in
    const publicPaths = ["/account/login", "/account/register"];
    const path = url.split("?")[0];
    return !this.getUser() && !publicPaths.includes(path)
        ? AuthStatus.Unauthorized
        : AuthStatus.Authorized;
  }

  private notify(authorizedState: AuthStatus) {
    this.observers.forEach((observer) => {
      console.log("notifying! " + authorizedState)
      observer(authorizedState);
    });
  }
  
}
const authService = new AuthService();
export default authService;
