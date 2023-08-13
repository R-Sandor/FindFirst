"use client";
import authService, { AuthStatus } from "@/services/auth.service";
import TagList from '@components/TagList'
import { useEffect, useState } from "react";

export default function App() {
  const [authorized, setAuthorized] = useState<AuthStatus>();

  useEffect(() => {
    setAuthorized(authService.getAuthorized());
  }, []);

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return authorized ? <div>
    <div className="w-1/4">
    <TagList/>
    </div>
  </div> : <div> Hello Welcome to BookmarkIt. </div>;
}
