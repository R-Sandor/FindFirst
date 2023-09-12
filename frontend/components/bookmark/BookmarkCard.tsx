import { useEffect, useState } from "react";
import { Card, CloseButton } from "react-bootstrap";
import api from "@/api/Api";
import { useTagsDispatch } from "@/contexts/TagContext";
import Bookmark from "@/types/Bookmarks/Bookmark";
import Tag from "@/types/Bookmarks/Tag";
import TagAction from "@/types/Bookmarks/TagAction";
import DeleteModal from "./DeleteModal";
import "./bookmarkCard.scss";

interface BookmarkProp {
  bookmark: Bookmark;
}

async function addTagToBookmark(bookmark: Bookmark, trimmedInput: string) {
  let action: TagAction = {
    type: "add",
    tagId: -1,
    tagTitle: "",
  };
  await api
    .bookmarkAddTagByTitle(bookmark?.id, trimmedInput)
    .then((response) => {
      // It will always be the last index since it was the last added.
      let index = response.data.tags.length - 1;
      let responseTag = response.data.tags[index];
      action.tagId = responseTag.id;
      action.tagTitle = responseTag.tag_title;
      bookmark.tags.push({ id: action.tagId, tag_title: action.tagTitle });
    });
  return action;
}

export default function BookmarkCard(bookmarkProp: BookmarkProp) {
  const bookmark: Bookmark = bookmarkProp.bookmark;
  const dispatch = useTagsDispatch();
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const onChange = (e: any) => {
    const { value } = e.target;
    setInput(value);
  };

  function onKeyDown(e: any) {
    const { keyCode } = e;
    const trimmedInput = input.trim();
    if (
      // Enter or space
      (keyCode === 32 || keyCode == 13) &&
      trimmedInput.length &&
      !strTags.includes(trimmedInput)
    ) {
      e.preventDefault();
      setStrTags((prevState) => [...prevState, trimmedInput]);
      setInput("");

      strTags.push(trimmedInput)
      console.log(strTags)
      setStrTags([...strTags]);

      // addTagToBookmark(bookmark, trimmedInput).then((action) => {
      //   dispatch(action)
      // }
      // );
    }
    // backspace delete
    if (keyCode === 8 && !input.length && bookmark?.tags.length) {
      e.preventDefault();
      let tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (poppedTag) deleteTag(tagsCopy.length);
      if (!poppedTag) poppedTag = "";
      setInput(poppedTag);
    }
  }

  useEffect(() => {
    if (bookmark) {
      const tagList: string[] = [];
      bookmark.tags.map((tag: Tag) => {
        tagList.push(tag.tag_title);
      });
      setStrTags(tagList);
    }
  }, [bookmark]);

  const deleteTag = (id: number) => {
    if (bookmark) {
      bookmark.tags = bookmark.tags.filter((t, i) => t.id !== id);
    }
    api.bookmarkRemoveTagById(bookmark.id, id);
    let titles =  bookmark.tags.map((t, i ) => t.tag_title)
    setStrTags(titles);
    let action: TagAction = { type: "delete", tagId: id, tagTitle: "" };
    dispatch(action);
  };

  return bookmark ? (
    <div className="px-1">
      <Card>
        <CloseButton onClick={handleShow} />
        {/* <DeleteModal show={show} onHide={handleClose} /> */}
        <Card.Body>
          <Card.Title>{bookmark.title}</Card.Title>
          <Card.Text className="title">{bookmark.url.toString()}</Card.Text>
        </Card.Body>
        <Card.Footer className="card-footer">
          <div className="container">
            {bookmark.tags.map((tag, index) => (
              <button
                key={tag.id}
                onClick={() => deleteTag(tag.id)}
                type="button"
                className="pill-button"
              >
                {tag.tag_title}
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
