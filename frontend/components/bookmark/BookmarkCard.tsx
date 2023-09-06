import { Card } from "react-bootstrap";
import "./bookmarkCard.scss";
import Tag from "@/types/Bookmarks/Tag";
import { useContext, useEffect, useState } from "react";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import { TagsCntContext, TagsCntDispatchContext } from "@/contexts/TagContext";
import TagAction from "@/types/Bookmarks/TagAction";

interface BookmarkProp {
  bookmark: Bookmark;
}

export default function BookmarkCard(bookmarkProp: BookmarkProp) {
  const dispatch = useContext(TagsCntDispatchContext);

  let bookmark: Bookmark | null = bookmarkProp ? bookmarkProp.bookmark : null;
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
        console.log(tResp)
        console.log(response.data.tags[index]);
        let action: TagAction = {
          type: "add",
          tagId: tResp.id,
          tagTitle: tResp.tag_title,
        };
        console.log("action", action)
        console.log(tResp.id);
        bookmark?.tags.push({ id: tResp.id, tag_title: tResp.tag_title });
        dispatch(action);
      });
    }
    if (keyCode === 8 && !input.length && bookmark?.tags.length) {
      e.preventDefault();
      const tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (!poppedTag) poppedTag = "";

      setStrTags(tagsCopy);
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
    console.log(tagTitle);
    console.log(bookmark?.tags[index]);
    let t = bookmark?.tags[index];
    let id = t ? t.id : -1111;
    if (bookmark) {
      bookmark.tags = bookmark?.tags.filter((t, i) => i !== index);
    }
    // TODO: SWITCH TO ID
    api.bookmarkRemoveTagByTitle(bookmark?.id, tagTitle);

    setStrTags((prevState) => prevState.filter((strTag, i) => i !== index));
    let action: TagAction = { type: "delete", tagId: id, tagTitle: tagTitle };
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
