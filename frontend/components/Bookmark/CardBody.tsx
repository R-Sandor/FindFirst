import { Card } from "react-bootstrap";
import { MutableRefObject, RefObject, SetStateAction, useState } from "react";
import Bookmark from "@type/Bookmarks/Bookmark";
import EditableField from "./EditableField";
import { ScrapableBookmarkToggle } from "./ScrapableToggle";

export default function CardBody({
  bookmark,
  inEditMode,
  edit,
  changeEditMode,
}: {
  bookmark: Bookmark;
  inEditMode: boolean;
  edit: RefObject<Bookmark>;
  changeEditMode: Function;
}) {
  const [isScrapable, setScrapable] = useState(bookmark.scrapable);
  return (
    <Card.Body>
      <Card.Title>
        {inEditMode ? (
          <EditableField
            fieldValue={bookmark.title}
            fieldName="title"
            changeEditMode={changeEditMode}
            onChange={(e: any) => {
              const { value } = e.target;
              edit.current.title = value;
              bookmark.title = value;
            }}
          />
        ) : (
          bookmark.title
        )}
      </Card.Title>
      {inEditMode ? (
        <div>
          <EditableField
            fieldValue={bookmark.url}
            fieldName="url"
            changeEditMode={changeEditMode}
            onChange={(e: any) => {
              const { value } = e.target;
              edit.current.url = value;
              bookmark.url = value;
            }}
          />
          <div className="mt-4">
            <ScrapableBookmarkToggle
              isScrapable={isScrapable}
              setScrapable={(scrapableUpd: boolean) => {
                setScrapable(scrapableUpd);
                edit.current.scrapable = scrapableUpd;
              }}
              id={bookmark.id}
            />
          </div>
        </div>
      ) : (
        <Card.Link target="_blank" href={bookmark.url}>
          {bookmark.url}
        </Card.Link>
      )}
    </Card.Body>
  );
}
