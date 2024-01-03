import { expect, test } from "vitest";
import { getByText, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/signup/page";
import { timeout } from "rxjs";
const user = userEvent.setup();
/**
 * Input forms should all be empty intially.
 */
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
  const usernameInput = screen.getByPlaceholderText(
    /Username/i
  ) as HTMLInputElement;
  const username = "jsmith";
  expect(usernameInput.value).toBe("");
  await user.type(usernameInput, username);
  expect(usernameInput.value).toBe(username);
});

test("Should be able to type an email", async () => {
  render(<Page />);
  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  const email = "jsmith@gmail.com";
  expect(emailInput.value).toBe("");
  await user.type(emailInput, email);
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
  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(true);
});

test("Username should have an error and button disabled", async () => {
  render(<Page />);
  const usernameInput = screen.getByPlaceholderText(
    /Username/i
  ) as HTMLInputElement;
  const badUserNameJS = "js";

  await user.type(usernameInput, badUserNameJS);

  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  await user.type(emailInput, "jsmith@gmail.com");

  const passwordInput = screen.getByPlaceholderText(
    /Password/i
  ) as HTMLInputElement;

  await user.type(passwordInput, "super_h@rd_p@$$w0rd");

  const usernameError = screen.getByText(/username too short/i);
  expect(usernameError).toBeInTheDocument();

  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(true);
});

test("Email should have an error", async () => {
  render(<Page />);
  const usernameInput = screen.getByPlaceholderText(
    /Username/i
  ) as HTMLInputElement;
  const goodUsername = "j-dog";
  await user.type(usernameInput, goodUsername);

  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  await user.type(emailInput, "j-dog.com");

  const passwordInput = screen.getByPlaceholderText(
    /Password/i
  ) as HTMLInputElement;
  await user.type(passwordInput, "$tev3s_sup3rH@rdPassword");

  const emailError = screen.getByText(/Invalid email/i);
  expect(emailError).toBeInTheDocument();

  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(true);
});

test("Password should have an error", async () => {
  render(<Page />);
  const usernameInput = screen.getByPlaceholderText(
    /Username/i
  ) as HTMLInputElement;

  const emailInput = screen.getByPlaceholderText(/Email/i) as HTMLInputElement;
  await user.type(emailInput, "j-dog@gmail.com");

  const passwordInput = screen.getByPlaceholderText(/Password/i);
  await user.type(passwordInput, "test");

  const goodUsername = "j-dog";
  await user.type(usernameInput, goodUsername);

  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(true);

  let pwdErr = screen.getByText(/password too short/i);
  expect(pwdErr).toBeInTheDocument();

  await user.type(passwordInput, "testtest");
  // to simulate user clicking off of textfield
  const rootElement = document.documentElement;
  user.click(rootElement);

  pwdErr = screen.getByText(/special character/i);
  expect(pwdErr).toBeInTheDocument();
});

test("All fields should have an error", async () => {
  render(<Page />);
  const usernameInput = screen.getByPlaceholderText(/Username/i);
  const passwordInput = screen.getByPlaceholderText(/Password/i);
  const emailInput = screen.getByPlaceholderText(/Email/i);

  const badUsername = "js";
  await user.type(usernameInput, badUsername);
  await user.type(emailInput, "j-dog.com");
  await user.type(passwordInput, "test");
  const usernameError = screen.getByText(/username too short/i);
  expect(usernameError).toBeInTheDocument();
  const emailError = screen.getByText(/Invalid email/i);
  expect(emailError).toBeInTheDocument();

  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(true);
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

  const submitBtn = screen
    .getByText(/submit/i)
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(false);
});
