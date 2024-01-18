import { beforeAll, beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import PasswordReset from "@/app/account/resetPassword/[token]/page";
import { debug } from "vitest-preview";
import RootLayout from "@/app/layout";

describe("Password reset handling", () => {
  beforeAll(() => {
    vi.mock("next/navigation", async (importOriginal) => {
      const actual = (await importOriginal()) as Object;
      return {
        ...actual,
        useRouter: vi.fn(() => ({
          push: vi.fn(),
        })),
        // giberish token
        useParams: vi.fn().mockImplementation(() => "12ds2-45434ds-1232334"),
        usePathname: vi.fn().mockImplementation(() => "/account/login/"),
      };
    });
  });

  beforeEach(() => {
    render(
      <RootLayout>
        <PasswordReset />
      </RootLayout>
    );
  });

  test("Password input fields exist", async () => {
    debug();
    const inputs = screen.getAllByPlaceholderText(/Password/i);
    const pwd = inputs[0];
    const confirmPwd = inputs[1];
    expect(pwd).toBeInTheDocument();
    expect(confirmPwd).toBeInTheDocument();
  });

  // TODO handle token expiration
});
