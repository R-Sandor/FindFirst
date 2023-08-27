import { Card, Button } from "react-bootstrap";
import "./button.scss";
import Tag from "@/types/Bookmarks/Tag";
import { useEffect, useState } from "react";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";

interface BookmarkProp { 
  bookmark: Bookmark
}

export default function BookmarkCard(bookmarkProp: BookmarkProp) {
  const bookmark = bookmarkProp.bookmark;
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const onChange = (e: any) => {
    const { value } = e.target;
    setInput(value);
  };
  function onKeyDown(e: any) {
    const { keyCode } = e;
    const trimmedInput = input.trim();
    if ((keyCode === 32 || keyCode == 13) && trimmedInput.length && !strTags.includes(trimmedInput)) {
      e.preventDefault();
      setStrTags((prevState) => [...prevState, trimmedInput]);
      setInput("");
      api.bookmarkAddTag(bookmark.id, trimmedInput);
    }
    if (keyCode === 8 && !input.length && bookmark.tags.length) {
      e.preventDefault();
      const tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (!poppedTag) poppedTag = "";
  
      setStrTags(tagsCopy);
      setInput(poppedTag);
      
    }
  }

  useEffect(() => {
    const tagList: string[] = []
   bookmark.tags.map((tag: Tag) => { 
    tagList.push(tag.tag_title);
   })
   setStrTags(tagList);
  }, [bookmark]); 

  const deleteTag = (index: number) => {
    setStrTags(prevState => prevState.filter((strTag, i) => i !== index))
  }

  return (
    <div className="mr-2 ml-0">
      <Card>
        <Card.Body>
          <Card.Title>{bookmark.title}</Card.Title>
          <Card.Text>
            {bookmark.url.toString()}
          </Card.Text>
        </Card.Body>
        <Card.Footer>
          <div className="container">
            {strTags.map((tag, index) => (
              <button key={index} onClick={() => deleteTag(index)} type="button" className="pill-button">
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
  );
}
