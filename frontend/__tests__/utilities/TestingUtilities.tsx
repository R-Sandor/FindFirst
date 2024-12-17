import { UserEvent } from "@testing-library/user-event";
import { expect } from "vitest";
import { act, screen } from "@testing-library/react";

export async function clickAway(user: UserEvent) {
  const rootElement = document.documentElement;
  await act(() => user.click(rootElement));
}

export async function click(user: UserEvent, element: HTMLElement) {
  await act(() => user.click(element));
}

export async function type(
  user: UserEvent,
  inputBox: HTMLElement,
  text: string,
) {
  await act(() => user.type(inputBox, text));
}

export function submitDisabled(
  isDisabled: Boolean,
  text?: string,
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

