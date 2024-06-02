import { fireEvent } from "@testing-library/react";

async function hitEnter(element: Element) {
  fireEvent.keyDown(element, {
    key: "Enter",
    code: "Enter",
    keyCode: 13,
    charCode: 13,
  });
}

async function hitKey(
  element: Element,
  key: string,
  code: string,
  keyCode: number,
  charCode: number,
) {
  fireEvent.keyDown(element, {
    key: key,
    code: code,
    keyCode: keyCode,
    charCode: charCode,
  });
}

export { hitKey, hitEnter };
