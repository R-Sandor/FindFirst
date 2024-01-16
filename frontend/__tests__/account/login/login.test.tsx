import { beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/login/page";
import {
  submitDisabled,
  typePassword,
  typeUsername,
} from "../signup/signup.test";
import { debug } from "vitest-preview";
const user = userEvent.setup();

describe("Login events.", () => {
  beforeEach(() => {
    render(<Page />);
  });
  test("User can login with a valid username password", async () => {
    // Mock recieving a 200 on the return from the server.
    await typeUsername("j-dog");
    await typePassword("$t3ves_$uperh@rd_P@$$w0rd");
    debug();
    await user.click(submitDisabled(false, 'Login'));
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
    submitDisabled(true, 'Login');
  });
});
