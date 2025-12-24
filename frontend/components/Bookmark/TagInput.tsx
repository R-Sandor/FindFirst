import { ChangeEventHandler, KeyboardEventHandler } from "react";
import style from "./bookmarkCard.module.scss";

const TagInput = (props: {
  tags: string[];
  deleteTag: (tag: string, index: number) => void;
  onKeyDown: KeyboardEventHandler;
  inputValue: string;
  onChange: ChangeEventHandler;
  testIdPrefix: `bk-${string}-` | `new-bk-`;
}) => (
  <div className={style.container}>
    {props.tags.map((tag, index) => (
      <button
        key={tag}
        onClick={() => props.deleteTag(tag, index)}
        onContextMenu={(e) => {
          e.preventDefault();
          props.deleteTag(tag, index);
        }}
        type="button"
        className={style.pillButton}
        data-testid={`${props.testIdPrefix}tag-${tag}`}
      >
        {tag}
        <i className="xtag bi bi-journal-x"></i>
      </button>
    ))}

    <input
      className={style.input}
      value={props.inputValue}
      placeholder="Enter a tag"
      data-testid={`${props.testIdPrefix}tag-input`}
      onKeyDown={props.onKeyDown}
      onChange={props.onChange}
    />
  </div>
);

export default TagInput;
