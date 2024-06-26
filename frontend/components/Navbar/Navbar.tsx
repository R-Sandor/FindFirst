"use client";
import { Navbar, Button, ButtonGroup, Container } from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import LightDarkToggle from "./LightDarkToggle";
import ImportModal from "@components/Import/ImportModal";

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
      style={{ borderBottom: "1px solid" }}
      className="bg-body-tertiary"
    >
      <Container>
        <Navbar.Brand
          onClick={() => router.push("/")}
          className="mx-3 cursor-pointer"
        >
          FindFirst
        </Navbar.Brand>
        <div className="mx-3">
          <LightDarkToggle />
          <ImportModal />
          {authButton()}
        </div>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
