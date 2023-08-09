"use client";
import React, { useState } from "react";
import axios from "axios";
import { Formik, Field, Form, FormikHelpers } from "formik";
import styles from "./login-form.module.css";

interface Values {
  username: string;
  password: string;
}
interface ServerState {
  ok: boolean;
  msg: String;
}
const SIGNIN_URL = "http://localhost:9000/api/auth/signin";

export default function LoginForm() {
  const [serverState, setServerState] = useState<ServerState>();
  const handleServerResponse = (ok: boolean, msg: String) => {
    setServerState({ ok, msg });
  };
  const handleOnSubmit = (values: Values, actions: any) => {
    console.log(values)
    axios({
      url: SIGNIN_URL,
      method: "POST",
      withCredentials: true,
      auth: {
        username: values.username,
        password: values.password,
      },
    })
      .then((response) => {
        console.log(response);
        actions.setSubmitting(false);
        actions.resetForm();
        handleServerResponse(true, "Thanks!");
      })
      .catch((error) => {
        actions.setSubmitting(false);
        handleServerResponse(false, error.response);
      });
  };

  return (
    <div className={styles.login_box + " p-3"}>
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

          <button type="submit" className="btn btn-primary">
            Login
          </button>
        </Form>
      </Formik>
    </div>
  );
}
