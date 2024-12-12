import Bookmark from "@type/Bookmarks/Bookmark";
import { ChangeEventHandler, MutableRefObject } from "react";

export default function EditableField({
  fieldValue,
  fieldName,
  changeEditMode,
  onChange,
}: {
  fieldValue: string;
  changeEditMode: Function;
  fieldName: string;
  onChange: ChangeEventHandler<HTMLInputElement>;
}) {
  return (
    <input
      className="title-edit"
      defaultValue={fieldValue}
      data-testid={`${fieldName}-edit-input`}
      onChange={onChange}
      onKeyDown={(e) => {
        const { key } = e;
        if (key === "Enter" || key === "NumpadEnter") {
          changeEditMode();
        }
      }}
    />
  );
}
