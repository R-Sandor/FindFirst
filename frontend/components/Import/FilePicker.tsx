// adapted from https://github.com/Jaaneek/useFilePicker
import api from "@/api/Api";
import { Dispatch, SetStateAction, useContext } from "react";
import { useFilePicker } from "use-file-picker";
import {
  FileAmountLimitValidator,
  FileSizeValidator,
} from "use-file-picker/validators";

export default function FilePicker({
  setUpload,
  setFile
}: {
  setUpload: Dispatch<SetStateAction<string>>;
  setFile: Dispatch<SetStateAction<File | undefined>>;
}) {
  const { openFilePicker, filesContent, loading, errors } = useFilePicker({
    readAs: "DataURL",
    accept: [".html"],
    multiple: false,
    validators: [
      new FileAmountLimitValidator({ max: 1 }),
      new FileSizeValidator({ maxFileSize: 2 * 1024 * 1024 /* 2 MB */ }),
    ],
    onFilesSuccessfullySelected: ({ plainFiles, filesContent }) => {
      // this callback is called when there were no validation errors
      const userFile = plainFiles[0];
      setUpload(userFile.name);
      // API call to add the html file of bookmarks
      // api.import()...
      setFile(userFile)
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
  return (
    <button className="btn btn-outline-info" onClick={() => openFilePicker()}>
      <i className="bi bi-arrow-bar-up"> Upload File</i>
    </button>
  );
}
