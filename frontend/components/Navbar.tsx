"use client";
import {
  Navbar,
  Nav,
  NavDropdown,
  Form,
  Button,
  ButtonGroup,
} from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import { useEffect, useState } from "react";

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();
  const [authorized, setAuthorized] = useState<AuthStatus>();

  useEffect(() => {
    setAuthorized(authService.getAuthorized());
  }, []);


  const router = useRouter();
  function authButton() {
    console.log("User loggedin " + userAuth);
    if (userAuth == AuthStatus.Unauthorized || userAuth === undefined) {
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
      bg="dark"
      variant="dark"
      className="mb-3"
    >
      <Navbar.Brand onClick={() => router.push("/")} className="mx-3">
        FindFirst
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
      <Form className="mx-3">
        {authButton()}
      </Form>
    </Navbar>
  );
};

export default GlobalNavbar;
function setAuthorized(arg0: AuthStatus) {
  throw new Error("Function not implemented.");
}

