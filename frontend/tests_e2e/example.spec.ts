import { test, expect } from '@playwright/test';

test.describe("Composed", () => {

  test("compose works", async ({ page }) => {
    await page.goto('localhost:3000');
    await expect(page.getByRole('heading', { name: 'Login' })).toBeVisible();
  });

  test("User can login", async ({ page }) => {
    await page.goto('localhost:3000');
    await page.getByRole('textbox', { name: 'Username' }).click();
    await page.keyboard.type('jsmith');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.keyboard.type('test');
    await page.getByTestId('login-btn').click();
    await expect(page.getByText(/add bookmark/i)).toBeVisible();
  })
});

