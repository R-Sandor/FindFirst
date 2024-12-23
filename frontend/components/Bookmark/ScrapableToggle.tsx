import { NewBookmarkForm } from "@type/Bookmarks/NewBookmark";
import { Dispatch, SetStateAction } from "react";

export function ScrapableNewBookmarkToggle({
  isScrapable,
  setScrapable,
  values,
  setValues,
}: {
  isScrapable: Readonly<boolean>;
  readonly setScrapable: Dispatch<SetStateAction<boolean>>;
  values: Readonly<NewBookmarkForm>;
  readonly setValues: Dispatch<SetStateAction<NewBookmarkForm>>;
}) {
  return (
    <div className="form-check form-switch isScrapable">
      <input
        className="form-check-input"
        type="checkbox"
        id="isScrapable"
        data-testid={`${values.url}-scrapable-edit`}
        defaultChecked={isScrapable}
        onChange={() => {
          setScrapable(!isScrapable);
          let cpy = { ...values };
          cpy.scrapable = !isScrapable;
          setValues({ ...cpy });
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
  id,
}: {
  isScrapable: Readonly<boolean>;
  setScrapable: Readonly<Function>;
  id: Readonly<number>;
}) {
  console.log(isScrapable);
  return (
    <div className="form-check form-switch isScrapable">
      <input
        className="form-check-input"
        type="checkbox"
        id="isScrapable"
        data-testid={`${id}-scrapable-edit`}
        defaultChecked={isScrapable}
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
