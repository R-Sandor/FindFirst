import OauthLogin from "@/app/account/login/oauth2/page";
import authService from "@services/auth.service";
import { render, waitFor } from "@testing-library/react";
import { describe } from "node:test";
import { expect, it, vi } from "vitest";

vi.mock("next/navigation", async (importOriginal) => {
  const actual = (await importOriginal()) as Object;
  return {
    ...actual,
    useRouter: vi.fn(() => ({
      push: vi.fn(),
    })),
    // giberish token
    useParams: vi.fn(),
    usePathname: vi.fn().mockImplementation(() => "/account/login/"),
  };
});

vi.mock(
  "@services/auth.service",
  async (
    importOriginal: () => Promise<typeof import("@services/auth.service")>,
  ) => {
    const actual = await importOriginal();
    return {
      __esModule: true,
      ...actual,
      default: {
        ...actual.default,
        logout: vi.fn(),
        getUser: vi.fn().mockImplementation(() => {
          return {
            id: 1,
            username: "test",
            refreshToken: "test",
            profileImage: "",
          };
        }),

        getUserInfoOauth2: vi.fn(() =>
          Promise.resolve({
            username: "test",
            id: 123,
            refreshToken: "",
          }),
        ),
        AuthStatus: {
          Unauthorized: "Unauthorized",
          Authorized: "Authorized",
        },
      },
    };
  },
);

describe("Test oauth2", () => {
  it("Push route", async () => {
    render(<OauthLogin></OauthLogin>);

    await waitFor(() => {
      expect(authService.getUserInfoOauth2).toHaveBeenCalledTimes(1);
    });
  });
});
