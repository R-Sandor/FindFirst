"use client";

import { useEffect, useState } from "react";
import { usePathname, useSearchParams, useRouter } from "next/navigation";
import authService, { AuthObserver, AuthStatus } from "@services/auth.service";
import UseAuth from "./UseAuth";

export function NavigationEvents() {
  const userAuth = UseAuth();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const router = useRouter();

  useEffect(() => {
    console.log(pathname)
      if (authService.authCheck(pathname) == AuthStatus.Unauthorized) {
      // do the reroute here.
      router.push("/account/login");
    }
  }, [pathname, searchParams, router, userAuth]);

  return null;
}
