import authService, { AuthObserver, AuthStatus } from "@services/auth.service";
import { useEffect, useState } from "react";

export default function UseAuth() {
  const [authorized, setAuthorized] = useState<AuthStatus>();

  const onAuthUpdated: AuthObserver = (authState: AuthStatus) => {
    setAuthorized(authState);
  };

  useEffect(() => {
    authService.attach(onAuthUpdated);
    return () => authService.detach(onAuthUpdated);
  }, []);

  useEffect(() => {
    setAuthorized(authService.getAuthorized());
  }, []);

  return authorized;
}
