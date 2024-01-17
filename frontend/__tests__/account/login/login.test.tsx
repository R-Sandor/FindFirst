import { beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/login/page";
import MockAdapter from "axios-mock-adapter";
import {
  submitDisabled,
  typePassword,
  typeUsername,
} from "../signup/signup.test";
import { debug } from "vitest-preview";
import axios from "axios";
const user = userEvent.setup();

describe("Login events.", () => {
  beforeEach(() => {
    render(<Page />);
  });
  test("User can login with a valid username password", async () => {
    // Mock recieving a 200 on the return from the server.
    const axiosMock = new MockAdapter(axios);
    const SIGNIN_URL = "http://localhost:9000/user/signin";
    const expectedResult = {
      tokenType: "Bearer",
      refreshToken: "5c3de962-950a-4633-91fa-90cc45f12a9d",
    };
    axiosMock.onPost(SIGNIN_URL).reply((config) => {
      return [
        200,
        expectedResult,
        {
          "Content-type": "application/x-www-form-urlencoded",
        },
      ];
    });
    await typeUsername("j-dog");
    await typePassword("$t3ves_$uperh@rd_P@$$w0rd");
    await user.click(submitDisabled(false, "Login"));
  });

  test("User login failed  invalid username password"),
    () => {
      // Mock recieving a 400 on the return from the server.
    };
});

describe("Errors on fields.", () => {
  beforeEach(() => {
    render(<Page />);
  });
  test("Submit button should be disabled no usename or password.", async () => {
    submitDisabled(true, "Login");
  });
});
