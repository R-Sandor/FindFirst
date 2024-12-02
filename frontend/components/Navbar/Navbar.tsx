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
  n = SearchType.titleSearch, // Name search (i.e. title)
  f = SearchType.textSearch, // Full-text search.
  t = SearchType.tagSearch, // Tag search.
}

const GlobalNavbar: React.FC = () => {
  const userAuth = useAuth();

  const [searchText, setSearchText] = useState("");
  const [modified, setModified] = useState(false);
  const [searchType, setSearchType] = useState(SearchType.titleSearch);
  const [prevSearchType, setPrevSearchType] = useState(SearchType.titleSearch);
  const [strTags, setStrTags] = useState<string[]>([]);
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
    if (searchType == SearchType.titleSearch) {
      await api
        .searchBookmarkByTitleKeywords(searchText.trim().replaceAll(" ", ","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchType.tagSearch && strTags.length) {
      await api
        .searchBookmarkByTags(strTags.join(","))
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
    const rawSearch: string = event.target.value;
    let trimmed = rawSearch.trim();
    let search: string = "";

    if (trimmed.length > 1 && trimmed.startsWith("/")) {
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
      setModified(true);
    } else if (rawSearch.length) {
      search = rawSearch;
      setModified(true);
    } else {
      // setModified(false);
    }
    setSearchText(search);
  };

  useEffect(() => {
    console.log(modified);
    // someone switched from tags to another type of search.
    if (searchType != SearchType.tagSearch && strTags.length) {
      setSearchText(strTags.join(" "));
      setStrTags([]);
      search(strTags.join(" "), searchType);
    }
    // switched a text search to a tag search.
    else if (
      searchType == SearchType.tagSearch &&
      searchType != prevSearchType &&
      searchText.length
    ) {
      console.log("converting to tags");
      setPrevSearchType(SearchType.tagSearch);
      if(modified) {
        setStrTags([...searchText.trimEnd().split(" ")]);
      }
      setSearchText("");
    }
    // No search parameters at all, bring back the defualt.
    else if (searchText.length == 0 && strTags.length == 0 && modified) {
      setModified(false);
      api.getAllBookmarks().then((successResult) => {
        bkmkDispatch({
          type: "search",
          bookmarks: successResult.data as Bookmark[],
        });
      });
    }
    // otherwise just search.
    else if (searchText.length || strTags.length) {
      console.log("searching");
      search(searchText, searchType);
    }
  }, [modified, searchText, searchType, strTags]);

  const deleteTag = (index: number) => {
    const tags = strTags.filter((t, i) => i !== index);
    setStrTags(tags);
  };

  function onKeyDown(e: any) {
    if (searchType == SearchType.tagSearch) {
      console.log("key down?");
      const { keyCode } = e;
      const trimmedInput = searchText.trim();
      if (
        // add tag via space bar or enter
        (keyCode === 32 || keyCode === 13) &&
        trimmedInput.length &&
        !strTags.includes(trimmedInput)
      ) {
        setStrTags((prevState) => [...prevState, trimmedInput]);
        setSearchText("");
      }
      // user hits backspace and the user has input field of 0
      // then pop the last tag only if there is one.
      if (keyCode === 8 && !searchText.length && strTags.length) {
        e.preventDefault();
        console.log(strTags);
        const tagsCopy = [...strTags];
        let poppedTag = tagsCopy.pop();
        console.log("popping tag");
        setStrTags(tagsCopy);
        setSearchText(poppedTag ? poppedTag : "");
      }
    }
  }

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
            <button
              key={"searchType"}
              onClick={() => {
                setPrevSearchType(searchType);
                setSearchType((searchType + 1) % 3);
              }}
              type="button"
              data-testid={searchType + "searchType"}
              className={navbarView.pillButton}
            >
              {`/${SearchTypeChar[searchType]}`}
            </button>
            {searchType == SearchType.tagSearch
              ? strTags.map((tag, index) => (
                  <button
                    key={index}
                    onClick={() => deleteTag(index)}
                    type="button"
                    data-testid={tag}
                    className={navbarView.pillButtonTag}
                  >
                    {tag}
                    <i className="xtag bi bi-x"></i>
                  </button>
                ))
              : null}
            <input
              type="text"
              className={`${navbarView.searchBarInput}`}
              placeholder="Search"
              onKeyDown={(e) => onKeyDown(e)}
              onChange={handleSearch}
              value={searchText}
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
