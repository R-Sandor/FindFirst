import { expect, test } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/signup/page";

const user = userEvent.setup();
const goodEmail = "jsmith@gmail.com";
const goodUsername = "jsmith";
const goodPassword = "super_h@rd_p@$$w0rd";
const badUsername = "js";
const badEmail = "j-dog.com";
const badPassword = "test";

type UEP = {
  usernameInput: HTMLInputElement;
  emailInput: HTMLInputElement;
  passwordInput: HTMLInputElement;
};

function getUsernameEmailPassword(): UEP {
  const usernameInput = screen.getByPlaceholderText(/Username/i);
  const emailInput = screen.getByPlaceholderText(/Email/i);
  const passwordInput = screen.getByPlaceholderText(/Password/i);

  return {
    usernameInput: usernameInput,
    emailInput: emailInput,
    passwordInput: passwordInput,
  } as UEP;
}

function clickAway() {
  const rootElement = document.documentElement;
  user.click(rootElement);
}

async function typeUsername(usernameInput: HTMLElement, username: string) {
  await user.type(usernameInput, username);
}

async function typeEmail(emailInput: HTMLElement, email: string) {
  await user.type(emailInput, email);
}

async function typePassword(passwordInput: HTMLElement, pwd: string) {
  await user.type(passwordInput, pwd);
}

async function typeUEP(username: string, email: string, password: string) {
  const uep = await getUsernameEmailPassword();
  await typeUsername(uep.usernameInput, username);
  await typeEmail(uep.emailInput, email);
  await typePassword(uep.passwordInput, password);
}

function submitDisabled(isDisabled: Boolean): HTMLButtonElement {
  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(isDisabled);
  return submitBtn;
}

test("Forms allow input", () => {
  render(<Page />);
  const form = screen.getAllByRole("textbox") as HTMLInputElement[];
  expect(form[0].value).toBe("");
  expect(form[1].value).toBe("");
  const password = screen.getByPlaceholderText(/password/i) as HTMLInputElement;
  expect(password.value).toBe("");
});

test("Should be able to type an userName", async () => {
  render(<Page />);
  const uep = getUsernameEmailPassword();
  const username = "jsmith";
  expect(uep.usernameInput.value).toBe("");
  await typeUsername(uep.usernameInput, username);
  expect(uep.usernameInput.value).toBe(username);
});

test("Should be able to type an email", async () => {
  render(<Page />);
  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  const email = "jsmith@gmail.com";
  expect(emailInput.value).toBe("");
  await typeEmail(emailInput, email);
  expect(emailInput.value).toBe(email);
});

test("Should be able to type an password", async () => {
  render(<Page />);
  const passwordInput = screen.getByPlaceholderText(
    /Password/i
  ) as HTMLInputElement;
  const password = "test";
  expect(passwordInput.value).toBe("");
  await user.type(passwordInput, password);
  expect(passwordInput.value).toBe(password);
});

test("Submit button should be disabled by default until errors are resolved", async () => {
  render(<Page />);
  submitDisabled(true);
});
//
test("Username should have an error and button disabled", async () => {
  render(<Page />);
  await typeUEP(badUsername, goodEmail, goodPassword);
  const usernameError = screen.getByText(/username too short/i);
  expect(usernameError).toBeInTheDocument();
  submitDisabled(true);
});

test("Email should have an error", async () => {
  render(<Page />);
  await typeUEP(goodUsername, badEmail, goodPassword);

  const emailError = screen.getByText(/Invalid email/i);
  expect(emailError).toBeInTheDocument();
  submitDisabled(true);
});

test("Password should have an error", async () => {
  render(<Page />);
  const uep = getUsernameEmailPassword();
  await typeUEP(goodUsername, goodEmail, "test");
  await user.type(uep.usernameInput, goodUsername);

  submitDisabled(true);

  let pwdErr = screen.getByText(/password too short/i);
  expect(pwdErr).toBeInTheDocument();
  await user.type(uep.passwordInput, "testtest");
  // to simulate user clicking off of textfield
  await clickAway();

  pwdErr = screen.getByText(/special character/i);
  expect(pwdErr).toBeInTheDocument();
});

test("All fields should have an error", async () => {
  render(<Page />);
  await typeUEP(badUsername, badEmail, badPassword);

  const usernameError = screen.getByText(/username too short/i);
  expect(usernameError).toBeInTheDocument();
  await clickAway();
  const emailError = screen.getByText(/Invalid email/i);
  expect(emailError).toBeInTheDocument();
  submitDisabled(true);
});

test("All field are set, user should be able to submit", async () => {
  render(<Page />);
  const usernameInput = screen.getByPlaceholderText(
    /Username/i
  ) as HTMLInputElement;
  const badUserNameJS = "jsmith";
  await user.type(usernameInput, badUserNameJS);
  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  await user.type(emailInput, "jsmith@gmail.com");
  const passwordInput = screen.getByPlaceholderText(
    /Password/i
  ) as HTMLInputElement;
  await user.type(passwordInput, "super_h@rd_p@$$w0rd");

  const submitBtn = submitDisabled(false);
  await user.click(submitBtn);

  // const goodMsg = screen.getByText(/Please complete/i);
  // expect(goodMsg).toBeInTheDocument();
});
