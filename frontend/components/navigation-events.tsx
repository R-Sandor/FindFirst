"use client";

import { useEffect, useState } from "react";
import { usePathname, useSearchParams, useRouter } from "next/navigation";
import authService, { AuthObserver, AuthStatus } from "@services/auth.service";

export function NavigationEvents() {
  const [authorized, setAuthorized] = useState<AuthStatus>(
    AuthStatus.Unauthorized
  );
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const router = useRouter();

  // Recheck that our current location is still accessible.
  const onAuthUpdated: AuthObserver = (authState: AuthStatus) => {
    setAuthorized(authState);
    checkAndRoute();
  };

  const checkAndRoute = () => {
    if (authService.authCheck(pathname) == AuthStatus.Unauthorized) {
      // do the reroute here.
      router.push("/account/login");
    }
  };

  useEffect(() => {
    authService.attach(onAuthUpdated);
    return () => authService.detach(onAuthUpdated);
  }, []);

  useEffect(() => {
    let user = localStorage.getItem("user");
    user ? authService.setUser(JSON.parse(user)) : authService.setUser(null);
    checkAndRoute();
  }, [pathname, searchParams, router]);

  return null;
}
