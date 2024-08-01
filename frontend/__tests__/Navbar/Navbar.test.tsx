/// <reference types="vitest" />
import { vi, beforeEach, afterEach, describe, expect, it } from "vitest";
import { render, screen, act, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import GlobalNavbar from "@components/Navbar/Navbar";
import { useRouter } from "next/navigation";
import useAuth from "@components/UseAuth";
import authService, { AuthStatus } from "@services/auth.service";
import exp from "node:constants";

const user = userEvent.setup();

// Mock the necessary modules
vi.mock('next/navigation', () => ({
  useRouter: vi.fn(),
}));

vi.mock('@components/UseAuth', () => ({
  default: vi.fn(),
}));

vi.mock('@services/auth.service', async (importOriginal: () => Promise<typeof import('@services/auth.service')>) => {
  const actual = await importOriginal();
  return {
    __esModule: true,
    ...actual,
    default: {
      ...actual.default,
      logout: vi.fn(),
      AuthStatus: {
        Unauthorized: 'Unauthorized',
        Authorized: 'Authorized',
      },
    },
  };
});

type MockedFunction<T extends (...args: any[]) => any> = ReturnType<typeof vi.fn> & { mockReturnValue: (value: ReturnType<T>) => void };

describe('GlobalNavbar', () => {
  const mockPush = vi.fn();
  const mockLogout = vi.fn();

  beforeEach(() => {
    (useRouter as MockedFunction<typeof useRouter>).mockReturnValue({ push: mockPush });
    (authService.logout as MockedFunction<typeof authService.logout>).mockImplementation(mockLogout);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('renders the navbar with brand logo', () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const brandLogo = screen.getByAltText('React Bootstrap logo');
    expect(brandLogo).toBeInTheDocument();
  });

  it('renders login and signup buttons when user is unauthorized', () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const loginButton = screen.getByText('Login');
    const signupButton = screen.getByText('Signup');
    expect(loginButton).toBeInTheDocument();
    expect(signupButton).toBeInTheDocument();
  });

  it('renders logout button when user is authorized', () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Authorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const logoutButton = screen.getByText('Logout');
    expect(logoutButton).toBeInTheDocument();
  });

  it('calls router.push on login button click', async () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const loginButton = screen.getByText('Login');
    await act(async () => {
      await user.click(loginButton);
    });
    expect(mockPush).toHaveBeenCalledWith('/account/login');
  });

  it('calls router.push on signup button click', async () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const signupButton = screen.getByText('Signup');
    await act(async () => {
      await user.click(signupButton);
    });
    expect(mockPush).toHaveBeenCalledWith('/account/signup');
  });

  it('calls authService.logout and router.push on logout button click', async () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Authorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const logoutButton = screen.getByText('Logout');
    await act(async () => {
      await user.click(logoutButton);
    });
    expect(authService.logout).toHaveBeenCalled();
    expect(mockPush).toHaveBeenCalledWith('/account/login');
  });

  it('renders ImportModal, Export, and Search Bar components when user is authorized', () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Authorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const importModal = screen.getByTestId('import-modal'); // Test ID for ImportModal
    const exportComponent = screen.getByTestId('export-button'); // Test ID for Export
    const searchBar = screen.getByTestId('search-input'); // Test ID for Search Bar
    const searchButton = screen.getByTestId('search-button'); // Test ID for Search Button
    expect(importModal).toBeInTheDocument();
    expect(exportComponent).toBeInTheDocument();
    expect(searchBar).toBeInTheDocument();
    expect(searchButton).toBeInTheDocument();
  });

  it('does not render ImportModal, Export, and Search Bar components when user is unauthorized', () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const importModal = screen.queryByTestId('import-modal'); // Test ID for ImportModal
    const exportComponent = screen.queryByTestId('export-button'); // Test ID for Export
    const searchBar = screen.queryByTestId('search-input'); // Test ID for Search Bar 
    const searchButton = screen.queryByTestId('search-button'); // Test ID for Search Button
    expect(importModal).toBeNull();
    expect(exportComponent).toBeNull();
    expect(searchBar).toBeNull();
    expect(searchButton).toBeNull();
  });

  it('calls router.push on brand logo click', async () => {
    (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(AuthStatus.Unauthorized);
    act(() => {
      render(<GlobalNavbar />);
    });
    const brandLogo = screen.getByAltText('React Bootstrap logo');
    await act(async () => {
      await user.click(brandLogo);
    });
    expect(mockPush).toHaveBeenCalledWith('/');
  });

  it("renders the search bar", () => {
    render(<GlobalNavbar />);
    const searchInput = screen.getByTestId("search-input");
    const searchButton = screen.getByTestId("search-button");

    expect(searchInput).toBeInTheDocument();
    expect(searchButton).toBeInTheDocument();
  });

  it("calls the search function when the search button is clicked", () => {
    render(<GlobalNavbar />);
    const searchInput = screen.getByTestId("search-input");
    const searchButton = screen.getByTestId("search-button");

    fireEvent.change(searchInput, { target: { value: "test search" } });
    fireEvent.click(searchButton);
  });
});
