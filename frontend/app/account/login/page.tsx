"use client";
import { Formik, Field, Form } from "formik";
import styles from "./login-form.module.css";
import  authService  from "@/services/auth.service";
import { useRouter } from "next/navigation";

export interface credentials {
  username: string;
  password: string;
}

export default function Page() {
  const router = useRouter();
  const handleOnSubmit = async (credentials: credentials, actions: any) => {
    if (await authService.login(credentials)) {
      router.push("/");
    }
  };


  return (
    <div className="grid h-screen place-items-center">
    <div className={ "content-center " + styles.login_box + " p-3"}>
      <h1 className="display-6 mb-3">Login</h1>
      <Formik
        initialValues={{
          username: "",
          password: "",
        }}
        onSubmit={handleOnSubmit}
      >
        <Form>
          <div className="mb-3">
            <Field
              className="form-control"
              id="username"
              name="username"
              placeholder="Username"
              aria-describedby="usernameHelp"
            />
          </div>

          <div className="mb-3">
            <Field
              className="form-control"
              id="password"
              name="password"
              placeholder="Password"
              type="password"
            />
          </div>

          <button type="submit" className="btn bg-sky-500/75">
            Login
          </button>
        </Form>
      </Formik>
    </div>
    </div>
  );
}
