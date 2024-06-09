// adapted from https://github.com/Jaaneek/useFilePicker
import api from "@/api/Api";
import { Dispatch, SetStateAction, useContext } from "react";
import { useFilePicker } from "use-file-picker";
import {
  FileAmountLimitValidator,
  FileSizeValidator,
  ImageDimensionsValidator,
} from "use-file-picker/validators";

export default function FilePicker(pickerProp: {
  setSearch: Dispatch<SetStateAction<string>>;
  classifications: string[];
}) {
  const { openFilePicker, filesContent, loading, errors } = useFilePicker({
    readAs: "DataURL",
    accept: "image/*",
    multiple: true,
    validators: [
      new FileAmountLimitValidator({ max: 1 }),
      new FileSizeValidator({ maxFileSize: 50 * 1024 * 1024 /* 50 MB */ }),
      new ImageDimensionsValidator({
        maxHeight: 3200, // in pixels
        maxWidth: 3200,
        minHeight: 1,
        minWidth: 1,
      }),
    ],
    onFilesSuccessfullySelected: ({ plainFiles, filesContent }) => {
      // this callback is called when there were no validation errors
      const userFile = plainFiles[0];
      pickerProp.setSearch(userFile.name);
      // API call to add the html file of bookmarks
      // api.import()...

      // handle adding each bookmark returned to frontend or rerender
    },
  });

  if (loading) {
    return <div>Loading...</div>;
  }

  if (errors.length) {
    {
      console.log(errors);
    }
    return <div>Error...</div>;
  }
  if (filesContent) {
  }
  return (
    <button
      className="btn bi bi-file-earmark-arrow-up-fill"
      onClick={() => openFilePicker()}
    >
      <i className="bi bi-file-earmark-arrow-up-fill"></i>
    </button>
  );
}
