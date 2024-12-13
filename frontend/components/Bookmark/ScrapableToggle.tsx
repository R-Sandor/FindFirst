import { NewBookmarkForm } from "@type/Bookmarks/NewBookmark";
import { Dispatch, SetStateAction, useEffect } from "react";

export function ScrapableNewBookmarkToggle({
  isScrapable,
  setScrapable,
  values,
  setValues,
}: {
  isScrapable: boolean;
  setScrapable: Dispatch<SetStateAction<boolean>>;
  values: NewBookmarkForm;
  setValues: Dispatch<SetStateAction<NewBookmarkForm>>;
}) {
  return (
    <div className="form-check form-switch isScrapable">
      <input
        className="form-check-input"
        type="checkbox"
        id="isScrapable"
        checked={isScrapable}
        onClick={() => {
          setScrapable(!isScrapable);
          values.scrapable = !isScrapable;
          setValues({ ...values });
        }}
      />
      <label
        className="form-check-label"
        htmlFor="flexSwitchCheckChecked"
        id="isScrapableLabel"
      >
        Scrapable
      </label>
    </div>
  );
}

export function ScrapableBookmarkToggle({
  isScrapable,
  setScrapable,
}: {
  isScrapable: boolean;
  setScrapable: Function;
}) {
  useEffect(() => {
    console.log(isScrapable);
  }, [isScrapable]);

  return (
    <div className="form-check form-switch isScrapable">
      <input
        className="form-check-input"
        type="checkbox"
        id="isScrapable"
        checked={isScrapable}
        onChange={() => {
          setScrapable(!isScrapable);
        }}
      />
      <label
        className="form-check-label"
        htmlFor="flexSwitchCheckChecked"
        id="isScrapableLabel"
      >
        Scrapable
      </label>
    </div>
  );
}
