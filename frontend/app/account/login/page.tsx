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

function failureMessage(submitMessage: string) {
  return submitFailureDisplay(submitMessage);
}

function submitFailureDisplay(submissionMessage: string) {
  return <div className={styles.failure}>{submissionMessage}</div>;
}

export default function Page() {
  const [signinFailure, setSignFailure] = useState<boolean>(false);
  const attemptCount = useRef<number>(0);
  const router = useRouter();
  const handleOnSubmit = async (credentials: credentials) => {
    if (await authService.login(credentials)) {
      attemptCount.current = 0;
      router.push("/");
    } else {
      attemptCount.current = attemptCount.current + 1;
      setSignFailure(true);
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
              {signinFailure
                ? failureMessage("Username or password not found.")
                : null}
              <button
                type="submit"
                disabled={!(isValid && dirty)}
                className={`btn ${styles.signin_button}`}
                data-testid="login-btn"
              >
                Login
              </button>
              {attemptCount.current > 2 ? (
                <button
                  type="submit"
                  className={`btn ${styles.forgot_button}`}
                  onClick={() => {
                    router.push("/account/resetPassword");
                  }}
                >
                  Forgot Password?
                </button>
              ) : null}
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
