import { ChangeEventHandler } from "react";

interface EditableFieldProps {
  fieldValue: Readonly<string>;
  changeEditMode: Readonly<Function>;
  fieldName: Readonly<string>;
  readonly onChange: ChangeEventHandler<HTMLInputElement>;
}

export default function EditableField({
  fieldValue,
  fieldName,
  changeEditMode,
  onChange,
}: Readonly<EditableFieldProps>) {
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
