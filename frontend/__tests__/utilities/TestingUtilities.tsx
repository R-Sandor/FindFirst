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
  await act(async () => await user.type(inputBox, text));
}

/**
 * Backspaces on a field a given number of times. If number of times is not
 * provided backspace once.
 */
export async function backSpaceOnField(
  user: UserEvent,
  field: HTMLElement,
  repeat: number | undefined | null,
) {
  // delete entire word
  if (repeat == undefined || repeat == null) {
    let input = field.getAttribute("value")?.trim();
    if (input) {
      repeat = input.substring(input.lastIndexOf(" "), input.length).length;
    } else {
      repeat = 1;
    }
  }
  for (let i = 0; i < repeat; i++) {
    await act(async () => {
      await user.type(field, "{backspace}");
    });
  }
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
