"use client";
import { Formik, Field, Form } from "formik";
import styles from '../accountForm.module.scss'
import * as Yup from "yup";
import { useEffect, useState } from "react";

export interface ForgotPasswordRequest {
    email: string;
}

const resetUrl = process.env.NEXT_PUBLIC_SERVER_URL + "/user/resetPassword";

const emailValidationSchema = Yup.object().shape({
    email: Yup.string().email("Invalid email").required("Required"),
});

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
            setSubmitMessage("Reset token has been sent to your email.");
        }
    }, [submitSuccess]);

    const handleOnSubmit = async (forgot: ForgotPasswordRequest, actions: any) => {
        const response = await fetch(resetUrl + "?email=" + forgot.email, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
        })
        if (!response.ok) {
            setSubmitMessage(await response.text());
            setSubmitSuccess(false);
        } else {
            setSubmitSuccess(true)
        }
        actions.resetForm();
    };
    return (
        <div className="grid h-screen place-items-center">
            <div className={`content-center p-3 ${styles.resetForm}`}>
                <h1 className="display-6 mb-3">Reset Password</h1>
                <p>
                    Psst: This is the email for your account.
                </p>
                <Formik
                    initialValues={{
                        email: ""
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
