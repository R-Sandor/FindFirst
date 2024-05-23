"use client";
import { Formik, Field, Form } from "formik";
import styles from "./login-form.module.scss";
import authService from "@/services/auth.service";
import * as Yup from "yup";
import { useRouter } from "next/navigation";

export interface credentials {
  username: string;
  password: string;
}

export default function Page() {
  const router = useRouter();
  const handleOnSubmit = async (credentials: credentials, actions: any) => {
    if (await authService.login(credentials)) {
      router.push("/");
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
              <button
                type="submit"
                disabled={!(isValid && dirty)}
                className={`btn ${styles.signin_button}`}
              >
                Login
              </button>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
