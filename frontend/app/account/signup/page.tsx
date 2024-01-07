"use client";
import { Formik, Field, Form, useFormikContext, FormikBag } from "formik";
import styles from "./signup-form.module.scss";
import * as Yup from "yup";
import { useEffect, useState } from "react";

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

const signupUrl = process.env.NEXT_PUBLIC_SERVER_URL + "/user/signup";

const SignupSchema = Yup.object().shape({
  username: Yup.string()
    .min(4, "Username too short!")
    .max(50, "Too long!")
    .required("Required"),
  email: Yup.string().email("Invalid email").required("Required"),
  password: Yup.string()
    .min(8, "Password too short!")
    .max(24, "Password is too long")
    .matches(
      /^(?=.*[!@#$%^&*])/,
      "Password must contain at least one special character"
    )
    .required("Required"),
});

function submitSuccessDisplay(submissionMessage: string) {
  return (
    <div className={styles.success}>
      <p>{submissionMessage}</p>
    </div>
  );
}

function submitFailureDisplay(submissionMessage: string) {
  console.log("FAILURE");
  return <div className={styles.failure}>{submissionMessage}</div>;
}

function submissionMessage(
  submitSuccess: boolean | undefined,
  submitMessage: string
) {
  if (submitSuccess == undefined) {
    return <div></div>;
  }
  return submitSuccess
    ? submitSuccessDisplay(submitMessage)
    : submitFailureDisplay(submitMessage);
}

export default function Page() {
  const [submitSuccess, setSubmitSuccess] = useState<boolean | undefined>(
    undefined
  );
  const [submitMessage, setSubmitMessage] = useState<string>("");

  useEffect(() => {
    if (submitSuccess) {
      setSubmitMessage("Please complete your registration with your email.");
    }
  }, [submitSuccess]);

  const handleOnSubmit = async (signupRequest: SignupRequest, actions: any) => {
    console.log(JSON.stringify(signupRequest));
    const response = await fetch(signupUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(signupRequest),
    })
    if (!response.ok) { 
      setSubmitMessage(await response.text());
      setSubmitSuccess(false);
    } else { 
      setSubmitSuccess(true)
    }

    if (!setSubmitSuccess) { 
      console.log("WE SHOULD NOT SEE THIS")
    }
  };
  return (
    <div className="grid h-screen place-items-center">
      <div className={"content-center " + " p-3"}>
        <h1 className="display-6 mb-3">Sign up</h1>
        <Formik
          initialValues={{
            username: "",
            email: "",
            password: "",
          }}
          onSubmit={handleOnSubmit}
          validationSchema={SignupSchema}
        >
          {({ values, setFieldValue, errors, touched, isValid, dirty }) => (
            <Form>
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="username"
                  name="username"
                  placeholder="Username"
                  type="username"
                  value={values.username}
                  onChange={(e: any) =>
                    setFieldValue("username", e.target.value)
                  }
                />
                {errors.username && touched.username ? (
                  <div>
                    <p>{errors.username}</p>
                  </div>
                ) : null}
              </div>
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="email"
                  name="email"
                  placeholder="Email"
                  type="email"
                  value={values.email}
                  onChange={(e: any) => setFieldValue("email", e.target.value)}
                />
                {errors.email && touched.email ? (
                  <div>{errors.email}</div>
                ) : null}
              </div>
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="password"
                  name="password"
                  placeholder="Password"
                  type="password"
                  value={values.password}
                  onChange={(e: any) =>
                    setFieldValue("password", e.target.value)
                  }
                />
                {errors.password && touched.password ? (
                  <div>{errors.password}</div>
                ) : null}
              </div>
              {submissionMessage(submitSuccess, submitMessage)}
              <button
                type="submit"
                disabled={!(isValid && dirty)}
                className={`btn ${styles.login_button}`}
              >
                Submit
              </button>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
