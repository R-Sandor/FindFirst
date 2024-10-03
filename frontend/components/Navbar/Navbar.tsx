"use client";
import { Nav, Navbar, Button, ButtonGroup, Container } from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import LightDarkToggle from "./LightDarkToggle";
import ImportModal from "@components/Import/ImportModal";
import Export from "./Export";
import Image from "next/image";

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();

  const router = useRouter();
  // TODO: Refactor into its own component.
  function authButton() {
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
      expand="lg"
      style={{ borderBottom: "1px solid", height: "60px", zIndex: "1030" }}
      className="bg-body-tertiary"
    >
      <Container>
        <Navbar.Brand
          onClick={() => router.push("/")}
          className="mx-3 cursor-pointer"
        >
          <Image
            src="/basic-f-v2-dark-mode-v2-fav.png"
            width="38"
            height="30"
            className="d-inline-block align-top"
            alt="Find First logo"
          />
          FindFirst
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse>
          <Nav style={{ background: '#ffffff' }} className="float-right">
            {userAuth === AuthStatus.Authorized ? <Nav.Link><ImportModal file={undefined} show={false} data-testid="import-modal" /></Nav.Link> : null}
            {userAuth === AuthStatus.Authorized ? <Nav.Link><Export data-testid="export-component" /></Nav.Link> : null}
            <Nav.Link><LightDarkToggle /></Nav.Link>
            <Nav.Link>{authButton()}</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
