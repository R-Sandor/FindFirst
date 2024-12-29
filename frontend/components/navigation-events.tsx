"use client";

import { useEffect } from "react";
import { usePathname, useSearchParams, useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import UseAuth from "./UseAuth";

export function NavigationEvents() {
  const userAuth = UseAuth();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const router = useRouter();

  useEffect(() => {
    if (
      pathname &&
      authService.authCheck(pathname) == AuthStatus.Unauthorized
    ) {
      // do the reroute here.
      router.push("/account/login");
    }
  }, [pathname, searchParams, router, userAuth]);

  return null;
}
