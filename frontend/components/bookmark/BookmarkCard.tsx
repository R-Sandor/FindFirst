import { useEffect, useState } from "react";
import { Card, CloseButton } from "react-bootstrap";
import { useTagsDispatch } from "@/contexts/TagContext";
import Bookmark from "@/types/Bookmarks/Bookmark";
import TagAction from "@/types/Bookmarks/TagAction";
import DeleteModal from "./DeleteModal";
import "./bookmarkCard.scss";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import { Tag } from "@/types/Bookmarks/Tag";

interface BookmarkProp {
  bookmark: Bookmark;
}

/**
 *  function to add a Tag to a Bookmark
 * @param bookmark bookmark
 * @param trimmedInput string title of tag
 * @returns Promise<TagAction> populates bookmark with the tags.
 */
async function addTagToBookmark(
  bookmark: Bookmark,
  trimmedInput: string
): Promise<TagAction> {
  console.log("trimmedInput", trimmedInput);
  let action: TagAction = {
    type: "add",
    tagId: -1,
    tagTitle: "",
  };
  await api
    .bookmarkAddTagByTitle(bookmark?.id, trimmedInput)
    .then((response) => {
      // It will always be the last index since it was the last added.
      // let index = response.data.length - 1;
      console.log("response", response);
      action.tagId = response.data.id;
      action.tagTitle = response.data.tag_title;
      bookmark.tags.push({ id: action.tagId, tag_title: action.tagTitle });
    });
  return action;
}

export default function BookmarkCard(bookmarkProp: BookmarkProp) {
  const bookmark: Bookmark = bookmarkProp.bookmark;
  const dispatch = useTagsDispatch();
  const bkmkDispatch = useBookmarkDispatch();
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const [show, setShow] = useState(false);
  const handleClose = () => {
    setShow(false);
    console.log("handle close");
  };
  const handleShow = () => setShow(true);

  useEffect(() => {
    if (bookmark) {
      const tagList: string[] = [];
      bookmark.tags.map((tag: Tag) => {
        tagList.push(tag.tag_title);
      });
      setStrTags(tagList);
    }
  }, [bookmark]);

  /**
   * Decrement all the tags associated to this bookmark
   * then remove the bookmark itself.
   * Remove this from the inverse list of tags -> bookmarks when that map is created
   *
   * Consider creating a typescript class to act a handler for
   * the state of bookmarks.
   */
  function deleteBkmk() {
    console.log("delete this bookmark");
    let action: BookmarkAction = {
      type: "delete",
      bookmarkId: bookmark.id,
    };
    // delete the bookmark.
    bkmkDispatch(action);

    // decrement the bookmark counters
    bookmark.tags.forEach((tag) => {
      const idx = getIdxFromTitle(tag.tag_title);
      const tagId = bookmark.tags[idx].id;
      // update the sidebar.
      let action: TagAction = { type: "delete", tagId: tagId, tagTitle: "" };
      dispatch(action);
    });
  }

  const deleteTag = (tag_title: string) => {
    const idx = getIdxFromTitle(tag_title);
    const tagId = bookmark.tags[idx].id;
    if (bookmark) {
      bookmark.tags = bookmark.tags.filter((t, i) => i !== idx);
    }
    // api.bookmarkRemoveTagById(bookmark.id, tagId);
    let titles = bookmark.tags.map((t) => t.tag_title); // just the titles display
    setStrTags(titles);

    // update the sidebar.
    let action: TagAction = { type: "delete", tagId: tagId, tagTitle: "" };
    dispatch(action);
  };

  function getIdxFromTitle(tag_title: string): number {
    return bookmark.tags.findIndex((t) => t.tag_title == tag_title);
  }

  const onChange = (e: any) => {
    const { value } = e.target;
    setInput(value);
  };

  function onKeyDown(e: any) {
    const { keyCode } = e;
    const trimmedInput = input.trim();
    console.log(trimmedInput);
    if (
      // Enter or space
      (keyCode === 32 || keyCode == 13) &&
      trimmedInput.length &&
      !strTags.includes(trimmedInput)
    ) {
      e.preventDefault();
      setStrTags((prevState) => [...prevState, trimmedInput]);

      strTags.push(trimmedInput);
      console.log(strTags);
      setStrTags([...strTags]);
      console.log("input :", input);
      let ltag: Tag;
      addTagToBookmark(bookmark, trimmedInput).then((action) => {
        dispatch(action);
      });

      setInput("");
    }
    // backspace delete
    if (keyCode === 8 && !input.length && bookmark?.tags.length) {
      e.preventDefault();
      let tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (poppedTag) deleteTag(poppedTag);
      if (!poppedTag) poppedTag = "";
      setInput(poppedTag);
    }
  }

  return bookmark ? (
    <div className="px-2 flex">
      <Card className="">
        <div className="card-header">
          <CloseButton
            className="inline float-right text-sm"
            onClick={handleShow}
          />
        </div>
        <DeleteModal
          show={show}
          handleClose={handleClose}
          deleteBkmk={deleteBkmk}
        />
        <Card.Body>
          <Card.Title>{bookmark.title}</Card.Title>
          <Card.Text className="title">{bookmark.url.toString()}</Card.Text>
        </Card.Body>
        <Card.Footer className="card-footer">
          <div className="container">
            {strTags.map((tag, id) => (
              <button
                key={id}
                onClick={() => deleteTag(tag)}
                type="button"
                className="pill-button"
              >
                {tag}
                <i className="xtag bi bi-journal-x"></i>
              </button>
            ))}

            <input
              value={input}
              placeholder="Enter a tag"
              onKeyDown={onKeyDown}
              onChange={onChange}
            />
          </div>
        </Card.Footer>
      </Card>
    </div>
  ) : (
    <p>...loading </p>
  );
}
