import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import api from "@api/Api";
import Bookmark from "@type/Bookmarks/Bookmark";
import SearchType from "@type/classes/SearchType";
import { useEffect, useState } from "react";
import navbarView from "styles/navbar.module.scss";

enum SearchTypeEnum {
  titleSearch,
  textSearch,
  tagSearch,
}

enum SearchTypeChar {
  b = SearchTypeEnum.titleSearch, // Title search (i.e. Bookmark Title)
  f = SearchTypeEnum.textSearch, // Full-text search.
  t = SearchTypeEnum.tagSearch, // Tag search.
}

const searchTypes = Object.values(SearchTypeEnum)
  .filter((v) => isNaN(Number(v)))
  .map((st, i) => {
    let type = String(st)
      .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
      .split(" ")[0];
    let typeCased = type.charAt(0).toUpperCase() + String(type).slice(1);
    return new SearchType(i, SearchTypeChar[i], typeCased + " Search");
  });

export default function Searchbar() {
  const bkmkDispatch = useBookmarkDispatch();
  const [searchType, setSearchType] = useState(searchTypes[0]);
  const [searchText, setSearchText] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const [shouldSplit, setShouldSplit] = useState(false);
  const [modified, setModified] = useState(false);

  useEffect(() => {
    // someone switched from tags to another type of search.
    if (searchType.type != SearchTypeEnum.tagSearch && strTags.length) {
      setSearchText(strTags.join(" "));
      setStrTags([]);
      search(strTags.join(" "), searchType.type);
    }
    // switched a text search to a tag search.
    else if (
      searchType.type == SearchTypeEnum.tagSearch &&
      shouldSplit &&
      searchText.length
    ) {
      setStrTags([...searchText.trimEnd().split(" ")]);
      // only split once.
      setShouldSplit(false);
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
      search(searchText, searchType.type);
    }
  }, [modified, searchText, searchType, strTags]);

  const deleteTag = (index: number) => {
    const tags = strTags.filter((t, i) => i !== index);
    setStrTags(tags);
  };

  function onKeyDown(e: any) {
    if (searchType.type == SearchTypeEnum.tagSearch) {
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
        const tagsCopy = [...strTags];
        let poppedTag = tagsCopy.pop();
        setStrTags(tagsCopy);
        setSearchText(poppedTag ? poppedTag : "");
      }
    }
  }

  const handleSearch = (event: any) => {
    const rawSearch: string = event.target.value;
    let trimmed = rawSearch.trim();
    let search: string = "";

    if (trimmed.length > 1 && trimmed.startsWith("/")) {
      let sTypeChar: string | undefined = rawSearch.at(1);
      if (sTypeChar) {
        for (let i = 0; i < searchTypes.length; i++) {
          if (searchTypes[i].charCode == sTypeChar) {
            setSearchType(searchTypes[i]);
            break;
          }
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

  async function search(searchText: string, searchType: SearchTypeEnum) {
    let searchData: Bookmark[] = [];
    if (searchType == SearchTypeEnum.titleSearch) {
      await api
        .searchBookmarkByTitleKeywords(searchText.trim().replaceAll(" ", ","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchTypeEnum.tagSearch && strTags.length) {
      await api
        .searchBookmarkByTags(strTags.join(","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchTypeEnum.textSearch) {
      await api.searchBookmarkByText(searchText);
    }
    bkmkDispatch({
      type: "search",
      bookmarks: searchData,
    });
  }

  return (
    <div className={`d-flex flex-grow-1 mx-3 ${navbarView.searchBar}`}>
      <div className={`flex-grow d-flex `}>
        <div className="mt-1">
          <button
            key={"searchType"}
            title={searchType.textDescription}
            onClick={() => {
              const nextType = (searchType.type + 1) % 3;
              if (searchText.length && nextType == SearchTypeEnum.tagSearch) {
                setShouldSplit(true);
              } else {
                setShouldSplit(false);
              }
              setSearchType(searchTypes[nextType]);
            }}
            type="button"
            data-testid={searchType + "searchType"}
            className={`${navbarView.pillButton}`}
          >
            {`/${searchType.charCode}`}
          </button>
          {searchType.type == SearchTypeEnum.tagSearch
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
        </div>
        <input
          type="text"
          className={`flex-grow-1 ${navbarView.searchBarInput} `}
          placeholder="Search"
          onKeyDown={(e) => onKeyDown(e)}
          onChange={handleSearch}
          value={searchText}
        />
        <button className={`btn ${navbarView.searchBarBtn}`} type="submit">
          <i className="bi bi-search"></i>
        </button>
      </div>
    </div>
  );
}
