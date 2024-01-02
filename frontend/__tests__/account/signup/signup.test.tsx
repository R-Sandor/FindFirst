import { expect, test } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from '@testing-library/user-event'
import Page from "app/account/signup/page";


const user = userEvent.setup()
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

test("Should be able to type an userName", () => {
  render(<Page />);
  const form = screen.getAllByRole("textbox") as HTMLInputElement[];
  const usernameInput = form[0];
  expect(form[0].value).toBe("");
  const userName = "jsmith";
  user.type(usernameInput, userName)
  expect(usernameInput.value).toBe(userName);
});

// User should be able to enter in an email and it be valid
// OTHERWISE: ERROR, Submit is not allowed.
/**
 * enter in email in email box that is invalid
 * assert that that error window or form is in error state.
 * assert that submit button is not enabled.
 */

//
