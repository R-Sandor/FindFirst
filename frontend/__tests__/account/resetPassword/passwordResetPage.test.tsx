import { clickAway } from "@/__tests__/utilities/TestingUtilities";
import PasswordReset from "@/app/account/resetPassword/page";
import { act, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { beforeEach, beforeAll, vi, describe, it, expect } from "vitest";
const user = userEvent.setup();

beforeEach(async () => {
  await act(async () => {
    render(<PasswordReset />);
  });
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
      usePathname: vi.fn().mockImplementation(() => "/account/login/"),
    };
  });
});

describe("User attempts reset password:", () => {
  it("User enters invalid email", async () => {
    const email = screen.getByPlaceholderText(/email/i);
    await act(async () => {
      await user.type(email, "ajacobs");
      await clickAway(user);
    });
    expect(screen.getByText("Invalid email")).toBeInTheDocument();
    await act(async () => {
      await user.clear(email);
    });
    expect(screen.getByText("Required")).toBeInTheDocument();
  });
  it("User enters an email that does not exist.", async () => {
    const email = screen.getByPlaceholderText(/email/i);

    // This sets the mock adapter on the default instance
    let mock = new MockAdapter(axios);
    mock.onPost().reply(400, {
      error: "User does not exist",
    });
    await act(async () => {
      await user.type(email, "ajacobs@gmail.com");
      await user.click(screen.getByText(/submit/i));
    });
    await waitFor(() => {
      expect(screen.getByText("User does not exist")).toBeInTheDocument();
    });
  });
  it("User requests valid password reset for email.", async () => {
    const email = screen.getByPlaceholderText(/email/i);
    // This sets the mock adapter on the default instance
    let mock = new MockAdapter(axios);
    mock.onPost().reply(200, "Password reset sent");
    await act(async () => {
      await user.type(email, "ajacobs@gmail.com");
      await user.click(screen.getByText(/submit/i));
    });
  });
});
