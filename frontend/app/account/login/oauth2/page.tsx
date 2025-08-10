"use client";
/**
 *  Once a user logins with OAuth2 from backend service
 *  they should be brought here. This will quickly make
 *  a call to authService which will make a request to
 *  the endpoint /user-info with the already attached
 *  cookie and save the User info.
 */
import authService from "@/services/auth.service";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function OauthLogin() {
  const router = useRouter();

  useEffect(() => {
    authService.getUserInfoOauth2().then((user) => {
      if (user) {
        router.push("/");
      }
    });
  }, [authService]);
}
