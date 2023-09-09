import { Card } from "react-bootstrap";
import "./bookmarkCard.scss";
import Tag from "@/types/Bookmarks/Tag";
import { useContext, useEffect, useState } from "react";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import { TagsCntContext, TagsCntDispatchContext, useTagsDispatch } from "@/contexts/TagContext";
import TagAction from "@/types/Bookmarks/TagAction";

interface BookmarkProp {
  bookmark: Bookmark;
}

export default function BookmarkCard(bookmarkProp: BookmarkProp) {
  const dispatch = useTagsDispatch();
  let bookmark: Bookmark = bookmarkProp.bookmark;
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);

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
      api.bookmarkAddTagByTitle(bookmark?.id, trimmedInput).then((response) => {
        // It will always be the last index since it was the last added.
        let index = response.data.tags.length - 1;
        let tResp = response.data.tags[index];
        let action: TagAction = {
          type: "add",
          tagId: tResp.id,
          tagTitle: tResp.tag_title,
        };
        bookmark.tags.push({ id: tResp.id, tag_title: tResp.tag_title });
        let cp = [...strTags, trimmedInput];
        setStrTags(cp);
        dispatch(action);
      });
    }
    // backspace delete
    if (keyCode === 8 && !input.length && bookmark?.tags.length) {
      e.preventDefault();
      let tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (poppedTag) deleteTag(tagsCopy.length);
      console.log(poppedTag)
      if (!poppedTag) poppedTag = "";
      console.log(tagsCopy.length)
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

  const deleteTag = (index: number) => {
    let tagTitle = strTags[index];
    console.log("deleteTag")
    console.log(tagTitle);
    console.log(bookmark.tags[index]);
    let t = bookmark.tags[index];
    if (bookmark) {
      bookmark.tags = bookmark.tags.filter((t, i) => i !== index);
    }
    api.bookmarkRemoveTagById(bookmark.id, t.id);

    setStrTags((prevState) => prevState.filter((strTag, i) => i !== index));
    let action: TagAction = { type: "delete", tagId: t.id, tagTitle: tagTitle };
    dispatch(action);
  };

  return bookmark ? (
    <div className="px-1">
      <Card>
        <Card.Body>
          <Card.Title>{bookmark.title}</Card.Title>
          <Card.Text className="title">{bookmark.url.toString()}</Card.Text>
        </Card.Body>
        <Card.Footer className="card-footer">
          <div className="container">
            {bookmark.tags.map((tag, index) => (
              <button
                key={tag.id}
                onClick={() => deleteTag(index)}
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
