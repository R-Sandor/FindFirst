import { beforeAll, beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import PasswordReset from "@/app/account/resetPassword/[token]/page";
import {
  clickAway,
  submitDisabled,
} from "@/__tests__/utilities/TestingUtilities";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import TokenPassword from "@type/account/TokenPassword";
const user = userEvent.setup();

function getPasswordFields() {
  const inputs = screen.getAllByPlaceholderText(/Password/i);
  const pwd = inputs[0];
  const confirmPwd = inputs[1];
  return { pwd, confirmPwd };
}

interface Params {
  token: string;
}

const myParams: Params = {
  token: "12ds2-45434ds-1232334",
};

beforeEach(async () => {
  render(<PasswordReset />);
});

beforeAll(() => {
  vi.mock("next/navigation", async (importOriginal) => {
    const actual = (await importOriginal()) as Object;
    return {
      ...actual,
      useRouter: vi.fn(() => ({
        push: vi.fn(),
      })),
      // giberish token
      useParams: vi.fn().mockImplementation(() => myParams),
      usePathname: vi.fn().mockImplementation(() => "/account/login/"),
    };
  });
});

describe("Password field handling", () => {
  test("Password input fields exist.", async () => {
    const fields = getPasswordFields();
    expect(fields.pwd).toBeInTheDocument();
    expect(fields.confirmPwd).toBeInTheDocument();
  });

  test("Password too short.", async () => {
    const fields = getPasswordFields();
    await user.type(fields.pwd, "Test");
    await clickAway(user);
    expect(screen.getByText("Password too short!")).toBeInTheDocument();
    await user.type(fields.pwd, "Test");
    expect(screen.getByText(/must contain/i)).toBeInTheDocument();
    await user.type(fields.pwd, "!");
    expect(screen.queryByText(/must contain/i)).not.toBeInTheDocument();
    submitDisabled(true);
  });

  test("Passwords don't match.", async () => {
    const fields = getPasswordFields();
    await user.type(fields.pwd, "TestTest!");
    await user.type(fields.confirmPwd, "Test");
    await clickAway(user);
    expect(screen.getByText(/must match/i)).toBeInTheDocument();
    await user.type(fields.confirmPwd, "Test!");
    expect(screen.queryByText(/must match/i)).not.toBeInTheDocument();
    submitDisabled(false);
  });
});

describe("Submission handling.", () => {
  test("Successful reset.", async () => {
    const axiosMock = new MockAdapter(axios);
    const resetUrl = "http://localhost:9000/user/changePassword";
    const expectedResult = {
      text: "Password changed",
    };

    const tknPwd: TokenPassword = {
      token: myParams.token,
      password: "TestTest!",
    };

    axiosMock.onPost(resetUrl, tknPwd).reply(() => {
      return [
        200,
        expectedResult,
        {
          "Content-type": "application/x-www-form-urlencoded",
        },
      ];
    });
    const fields = getPasswordFields();
    await user.type(fields.pwd, "TestTest!");
    await user.type(fields.confirmPwd, "TestTest!");
    await user.click(submitDisabled(false));
  });

  test("Unsuccessful reset.", async () => {
    const axiosMock = new MockAdapter(axios);
    const resetUrl = "http://localhost:9000/user/changePassword";
    const expectedResult = {
      text: "Error: Token or password, try again.",
    };

    const tknPwd: TokenPassword = {
      token: myParams.token,
      password: "TestTest!",
    };
    axiosMock.onPost(resetUrl, tknPwd).reply(() => {
      return [
        400,
        expectedResult,
        {
          "Content-type": "application/x-www-form-urlencoded",
        },
      ];
    });

    const fields = getPasswordFields();
    await user.type(fields.pwd, "TestTest!");
    await user.type(fields.confirmPwd, "TestTest!");
    await user.click(submitDisabled(false));
    expect(
      await screen.findByText(/Error/i, undefined, { timeout: 3000 }),
    ).toBeInTheDocument();
  });
});
