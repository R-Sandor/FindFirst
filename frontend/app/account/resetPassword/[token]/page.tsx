"use client";
import { Field, Form, Formik } from "formik";
import { useParams } from "next/navigation";
import TokenPassword, { PasswordConfirm } from "@type/account/TokenPassword";
import styles from "../../accountForm.module.scss";
import * as Yup from "yup";
import axios from "axios";
import { useState } from "react";
import { Router, useRouter } from "next/router";

const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/user/";
export default function PasswordReset() {
  const [submitSuccess, setSubmitSuccess] = useState<boolean | undefined>(
    undefined
  );
  const [submitMessage, setSubmitMessage] = useState<string>("");
  const params = useParams();

  function submitSuccessDisplay(submissionMessage: string) {
    return (
      <div className={styles.success}>
        <p>{submissionMessage}</p>
      </div>
    );
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
  function submitFailureDisplay(submissionMessage: string) {
    return <div className={styles.failure}>{submissionMessage}</div>;
  }

  const ResetSchema = Yup.object().shape({
    password: Yup.string()
      .min(8, "Password too short!")
      .max(24, "Password is too long")
      .matches(
        /^(?=.*[!@#$%^&*])/,
        "Password must contain at least one special character"
      )
      .required("Required"),
    passwordConfirm: Yup.string()
      .oneOf([Yup.ref("password"), undefined], "Passwords must match")
      .required("Required"),
  });

  const handleOnSubmit = async (
    passwordConfirm: PasswordConfirm,
    actions: any
  ) => {
    let tknVal = "";
    if (typeof params.token == "string") {
      tknVal = params.token;
    }
    const tknPwd: TokenPassword = {
      token: tknVal,
      password: passwordConfirm.password,
    };
    console.log("POSTING");
    axios.post(SERVER_URL + "changePassword", tknPwd).then(
      (response) => {
        setSubmitMessage(response.data.text);
        setSubmitSuccess(true);
        actions.resetForm();
      },
      (reject) => {
        setSubmitMessage(reject.response.data.text);
        setSubmitSuccess(false);
      }
    );
  };

  return (
    <div className="grid h-screen place-items-center">
      <div className={"content-center " + " p-3"}>
        <h1 className="display-16 mb-3">Forget something?</h1>
        <Formik
          initialValues={{
            password: "",
            passwordConfirm: "",
          }}
          onSubmit={handleOnSubmit}
          validationSchema={ResetSchema}
        >
          {({ values, setFieldValue, errors, touched, isValid, dirty }) => (
            <Form>
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
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="passwordConfirm"
                  name="passwordConfirm"
                  placeholder="Confirm Password"
                  type="password"
                  value={values.passwordConfirm}
                  onChange={(e: any) =>
                    setFieldValue("passwordConfirm", e.target.value)
                  }
                />
                {errors.passwordConfirm && touched.passwordConfirm ? (
                  <div>{errors.passwordConfirm}</div>
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
