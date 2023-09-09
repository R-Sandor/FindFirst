import { Field, Form, Formik, FormikHelpers } from "formik";
import { useState } from "react";
import Image from "next/image";
import { Button, Card } from "react-bootstrap";
import "./bookmarkCard.scss";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import Tag from "@/types/Bookmarks/Tag";

export interface NewBookmark {
  id?: string;
  title: string;
  url: string;
  tags: string[];
}

export interface NewBookmarkRequest {
  title: string;
  url: string;
  tagIds: number[];
}

// function testSubmit(requestParams.id, ticket)
const newcard: NewBookmark = {
  title: "",
  url: "",
  tags: [],
};

export default function NewBookmarkCard() {
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const bkmkDispatch = useBookmarkDispatch();
  const onChange = (e: any) => {
    const { value } = e.target;
    setInput(value);
  };

  const handleOnSubmit = async (newbookmark: NewBookmark, actions: any) => {
    newbookmark.tags = strTags;
    let tags: Tag[] = strTags.map((t, i) => {
      return { tag_title: t, id: -1 };
    });
    newbookmark.title = newbookmark.url;
    console.log("handleOnSubmit");
    let action: BookmarkAction = {
      type: "add",
      bookmarkId: -1,
      bookmark: {
        id: -1,
        title: newbookmark.title,
        url: newbookmark.url,
        tags: tags,
      },
    };
    bkmkDispatch(action);
    actions.resetForm({ newcard }, setStrTags([]));
    console.log(newbookmark);
  };

  const handleOnReset = async (
    newbookmark: NewBookmark,
    formikHelpers: FormikHelpers<NewBookmark>
  ) => {
    setStrTags([]);
    newbookmark.tags = [];
    newbookmark.title = "";
    newbookmark.url = "";
  };

  function onKeyDown(e: any) {
    const { keyCode } = e;
    const trimmedInput = input.trim();
    if (
      // add tag via space bar or enter
      (keyCode === 32 || keyCode == 13) &&
      trimmedInput.length &&
      !strTags.includes(trimmedInput)
    ) {
      e.preventDefault();
      setStrTags((prevState) => [...prevState, trimmedInput]);
      setInput("");
    }
    if (keyCode === 8 && !input.length && strTags.length) {
      e.preventDefault();
      const tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();
      if (!poppedTag) poppedTag = "";

      setStrTags(tagsCopy);
      setInput(poppedTag);
    }
  }

  const deleteTag = (index: number) => {
    let tagTitle = strTags[index];
    setStrTags(strTags.filter((t, i) => i !== index));
  };

  return (
    <div className="px-1">
      <Formik
        initialValues={newcard}
        onSubmit={handleOnSubmit}
        onReset={handleOnReset}
      >
        <Form>
          <Card>
            <Card.Body>
              <Image
                className="journal-icon"
                src="/journal-bookmark-fill.svg"
                height={50}
                width={50}
                alt="Add New Bookmark"
              />
              <Card.Title>Add New Bookmark</Card.Title>
              <Card.Text className="title">
                <Field
                  className="form-control"
                  id="url"
                  name="url"
                  placeholder="url: https://findfirst.com/discover"
                  type="text"
                />
              </Card.Text>
            </Card.Body>
            <Card.Footer className="card-footer">
              <div className="container">
                {strTags.map((tag, index) => (
                  <button
                    key={index}
                    onClick={() => deleteTag(index)}
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
              <Button className="pill-button submit" type="submit">
                Submit
              </Button>
              <Button className="pill-button reset" type="reset">
                Reset
              </Button>
            </Card.Footer>
          </Card>
        </Form>
      </Formik>
    </div>
  );
}
