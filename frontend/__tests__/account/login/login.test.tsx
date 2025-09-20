import { beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Page from "app/account/login/page";
import MockAdapter from "axios-mock-adapter";
import { typePassword } from "../signup/signup.test";
import { submitDisabled } from "@/__tests__/utilities/TestingUtilities";
import { bkmkResp } from "@/__tests__/data/SampleData";
import { instance } from "@api/Api";
import authService from "@services/auth.service";
import axios from "axios";
import { userApiInstance } from "@api/userApi";
const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
const user = userEvent.setup();
const userApiMock = new MockAdapter(userApiInstance);
userApiMock.onGet("/oauth2Providers").reply(200, JSON.stringify([]));

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
    // eslint-disable-next-line react/no-children-prop
    render(<Page />);
  });

  test("User can login with a valid username password", async () => {
    // Mock recieving a 200 on the return from the server.
    const axiosMock = new MockAdapter(instance);
    const mock = new MockAdapter(axios);

    // Create a custom response
    axiosMock.onGet().reply(200, bkmkResp);

    const SIGNIN_URL = SERVER_URL + "/user/signin";
    const expectedResult = {
      tokenType: "Bearer",
      refreshToken: "5c3de962-950a-4633-91fa-90cc45f12a9d",
    };
    mock.onPost(SIGNIN_URL).reply(() => {
      return [
        200,
        expectedResult,
        {
          "Content-type": "application/x-www-form-urlencoded",
        },
      ];
    });

    const submitBtn = screen.getByTestId("login-btn");
    const username = screen.getByPlaceholderText(/Username/i);
    await user.type(username, "j-dog");
    await typePassword("$t3ves_$uperh@rd_P@$$w0rd");

    await user.click(submitBtn);
  });

  test("User login failed invalid username password", async () => {
    // Mock recieving a 200 on the return from the server.
    const axiosMock = new MockAdapter(instance);
    // Create a custom response

    axiosMock.onGet().reply(401, "Invalid username or password");

    vi.spyOn(authService, "getAuthorized").mockImplementation(() => 0);
    vi.spyOn(authService, "login").mockImplementation(async () => false);

    const submitBtn = screen.getByTestId("login-btn");
    const username = screen.getByPlaceholderText(/Username/i);
    await user.type(username, "j-dog");
    await typePassword("$t3ves_$uperh@rd_P@$$w0rd");
    await user.click(submitBtn);
    await user.click(submitBtn);
    await user.click(submitBtn);
    const forgot = screen.getByText("Forgot Password?");
    expect(forgot).toBeInTheDocument();
    await user.click(forgot);
  });
});

describe("Errors on fields.", () => {
  beforeEach(() => {
    render(<Page />);
  });
  test("Submit button should be disabled no usename or password.", async () => {
    submitDisabled(true, "Login");
  });
});

// describe("Oauth2 Signin", () => {
//   beforeEach(() => {
//     render(<Page />);
//   });
//   test("Oauth2Providers are listed", async () => {});
// });
