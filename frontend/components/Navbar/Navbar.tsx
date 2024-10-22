"use client";
import { Navbar, Button, ButtonGroup, Container } from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import LightDarkToggle from "./LightDarkToggle";
import ImportModal from "@components/Import/ImportModal";
import Export from "./Export";
import Image from "next/image";
import navbarView from "styles/navbar.module.scss"

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
      style={{ borderBottom: "1px solid", height: "60px" }}
      className="bg-body-tertiary"
    >
      <Container className={`${navbarView.navContainer}`}>
        <Navbar.Brand
          onClick={() => router.push("/")}
          className={`mx-3 cursor-pointer ${navbarView.navBrand}`}
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
        <div className={`d-flex flex-grow-1 mx-3 ${navbarView.searchBar}`}>
          <input
            type="text"
            className={`${navbarView.searchBarInput}`}
            placeholder="Search"
          />
          <button className={`btn ms-2 ${navbarView.searchBarBtn}`} type="submit">
            <i className="bi bi-search"></i>
          </button>
        </div>
        <div className={`mx-3 ${navbarView.navBtns}`}>
          {userAuth === AuthStatus.Authorized ? <ImportModal file={undefined} show={false} data-testid="import-modal" /> : null}
          {userAuth === AuthStatus.Authorized ? <Export data-testid="export-component" /> : null}
          <LightDarkToggle />
          {authButton()}
        </div>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
