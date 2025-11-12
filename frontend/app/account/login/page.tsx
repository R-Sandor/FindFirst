"use client";
import { Formik, Field, Form } from "formik";
import styles from "./login-form.module.scss";
import authService from "@/services/auth.service";
import * as Yup from "yup";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import userApi from "@api/userApi";

const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
const AUTH_ENDPOINT = SERVER_URL + "/";
export interface Credentials {
  username: string;
  password: string;
}

export interface Oauth2Sources {
  provider: string;
  iconUrl: string;
  authEndpoint: string;
}

function failureMessage(submitMessage: string) {
  return submitFailureDisplay(submitMessage);
}

function submitFailureDisplay(submissionMessage: string) {
  return <div className={styles.failure}>{submissionMessage}</div>;
}

export default function Page() {
  const [signinFailure, setSigninFailure] = useState<boolean>(false);
  const attemptCount = useRef<number>(0);
  const [oauth2Providers, setOauth2Provider] = useState<Oauth2Sources[]>([]);
  const router = useRouter();

  useEffect(() => {
    userApi.oauth2Providers().then((resp) => {
      setOauth2Provider(resp.data as Oauth2Sources[]);
    });
  }, []);

  const handleOnSubmit = async (credentials: Credentials) => {
    if (await authService.login(credentials)) {
      attemptCount.current = 0;
      router.push("/");
    } else {
      attemptCount.current = attemptCount.current + 1;
      setSigninFailure(true);
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
        {oauth2Providers.length > 0 ? (
          <div className={styles.oauth}>
            <h4>
              <i>or login with:</i>
            </h4>
            <ul className={`list-group list-group-flush `}>
              {oauth2Providers.map((oauth, index) => (
                <a
                  href={AUTH_ENDPOINT + oauth.authEndpoint}
                  target="_self"
                  key={index}
                  className="list-group-item rounded"
                >
                  {oauth.provider}
                  <span className="float-end">
                    <img
                      src={oauth.iconUrl}
                      alt={`${oauth.provider} icon`}
                      style={{ width: 25, height: 25, marginRight: 10 }}
                    />
                  </span>
                </a>
              ))}
            </ul>
          </div>
        ) : null}
      </div>
    </div>
  );
}

// src={oauth.iconUrl}
