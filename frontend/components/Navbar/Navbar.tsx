"use client";
import { Navbar, Button, ButtonGroup, Container } from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import LightDarkToggle from "./LightDarkToggle";
import ImportModal from "@components/Import/ImportModal";
import Export from "./Export";
import Image from "next/image";
import Searchbar from "./Searchbar";
import navbarView from "styles/navbar.module.scss";

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();
  const user = authService.getUser();

  const router = useRouter();
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
        <Button
          className="d-inline-block"
          variant="secondary"
          onClick={handleLogoutClick}
        >
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
      expand="md"
      style={{ borderBottom: "1px solid", height: "auto", minHeight: "60px" }}
      className="bg-body-tertiary"
    >
      <Container className={navbarView.navContainer}>
        <Navbar.Brand
          onClick={() => router.push("/")}
          className={`cursor-pointer ${navbarView.navBrand}`}
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

        {/* Search bar stays visible always */}
        {userAuth === AuthStatus.Authorized ? <Searchbar /> : null}

        {/* Toggle button for collapsed menu */}
        <Navbar.Toggle aria-controls="navbar-collapse" className="ms-auto" />

        {/* Collapsible content */}
        <Navbar.Collapse id="navbar-collapse">
          <div className={`btn-group ms-auto ${navbarView.navBtns}`}>
            {userAuth === AuthStatus.Authorized ? (
              <ImportModal
                file={undefined}
                show={false}
                data-testid="import-modal"
              />
            ) : null}
            {userAuth === AuthStatus.Authorized ? (
              <Export data-testid="export-component" />
            ) : null}
            <LightDarkToggle />
            {userAuth === AuthStatus.Authorized && (
              <Image
                src={
                  user?.profileImage && user?.profileImage.trim() !== ""
                    ? `/api/user/avatar?userId=${user.id}`
                    : "/img_avatar.png"
                }
                alt="Profile"
                width={39}
                height={39}
              />
            )}
            {authButton()}
          </div>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
