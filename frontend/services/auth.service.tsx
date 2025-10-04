"use client";
import axios from "axios";
import { Credentials } from "../app/account/login/page";
import userApi from "@api/userApi";
export interface User {
    id: number;
    username: string;
    refreshToken: string;
    profileImage?: string | null;
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

  private user: User | null = null;

  private authorizedState: AuthStatus = AuthStatus.Unauthorized;

  public attach(observer: AuthObserver) {
    this.observers.push(observer);
  }

  public detach(observer: AuthObserver) {
    this.observers = this.observers.filter((obs) => obs !== observer);
  }

  public getUser(): User | null {
    if (this.user) {
      return this.user;
    }
    let user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;
  }

  public setUser(user: User): void {
    this.user = user;
  }

  // Gets the user info implicitly using the cookie 
  // and sets the user info.
    public async getUserInfoOauth2(): Promise<User | null> {
        const backendUser = (await userApi.userInfo()) as any; // the response from backend

        if (backendUser) {
            const user: User = {
                id: backendUser.userId,
                username: backendUser.username,
                refreshToken: "",
                profileImage: backendUser.userPhoto || null,
            };

            this.setUser(user);
            localStorage.setItem("user", JSON.stringify(user));
            this.authorizedState = AuthStatus.Authorized;
            this.notify(this.authorizedState);

            return user;
        }
        return null;
    }

    public getAuthorized(): AuthStatus {
    return this.getUser() ? AuthStatus.Authorized : AuthStatus.Unauthorized;
  }

  public async login(credentials: Credentials): Promise<boolean> {
    let success = false;
    await axios({
      url: SIGNIN_URL,
      method: "POST",
      withCredentials: true,
      auth: {
        username: credentials.username,
        password: credentials.password,
      },
    })
      .then((response) => {
        if (response.status == 200) {
            let signedInUser: User = {
                id: response.data.id,
                username: credentials.username,
                refreshToken: response.data.refreshToken,
                profileImage: response.data.profileImage || null,
            };
            localStorage.setItem("user", JSON.stringify(signedInUser));
          this.user = signedInUser;
          this.authorizedState = AuthStatus.Authorized;
          this.notify(this.authorizedState);
          success = true;
        }
      })
      .catch(() => { });
    return success;
  }

  public logout() {
    localStorage.removeItem("user");
    this.authorizedState = AuthStatus.Unauthorized;
    this.notify(AuthStatus.Unauthorized);
  }

  public authCheck(url: string): AuthStatus {
    // redirect to login page if accessing a private page and not logged in
    const publicPaths = ["/account/", "/about"];
    const path = url.split("?")[0];
    let found = publicPaths.find((p) => {
      return path.startsWith(p);
    });
    return this.getUser() || found
      ? AuthStatus.Authorized
      : AuthStatus.Unauthorized;
  }

  private notify(authorizedState: AuthStatus) {
    this.observers.forEach((observer) => {
      observer(authorizedState);
    });
  }
}

const authService = new AuthService();
export default authService;
