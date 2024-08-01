import PasswordReset from "@/app/account/resetPassword/page";
import { render } from "@testing-library/react";
import { act } from "react-dom/test-utils";
import { beforeEach, beforeAll, vi, describe, it } from "vitest";
import { debug } from "vitest-preview";

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

describe('User resets password', () => { 
    it('Reset Password', () => { 
    })
})