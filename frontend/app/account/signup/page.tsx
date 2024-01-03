"use client";
import { Formik, Field, Form, useFormikContext } from "formik";
import styles from "./signup-form.module.scss";
import authService from "@/services/auth.service";
import { useRouter } from "next/navigation";
import * as Yup from "yup";

export interface signupRequest {
  userName: string;
  email: string;
  password: string;
}

const SignupSchema = Yup.object().shape({
  userName: Yup.string()
    .min(4, "Username too short!")
    .max(50, "Too long!")
    .required("Required"),
  email: Yup.string().email("Invalid email").required("Required"),
  password: Yup.string()
    .min(8, "Password too short!")
    .max(24, "Password is too long")
    .matches(
      /^(?=.*[!@#$%^&*])/,
      'Password must contain at least one special character'
    )
    .required("Required"),
});

export default function Page() {

  const router = useRouter();
  const handleOnSubmit = async (
    signupRequest: signupRequest,
    actions: any
  ) => {};
  return (
    <div className="grid h-screen place-items-center">
      <div className={"content-center " + styles.login_box + " p-3"}>
        <h1 className="display-6 mb-3">Sign up</h1>
        <Formik
          initialValues={{
            userName: "",
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
                  id="userName"
                  name="userName"
                  placeholder="Username"
                  type="userName"
                  value={values.userName}
                  onChange={(e:any) => setFieldValue('userName', e.target.value)}
                />
                 {errors.userName && touched.userName ? (
                  <div><p>{errors.userName}</p></div>
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
                  onChange={(e:any) => setFieldValue('email', e.target.value)}
                />
              {errors.email && touched.email ? <div>{errors.email}</div> : null}
              </div>
              <div className="mb-3">
                <Field
                  className="form-control"
                  id="password"
                  name="password"
                  placeholder="Password"
                  type="password"
                  value={values.password}
                  onChange={(e:any) => setFieldValue('password', e.target.value)}
                />
               {errors.password && touched.password ? (
                <div>{errors.password}</div>
                ) : null}
              </div>

              <button type="submit" disabled={!(isValid && dirty)} className={`btn ${styles.login_button}`}>
                Submit 
              </button>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
