import { Field, Form, Formik, FormikHelpers } from "formik";
import { useState } from "react";
import Image from "next/image";
import { Button, Card } from "react-bootstrap";
import "./bookmarkCard.scss";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import { useTagsDispatch } from "@/contexts/TagContext";
import TagAction from "@/types/Bookmarks/TagAction";
import { Tag } from "@/types/Bookmarks/Tag";

/**
 * Bookmark representation from the NewBookmarkCard card form.. 
 */
export interface NewBookmarkForm {
  id?: string;
  title: string;
  url: string;
  tagTitles: string[];
}

/**
 * The actual request made to server.
 */
export interface NewBookmarkRequest {
  title: string;
  url: string;
  tagIds: number[];
}

/**
 * NewCard form.
 */
const newcard: NewBookmarkForm = {
  title: "",
  url: "",
  tagTitles: [],
};

// TODO error handling, tag list limits
async function makeNewBookmark(createBmk: Bookmark): Promise<Bookmark> {
  let newBkmkRequest: NewBookmarkRequest;
  newBkmkRequest = {
    title: createBmk.title,
    url: createBmk.url,
    tagIds: [],
  };
  let tagTitles: string[] = createBmk.tags.map((t, i) => {
    return t.tag_title;
  });
  await api.addAllTag(tagTitles).then((response) => {
    let respTags: Tag[] = response.data;
    respTags.forEach((rt) => {
      newBkmkRequest.tagIds.push(rt.id);
    });
  });
  await api.addBookmark(newBkmkRequest).then((response) => {
    createBmk.id = response.data.id;
    createBmk.tags = response.data.tags;
  });
  return createBmk;
}

export default function NewBookmarkCard() {
  const [input, setInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const bkmkDispatch = useBookmarkDispatch();
  const tagDispatch = useTagsDispatch();
  const onChange = (e: any) => {
    const { value } = e.target;
    setInput(value);
  };

  const handleOnSubmit = async (submittedBmk: NewBookmarkForm, actions: any) => {
    submittedBmk.tagTitles = strTags;
    let tags: Tag[] = strTags.map((t, i) => {
      return { tag_title: t, id: -1 };
    });
    submittedBmk.title = submittedBmk.url;
    let newBkmk: Bookmark = {
      id: -1,
      title: submittedBmk.title,
      url: submittedBmk.url,
      tags: tags,
    };
    let retBkmk = await makeNewBookmark(newBkmk);
    let action: BookmarkAction = {
      type: "add",
      bookmarkId: retBkmk.id,
      bookmark: retBkmk,
    };
    retBkmk.tags.forEach((t, i) => {
      let tAct: TagAction = {
        type: "add",
        tagId: t.id,
        tagTitle: t.tag_title,
      };
      tagDispatch(tAct)
    });

    bkmkDispatch(action);
    actions.resetForm({ newcard }, setStrTags([]));
  };

  const handleOnReset = async (
    newbookmark: NewBookmarkForm,
    formikHelpers: FormikHelpers<NewBookmarkForm>
  ) => {
    setStrTags([]);
    newbookmark.tagTitles = [];
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
    <div className="px-1 new-bookmark-card">
      <Formik
        initialValues={newcard}
        onSubmit={handleOnSubmit}
        onReset={handleOnReset}
      >
        <Form>
          <Card className="new-bookmark-card">
              <Card.Header>Add Bookmark <i className="bi bi-bookmarks-fill"></i> </Card.Header>
              <Card.Body>
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
