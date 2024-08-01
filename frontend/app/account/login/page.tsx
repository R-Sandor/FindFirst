"use client";
import { Formik, Field, Form } from "formik";
import styles from "./login-form.module.scss";
import authService from "@/services/auth.service";
import * as Yup from "yup";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";

export interface credentials {
  username: string;
  password: string;
}

function signMessage(
  submitSuccess: boolean,
  submitMessage: string
) {
  return !submitSuccess ?
    submitFailureDisplay(submitMessage) : null;
}

function submitFailureDisplay(submissionMessage: string) {
  return <div className={styles.failure}>{submissionMessage}</div>;
}
export default function Page() {
  const [signinSuccess, setSigninSuccess] = useState<boolean>(true);
  const attemptCount = useRef<number>(0);
  const router = useRouter();
  const handleOnSubmit = async (credentials: credentials, actions: any) => {
    if (await authService.login(credentials)) {
      attemptCount.current = 0;
      router.push("/");
    } else {
      console.log("failed")
      attemptCount.current = attemptCount.current + 1;
      setSigninSuccess(false)
    }
  };

  const SigninSchema = Yup.object().shape({
    username: Yup.string().required("Required"),
    password: Yup.string().required("Required"),
  });

  return (
    <div className={` ${styles.center} grid`}>
      <div className={" " + styles.login_box + " "}>
        <h1 className="display-6 mb-3">Login</h1>
        <Formik
          initialValues={{
            username: "",
            password: "",
          }}
          onSubmit={handleOnSubmit}
          validationSchema={SigninSchema}
        >
          {({ isValid, dirty }) => (
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
              {signMessage(signinSuccess, "Username or password not found.")}
              <button
                type="submit"
                disabled={!(isValid && dirty)}
                className={`btn ${styles.signin_button}`}
              >
                Login
              </button>
              {
                attemptCount.current > 2 ? <button
                  type="submit"
                  className={`btn ${styles.forgot_button}`}
                  onClick={()=> { router.push("/account/resetPassword") }}
                >
                  Forgot Password?
                </button> : null
              }
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
