// import Link from 'next/link'
import LoginForm from "@/componenets/login-form";
import { useState } from "react";
export default function App() {
  const [loggedIn, setLoggedIn] = useState();
  if (!loggedIn) {
    return <LoginForm setLoggedIn={setLoggedIn} />;
  }
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <LoginForm />
    </main>
  );
}
