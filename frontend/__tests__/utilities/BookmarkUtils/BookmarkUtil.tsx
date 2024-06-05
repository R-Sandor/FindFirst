import { UserEvent } from "@testing-library/user-event";
import { hitEnter } from "../fireEvents";
import { screen } from "@testing-library/react";

async function populateTags(tags: string[], user: UserEvent) {
  const tagForm = screen.getByPlaceholderText("Enter a tag");
  for (let i = 0; i < tags.length; i++) {
    await user.type(tagForm, tags[i]);
    hitEnter(tagForm);
    // the testing-library is not perfect...
    await user.clear(tagForm);
  }
}

export { populateTags };
