import { expect, test } from 'vitest'
import { render, screen } from '@testing-library/react'
import Page from "app/account/signup/page"
 

/**
 * User should be able to enter in the form. 
 */
test("Forms allow input", () => {
  render(<Page />)
  const emailInputElement = screen.getByRole("textbox");
  expect(emailInputElement.value).toBe("");

})

// User should be able to enter in an email and it be valid
  // OTHERWISE: ERROR, Submit is not allowed. 
  /**
   * enter in email in email box that is invalid
   * assert that that error window or form is in error state. 
   * assert that submit button is not enabled.
   */

  // 