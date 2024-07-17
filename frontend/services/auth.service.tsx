"use client";
import axios from "axios";
import { credentials } from "../app/account/login/page";
export interface User {
  username: string;
  refreshToken: string;
}

const SIGNIN_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/user/signin";

// Used to declare if user is logged-in.
export enum AuthStatus {
  Unauthorized,
  Authorized,
}

export type AuthObserver = (autherizedState: AuthStatus) => void;

class AuthService {
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

  public getAuthorized(): AuthStatus {
    return this.getUser() ? AuthStatus.Authorized : AuthStatus.Unauthorized;
  }

  public async login(credentials: credentials): Promise<boolean> {
    console.log("user attempt to login");
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
      console.log("signin attempt");
      if (response.status == 200) {
        let signedinUser: User = {
          username: credentials.username,
          refreshToken: response.data.refreshToken,
        };
        localStorage.setItem("user", JSON.stringify(signedinUser));
        this.notify((this.authorizedState = AuthStatus.Authorized));
        console.log(signedinUser);
        success = true;
      }
    });
    return success;
  }

  public logout() {
    localStorage.removeItem("user");
    this.authorizedState = AuthStatus.Unauthorized;
    this.notify(AuthStatus.Unauthorized);
  }

  public authCheck(url: string): AuthStatus {
    // redirect to login page if accessing a private page and not logged in
    const publicPaths = ["/account/login", "/account/signup"];
    const path = url.split("?")[0];
    return !this.getUser() && !publicPaths.includes(path)
      ? AuthStatus.Unauthorized
      : AuthStatus.Authorized;
  }

  private notify(authorizedState: AuthStatus) {
    this.observers.forEach((observer) => {
      observer(authorizedState);
    });
  }
}

const authService = new AuthService();
export default authService;
