import { UserEvent } from "@testing-library/user-event";
import { expect } from "vitest";
import { act, fireEvent, screen } from "@testing-library/react";
import { async } from "rxjs";
import { hitKey } from "./fireEvents";

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

/**
 * Backspaces on a field a given number of times. If number of times is not
 * provided backspace once.
 */
export async function backSpaceOnField(
  field: HTMLElement,
  repeat: number | undefined | null,
) {
  if (repeat == undefined || repeat == null) {
    repeat = 1;
  }
  let val = field.getAttribute("value");
  console.log("the val is", val);
  for (let i = 0; i < repeat; i++) {
    await act(async () => {
      hitKey(field, "backspace", "backspace", 8, 8);

      if (val) {
        val = val.slice(0, val.length - 1);
      }
      fireEvent.change(field, {
        target: { value: val },
      });
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
