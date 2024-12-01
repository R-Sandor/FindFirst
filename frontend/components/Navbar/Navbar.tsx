"use client";
import { Navbar, Button, ButtonGroup, Container } from "react-bootstrap";
import { useRouter } from "next/navigation";
import authService, { AuthStatus } from "@services/auth.service";
import useAuth from "@components/UseAuth";
import LightDarkToggle from "./LightDarkToggle";
import ImportModal from "@components/Import/ImportModal";
import Export from "./Export";
import Image from "next/image";
import navbarView from "styles/navbar.module.scss";
import { useEffect, useState } from "react";
import api from "api/Api";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import Bookmark from "@type/Bookmarks/Bookmark";

enum SearchType {
  titleSearch,
  textSearch,
  tagSearch,
}

enum SearchTypeChar {
  n = SearchType.titleSearch, // Name seaerch (i.e. title)
  f = SearchType.textSearch, // Full-text search.
  t = SearchType.tagSearch, // Tag search.
}

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();

  const [searchText, setSearchText] = useState("");
  const [searchType, setSearchType] = useState(SearchType.titleSearch);
  const bkmkDispatch = useBookmarkDispatch();

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

  async function search(searchText: string, searchType: SearchType) {
    let searchData: Bookmark[] = [];
    console.log(searchType);
    if (searchType == SearchType.titleSearch) {
      await api
        .searchBookmarkByTitleKeywords(searchText.replaceAll(" ", ","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchType.tagSearch) {
      await api
        .searchBookmarkByTags(searchText.replaceAll(" ", ","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchType.textSearch) {
      await api.searchBookmarkByText(searchText);
    }
    bkmkDispatch({
      type: "search",
      bookmarks: searchData,
    });
  }
  const handleSearch = (event: any) => {
    let rawSearch: string = event.target.value;
    let search: string = "";

    rawSearch = rawSearch.trim();

    if (rawSearch.length > 1 && rawSearch.startsWith("/")) {
      let sTypeChar: string | undefined = rawSearch.at(1);
      if (sTypeChar) {
        if (sTypeChar in SearchTypeChar) {
          setSearchType(
            Object.keys(SearchTypeChar)
              .filter((v) => isNaN(Number(v)))
              .indexOf(sTypeChar),
          );
        }
      }
      search = rawSearch.substring(2).trim();
    } else if (rawSearch.length) {
      search = rawSearch;
    }
    setSearchText(search);
  };

  useEffect(() => {
    if (searchText) {
      search(searchText, searchType);
    } else {
      setSearchType(SearchType.titleSearch);
      api.getAllBookmarks().then((successResult) => {
        bkmkDispatch({
          type: "search",
          bookmarks: successResult.data as Bookmark[],
        });
      });
    }
  }, [searchText, searchType]);

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
        {userAuth === AuthStatus.Authorized ? (
          <div className={`d-flex flex-grow-1 mx-3 ${navbarView.searchBar}`}>
            <input
              type="text"
              className={`${navbarView.searchBarInput}`}
              placeholder="Search"
              onChange={handleSearch}
            />
            <button
              className={`btn ms-2 ${navbarView.searchBarBtn}`}
              type="submit"
            >
              <i className="bi bi-search"></i>
            </button>
          </div>
        ) : null}
        <div className={`mx-3 ${navbarView.navBtns}`}>
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
          {authButton()}
        </div>
      </Container>
    </Navbar>
  );
};

export default GlobalNavbar;
