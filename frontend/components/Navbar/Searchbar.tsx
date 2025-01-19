import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import api from "@api/Api";
import Bookmark from "@type/Bookmarks/Bookmark";
import SearchType from "@type/classes/SearchType";
import { useEffect, useState } from "react";
import navbarView from "styles/navbar.module.scss";

export enum SearchTypeEnum {
  titleSearch,
  textSearch,
  tagSearch,
}

export const SearchTypeChar: { [key in SearchTypeEnum]: string } = {
  [SearchTypeEnum.titleSearch]: "b", // Title search (i.e. Bookmark Title)
  [SearchTypeEnum.textSearch]: "f", // Full-text search
  [SearchTypeEnum.tagSearch]: "t", // Tag search
};

const searchTypes = Object.values(SearchTypeEnum)
  .filter((v) => isNaN(Number(v)))
  .map((st, i) => {
    let type = String(st)
      .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
      .split(" ")[0];
    let typeCased = type.charAt(0).toUpperCase() + String(type).slice(1);
    return new SearchType(
      i,
      SearchTypeChar[i as SearchTypeEnum],
      typeCased + " Search",
    );
  });

export default function Searchbar() {
  const bkmkDispatch = useBookmarkDispatch();
  const [searchType, setSearchType] = useState(searchTypes[0]);
  const [searchText, setSearchText] = useState("");
  const [lastSearched, setLastSearched] = useState("");
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
      const { key } = e;
      const trimmedInput = searchText.trim();
      if (
        // add tag via space bar or enter
        (key === "Enter" || key === "Space" || key === " ") &&
        trimmedInput.length &&
        !strTags.includes(trimmedInput)
      ) {
        e.preventDefault();
        setStrTags((prevState) => [...prevState, trimmedInput]);
        setSearchText("");
      }
      // user hits backspace and the user has input field of 0
      // then pop the last tag only if there is one.
      if (key === "Backspace" && !searchText.length && strTags.length) {
        e.preventDefault();
        const tagsCopy = [...strTags];
        let poppedTag = tagsCopy.pop();
        setStrTags(tagsCopy);
        if (poppedTag) {
          setSearchText(poppedTag);
        }
      }
    }
  }

  interface SearchHelper {
    text: string;
    wordsList: string[];
  }

  function cleanText(text: string, wordsList: string[]): SearchHelper {
    const trimmed = text.trim();
    const nextWordStart = trimmed.lastIndexOf(" ");
    if (nextWordStart > 0) {
      wordsList.push(trimmed.slice(nextWordStart + 1));
      return cleanText(trimmed.slice(0, nextWordStart), wordsList);
    } else if (trimmed.length > 0) {
      wordsList.push(text);
      return cleanText("", wordsList);
    }
    return { text: "", wordsList: wordsList };
  }

  function isSearchIsTypeSearchTypeChange(unmodifiedSearch: string): boolean {
    let input: string | undefined = unmodifiedSearch.at(1);
    let found = false;
    for (let searchType of searchTypes) {
      if (searchType.charCode == input) {
        setSearchType(searchType);
        found = true;
        break;
      }
    }
    return found;
  }

  const handleSearch = (event: any) => {
    const rawSearch: string = event.target.value;
    let trimmed = rawSearch.trim();
    let search: string = "";

    if (trimmed.length > 1 && trimmed.startsWith("/")) {
      if (isSearchIsTypeSearchTypeChange(rawSearch)) {
        search = rawSearch.substring(2).trim();
      } else {
        search = rawSearch;
      }
      setModified(true);
    } else if (rawSearch.length) {
      search = rawSearch;
      setModified(true);
    }
    setSearchText(search);
  };

  async function search(searchText: string, searchType: SearchTypeEnum) {
    let searchData: Bookmark[] = [];

    if (searchType == SearchTypeEnum.titleSearch && searchText.trim().length) {
      const words = cleanText(searchText, []).wordsList.join(",");
      if (words.length && words.trim() !== lastSearched.trim()) {
        setLastSearched(words);
        await api.searchBookmarkByTitleKeywords(words).then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
      }
    } else if (searchType == SearchTypeEnum.tagSearch && strTags.length) {
      await api
        .searchBookmarkByTags(strTags.join(","))
        .then((successResult) => {
          searchData = successResult.data as Bookmark[];
        });
    } else if (searchType == SearchTypeEnum.textSearch) {
      await api.searchBookmarkByText(searchText).then((success) => {
        searchData = success.data as Bookmark[];
      });
    }
    if (searchData.length > 0) {
      bkmkDispatch({
        type: "search",
        bookmarks: searchData,
      });
    }
  }

  return (
    <div className={`d-flex flex-grow-1 ${navbarView.searchBar}`}>
      <div className={`flex-grow d-flex`}>
        <div className={navbarView.searchButtonsContainer}>
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
                  key={tag}
                  onClick={() => deleteTag(index)}
                  type="button"
                  data-testid={`tag-${tag}`}
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
