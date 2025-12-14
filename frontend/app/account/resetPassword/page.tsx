"use client";
import { Formik, Field, Form } from "formik";
import styles from "../accountForm.module.scss";
import * as Yup from "yup";
import { useEffect, useState } from "react";
import axios from "axios";

export interface ForgotPasswordRequest {
  email: string;
}

const resetUrl = process.env.NEXT_PUBLIC_SERVER_URL + "/user/resetPassword";

const emailValidationSchema = Yup.object().shape({
  email: Yup.string().email("Invalid email").required("Required"),
});

function validateEmail(value: string | undefined) {
  let error;
  if (!value) {
    error = "Required";
  } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
    error = "Invalid email address";
  }
  return error;
}

function submitSuccessDisplay(submissionMessage: string) {
  return (
    <div className={styles.success}>
      <p>{submissionMessage}</p>
    </div>
  );
}

function submitFailureDisplay(submissionMessage: string) {
  return <div className={styles.failure}>{submissionMessage}</div>;
}

function submissionMessage(
  submitSuccess: boolean | undefined,
  submitMessage: string,
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
    undefined,
  );
  const [submitMessage, setSubmitMessage] = useState<string>("");

  useEffect(() => {
    if (submitSuccess) {
      setSubmitMessage("Reset token has been sent to your email.");
    }
  }, [submitSuccess, submitMessage]);

  const handleOnSubmit = async (forgot: ForgotPasswordRequest) => {
    axios
      .post(resetUrl + "?email=" + forgot.email)
      .then((response) => {
        if (response.status == 200) setSubmitSuccess(true);
      })
      .catch((error_) => {
        setSubmitMessage(error_.response.data.error);
        setSubmitSuccess(false);
      });
  };
  return (
    <div className={`grid ${styles.center}`}>
      <div className={`content-center p-3 ${styles.resetForm}`}>
        <h1 className="display-6 mb-3">Reset Password</h1>
        <p>Psst: This is the email for your account.</p>
        <Formik
          initialValues={{
            email: "",
          }}
          onSubmit={handleOnSubmit}
          validationSchema={emailValidationSchema}
        >
          {({ values, setFieldValue, errors, touched, isValid, dirty }) => (
            <Form>
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="email"
                  name="email"
                  placeholder="Email"
                  type="email"
                  value={values.email}
                  validate={validateEmail}
                  onChange={(e: any) => setFieldValue("email", e.target.value)}
                />
                {errors.email && touched.email ? (
                  <div>{errors.email}</div>
                ) : null}
              </div>
              {submissionMessage(submitSuccess, submitMessage)}
              <button
                type="submit"
                disabled={!(isValid && dirty)}
                className={`btn ${styles.signup_button}`}
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
