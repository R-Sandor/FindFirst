import { Field, Form, Formik } from "formik";
import { useState } from "react";
import { Button, Card } from "react-bootstrap";
import "./bookmarkCard.scss";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import { useTagsDispatch } from "@/contexts/TagContext";
import TagAction from "@/types/Bookmarks/TagAction";
import Tag from "@/types/Bookmarks/Tag";
import * as Yup from "yup";
import { tlds } from "@type/Bookmarks/TLDS";

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

async function makeNewBookmark(createBmk: Bookmark): Promise<Bookmark> {
  let newBkmkRequest: NewBookmarkRequest;
  newBkmkRequest = {
    title: createBmk.title,
    url: createBmk.url,
    tagIds: [],
  };
  let tagTitles: string[] = createBmk.tags.map((t) => {
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
  const [urlInput, setUrlInput] = useState("");
  const [tagInput, setTagInput] = useState("");
  const [strTags, setStrTags] = useState<string[]>([]);
  const bkmkDispatch = useBookmarkDispatch();
  const tagDispatch = useTagsDispatch();

  const onTagInputChange = (e: any) => {
    setTagInput(e.target.value);
  };

  const urlInputChange = (e: any, setFieldValue: any) => {
    let url: string = e.target.value;
    if (url.length > 4 && !url.startsWith("http")) {
      url = "https://" + url;
    }
    setFieldValue("url", url, true);
    setUrlInput(url);
  };

  const handleOnSubmit = async (
    submittedBmk: NewBookmarkForm,
    actions: any,
  ) => {
    // get the the last inputed string and all the tags already entered.
    let tags: Tag[] = strTags.map((t) => {
      return { tag_title: t, id: -1 };
    });
    if (tagInput) {
      tags.push({ tag_title: tagInput, id: -1 });
    }
    submittedBmk.title = submittedBmk.url;
    let newBkmk: Bookmark = {
      id: -1,
      title: submittedBmk.title,
      url: submittedBmk.url,
      tags: tags,
    };

    let retBkmk = await makeNewBookmark(newBkmk);
    // if adding the bookmark was successful.
    if (retBkmk) {
      retBkmk.tags.forEach((t) => {
        let tAct: TagAction = {
          type: "add",
          tagId: t.id,
          tagTitle: t.tag_title,
          bookmark: retBkmk as Bookmark,
        };
        tagDispatch(tAct);
      });

      let action: BookmarkAction = {
        type: "add",
        bookmarkId: retBkmk.id,
        bookmarks: [retBkmk],
      };
      bkmkDispatch(action);
    }
    actions.resetForm({ newcard }, setStrTags([]), setTagInput(""));
  };

  const handleOnReset = async ({ tagTitles, title, url }: NewBookmarkForm) => {
    setStrTags([]);
    setUrlInput("");
    tagTitles = [];
    title = "";
    url = "";
  };

  function onKeyDown(e: any, sv: any, values: NewBookmarkForm) {
    const { keyCode } = e;
    const trimmedInput = tagInput.trim();
    if (
      // add tag via space bar or enter
      (keyCode === 32 || keyCode === 13) &&
      trimmedInput.length &&
      !strTags.includes(trimmedInput)
    ) {
      e.preventDefault();
      setStrTags((prevState) => [...prevState, trimmedInput]);
      values.tagTitles = strTags.concat(trimmedInput);
      setTagInput("");
    }
    // user hits backspace and the user has input field of 0
    // then pop the last tag only if there is one.
    if (keyCode === 8 && !tagInput.length && strTags.length) {
      e.preventDefault();
      const tagsCopy = [...strTags];
      let poppedTag = tagsCopy.pop();

      values.tagTitles = tagsCopy;
      setStrTags(tagsCopy);
      setTagInput(poppedTag ? poppedTag : "");
    }
    sv(values);
  }

  const deleteTag = (index: number, setField: any, values: NewBookmarkForm) => {
    const tags = strTags.filter((t, i) => i !== index);
    values.tagTitles = tags;
    setStrTags(tags);
    setField("tags", tags, true);
  };

  const bookmarkSchema = Yup.object().shape({
    url: Yup.string()
      .url()
      .required("Must be a valid URL")
      .min(3)
      .test("The TLD is valid", "The Domain must be valid.", (value) => {
        const dots = value.split(".");
        const tld = dots[dots.length - 1];
        return tlds.includes(tld.toUpperCase());
      }),
    tagTitles: Yup.array().max(8, "Too many tags. Max 8.").optional(),
  });

  return (
    <div className="px-1 new-bookmark-card">
      <Formik
        initialValues={newcard}
        onSubmit={handleOnSubmit}
        onReset={handleOnReset}
        validateOnChange={true}
        validateOnBlur={true}
        validationSchema={bookmarkSchema}
      >
        {({ isValid, dirty, values, setValues, setFieldValue, errors }) => (
          <Form>
            <Card className="new-bookmark-card">
              <Card.Header>
                Add Bookmark <i className="bi bi-bookmarks-fill"></i>{" "}
              </Card.Header>
              <Card.Body>
                <Card.Text className="title">
                  <Field
                    className="form-control"
                    id="url"
                    name="url"
                    value={urlInput}
                    onChange={(e: any) => urlInputChange(e, setFieldValue)}
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
                      onClick={() => deleteTag(index, setFieldValue, values)}
                      type="button"
                      data-testid={tag}
                      className="pill-button"
                    >
                      {tag}
                      <i className="xtag bi bi-journal-x"></i>
                    </button>
                  ))}
                  <input
                    value={tagInput}
                    placeholder="Enter a tag"
                    onKeyDown={(e) => onKeyDown(e, setValues, values)}
                    onChange={onTagInputChange}
                  />
                </div>
                <Button
                  disabled={!(isValid && dirty)}
                  className="pill-button submit"
                  type="submit"
                >
                  Submit
                </Button>
                <Button className="pill-button reset" type="reset">
                  Reset
                </Button>
                {errors.url ? (
                  <div className="error-text">{errors.url}</div>
                ) : null}
                {errors.tagTitles && values.tagTitles ? (
                  <div className="error-text">{errors.tagTitles}</div>
                ) : null}
              </Card.Footer>
            </Card>
          </Form>
        )}
      </Formik>
    </div>
  );
}
