"use client";
import Link from "next/link";
import {
  Navbar,
  Nav,
  NavDropdown,
  Form,
  Button,
  ButtonGroup,
} from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthObserver, AuthStatus } from "@/services/auth.service";
import { useEffect, useState } from "react";

const MENU_LIST = [
  { text: "Home", href: "/" },
  { text: "Guide", href: "/guide" },
];
export const GlobalNavbar: React.FC = () => {
  const [authorized, setAuthorized] = useState<AuthStatus>();

  const onAuthUpdated: AuthObserver = (authState: AuthStatus) => {
    setAuthorized(authState);
  }

  useEffect(() => {
    setAuthorized(authService.getAuthorized())
    authService.attach(onAuthUpdated)

    return () => authService.detach(onAuthUpdated)
  }, [])

  const router = useRouter();
  function authButton() {
    console.log(authorized)
    if ( authorized == AuthStatus.Unauthorized) {
      return (
        <ButtonGroup>
          <Button
            variant="secondary"
            onClick={() => router.push("/account/login")}
          >
            Login
          </Button>
          <Button
            variant="secondary"
            onClick={() => router.push("/account/signup")}
          >
            Signup
          </Button>
        </ButtonGroup>
      );
    } else {
      return (
        <Button variant="secondary" onClick={handleLogoutClick}>
          Logout
        </Button>
      );
    }
  }
  const handleLogoutClick = () => {
    authService.logout();
    router.push("/account/login");
  };

  return (
    <Navbar
      collapseOnSelect
      expand="sm"
      bg="light"
      variant="light"
      className="mb-3"
    >
      <Navbar.Brand onClick={() => router.push("/")} className="mx-3">
        BookMarkIt
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link onClick={() => router.push("/")}>Discover</Nav.Link>
          <NavDropdown title="Collection" id="collasible-nav-dropdown">
            <NavDropdown.Item onClick={() => router.push("/")}>
              Tags 
            </NavDropdown.Item>
            <NavDropdown.Item onClick={() => router.push("/")}>
              Reading List
            </NavDropdown.Item>
          </NavDropdown>
        </Nav>
      </Navbar.Collapse>
      <Form inline="true" className="mx-3">
        {authButton()}
      </Form>
    </Navbar>
  );
};

export default GlobalNavbar;