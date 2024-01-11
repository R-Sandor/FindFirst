"use client";
import {
  Navbar,
  Nav,
  NavDropdown,
  Form,
  Button,
  ButtonGroup,
  Container,
} from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();

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
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container>
        <Navbar.Brand onClick={() => router.push("/")} className="mx-3 cursor-pointer">
          FindFirst
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Form className="mx-3">{authButton()}</Form>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
function setAuthorized(arg0: AuthStatus) {
  throw new Error("Function not implemented.");
}
