import { ChangeEventHandler } from "react";

export default function EditableField({
  fieldValue,
  fieldName,
  changeEditMode,
  onChange,
}: {
  fieldValue: Readonly<string>;
  changeEditMode: Readonly<Function>;
  fieldName: Readonly<string>;
  readonly onChange: ChangeEventHandler<HTMLInputElement>;
}) {
  return (
    <input
      className="title-edit"
      defaultValue={fieldValue}
      data-testid={`${fieldName}-${fieldValue}-edit-input`}
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
