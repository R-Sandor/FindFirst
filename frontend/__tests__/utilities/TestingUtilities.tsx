import { UserEvent } from "@testing-library/user-event";
import { expect } from "vitest";
import { screen } from "@testing-library/react";

export async function clickAway(user: UserEvent) {
  const rootElement = document.documentElement;
  await user.click(rootElement);
}

export function submitDisabled(
  isDisabled: Boolean,
  text?: string
): HTMLButtonElement {
  const txt = text ? text : "Submit";
  const submitBtn = screen
    .getByRole("button", {
      name: txt,
    })
    .closest("button") as HTMLButtonElement;
  expect(submitBtn.disabled).toBe(isDisabled);
  return submitBtn;
}