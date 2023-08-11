"use client";
import React, { useState } from "react";
import axios from "axios";
import { Formik, Field, Form } from "formik";
import styles from "./login-form.module.css";
import { authService } from "@/services/auth.service";
import { useRouter } from "next/navigation";

export interface credentials {
  username: string;
  password: string;
}
const SIGNIN_URL = "/auth/signin";

export default function Page() {
  const router = useRouter();
  const handleOnSubmit = async (credentials: credentials, actions: any) => {
    if (await authService.login(credentials)) {
      console.log("successful auth");
      router.push("/");
    }
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
