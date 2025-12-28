import { Card } from "react-bootstrap";
import { RefObject, useState } from "react";
import Bookmark from "@type/Bookmarks/Bookmark";
import EditableField from "./EditableField";
import { ScrapableBookmarkToggle } from "./ScrapableToggle";
import style from "./bookmarkCard.module.scss";
import { ScreenSizeProvider } from "@/contexts/ScreenSizeContext";

interface CardBodyProp {
  bookmark: Readonly<Bookmark>;
  highlight: string | null;
  inEditMode: Readonly<boolean>;
  edit: Readonly<RefObject<Bookmark>>;
  changeEditMode: Readonly<Function>;
}

export default function CardBody({
  bookmark,
  highlight,
  inEditMode,
  edit,
  changeEditMode,
}: Readonly<CardBodyProp>) {
  const [isScrapable, setIsScrapable] = useState(bookmark.scrapable);

  return (
    <Card.Body className={``}>
      <Card.Title className={`${style.cTitle}`}>
        {inEditMode ? (
          <EditableField
            fieldValue={bookmark.title}
            fieldName="title"
            changeEditMode={changeEditMode}
            onChange={(e: any) => {
              const { value } = e.target;
              edit.current.title = value;
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
            }}
          />
          <div className="mt-4">
            <ScrapableBookmarkToggle
              isScrapable={isScrapable}
              setScrapable={(scrapableUpd: boolean) => {
                setIsScrapable(scrapableUpd);
                edit.current.scrapable = scrapableUpd;
              }}
              id={bookmark.id}
            />
          </div>
        </div>
      ) : !bookmark.screenshotUrl ? (
        <Card.Link target="_blank" href={bookmark.url}>
          {bookmark.url}
        </Card.Link>
      ) : null}
      {highlight ? (
        <div>
          <hr />
          <p dangerouslySetInnerHTML={{ __html: highlight }}></p>
        </div>
      ) : null}
    </Card.Body>
  );
}
