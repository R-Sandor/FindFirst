"use client";
import LoginForm from "@/componenets/login-form";
import router, { useParams, usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
export default function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [authorized, setAuthorized] = useState(false);

  const pathname = usePathname();
  const router = useRouter();
  const user = JSON.parse(localStorage.getItem("user")!);
  useEffect(() => {
    if (user) {
      setLoggedIn(true);
      console.log(loggedIn)
    }
    authCheck(pathname)
    console.log(pathname)
  }, []);

  

  function authCheck(url: string) {
    // redirect to login page if accessing a private page and not logged in
    const publicPaths = ["/account/login", "/account/register"];
    const path = url.split("?")[0];
    console.log(loggedIn)
    if (!user && !publicPaths.includes(path)) {
      console.log("not logged in")
      setAuthorized(false);
    console.log(loggedIn)
      router.push("/account/login");
    } else {
      setAuthorized(true);
    }
  }

  return( authorized ? <div> cool</div> : <div> not cool</div>);
}
