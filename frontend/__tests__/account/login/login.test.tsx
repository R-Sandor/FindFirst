import { beforeEach, describe, expect, test, vi } from "vitest";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/login/page";
import MockAdapter from "axios-mock-adapter";
import { typePassword, typeUsername } from "../signup/signup.test";
import axios from "axios";
import RootLayout from "@/app/layout";
import { submitDisabled } from "@/__tests__/utilities/TestingUtilities";
const user = userEvent.setup();

describe("Login events.", () => {
  vi.mock("next/navigation", () => {
    const actual = vi.importActual("next/navigation");
    return {
      ...actual,
      useRouter: vi.fn(() => ({
        push: vi.fn(),
      })),
      useSearchParams: vi.fn(() => ({
        get: vi.fn(),
      })),
      usePathname: vi.fn().mockImplementation(() => "/account/login/"),
    };
  });

  beforeEach(() => {
    act(() => {
      // eslint-disable-next-line react/no-children-prop
      render(
        <RootLayout>
          <Page />
        </RootLayout>,
      );
    });
  });
  test("User can login with a valid username password", async () => {
    // Mock recieving a 200 on the return from the server.
    const axiosMock = new MockAdapter(axios);
    const SIGNIN_URL = "http://localhost:9000/user/signin";
    const expectedResult = {
      tokenType: "Bearer",
      refreshToken: "5c3de962-950a-4633-91fa-90cc45f12a9d",
    };
    axiosMock.onPost(SIGNIN_URL).reply((config) => {
      return [
        200,
        expectedResult,
        {
          "Content-type": "application/x-www-form-urlencoded",
        },
      ];
    });
    const submitBtn = screen.getAllByRole("button", {
      name: "Login",
    })[1];
    await act(async () => {
      await typeUsername("j-dog");
      await typePassword("$t3ves_$uperh@rd_P@$$w0rd");

      await user.click(submitBtn);
    });
  });

  test("User login failed  invalid username password"),
    () => {
      // Mock recieving a 400 on the return from the server.
    };
});

describe("Errors on fields.", () => {
  beforeEach(() => {
    act(() => {
      render(<Page />);
    });
  });
  test("Submit button should be disabled no usename or password.", async () => {
    submitDisabled(true, "Login");
  });
});
