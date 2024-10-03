import { useEffect, useState } from "react";
import { Modal, Button } from "react-bootstrap";
import "./modal.scss";
import FilePicker from "./FilePicker";
import Bookmark from "@type/Bookmarks/Bookmark";
import BookmarkAction from "@type/Bookmarks/BookmarkAction";
import { useBookmarkDispatch } from "@/contexts/BookmarkContext";

const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/api";

export default function ImportModal({
  file,
  show,
  'data-testid': dataTestId,
}: {
  file: Blob | undefined;
  show: boolean | undefined;
  'data-testid'?: string;
}) {
  const [modalShow, setModalShow] = useState(show);
  const [uploadFileName, setUploadFileName] = useState(
    "Import your bookmarks from a .html.",
  );
  const [importFile, setFile] = useState<Blob | undefined>(file);
  const [imported, setImported] = useState<Bookmark[]>([]);
  const [done, setDone] = useState(false);
  const bkmkDispatch = useBookmarkDispatch();

  useEffect(() => {
    importFile ? importBookmarks(importFile) : () => {};
  }, [importFile]);

  useEffect(() => {
    if (done) {
      let action: BookmarkAction = {
        type: "add",
        bookmarks: imported,
      };
      bkmkDispatch(action);
    }
  }, [bkmkDispatch, done, imported]);

  async function importBookmarks(htmlFile: Blob) {
    const formdata = new FormData();
    formdata.append("file", htmlFile, "bookmarks-test.html");

    const req = new Request(SERVER_URL + "/bookmark/import", {
      method: "POST",
      body: formdata,
      credentials: "include",
      redirect: "follow",
    });

    const response = await fetch(req);
    const reader = response.body!.getReader();
    const decoder = new TextDecoder();
    const bkmks: Bookmark[] = [];
    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        setDone(true);
        return;
      }
      const chunk = decoder.decode(value);
      if (chunk.length > 1) {
        const obj = JSON.parse(chunk);
        if (obj.id) {
          bkmks.push(obj as Bookmark);
          setImported([...bkmks]);
        }
      }
    }
  }

  const handleClose = () => setModalShow(false);
  const handleShow = () => setModalShow(true);

  return (
    <div data-testid={dataTestId}>
      <button className="btn" data-testid="import-btn" onClick={handleShow}>
        <i className="bi bi-file-earmark-arrow-up-fill" style={{ border: "solid", borderRadius: "10px", padding: "10px", color: "#717a83" }}></i>
      </button>

      <Modal
        show={modalShow}
        onHide={handleClose}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
        scrollable={true}
        className="modal-container"
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            Import Bookmarks
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="input-group mb-3">
            <input
              className="form-control"
              type="text"
              value={uploadFileName}
              aria-label="Disabled input example"
              disabled
              readOnly
            />
            <FilePicker setUpload={setUploadFileName} setFile={setFile} />
          </div>
          <hr />
          {imported.map((bkmk) => {
            return (
              <div data-testid={`imported-bkmk-${bkmk.title}`} key={bkmk.title}>
                <div className="import-item">
                  <div className="import-text">
                    <p>{bkmk.title}</p>
                  </div>
                  <div className="import-check">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="22"
                      height="22"
                      fill="currentColor"
                      className="bi bi-bookmark-check-fill import-check"
                      viewBox="0 0 16 16"
                    >
                      <path
                        fillRule="evenodd"
                        d="M2 15.5V2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.74.439L8 13.069l-5.26 2.87A.5.5 0 0 1 2 15.5m8.854-9.646a.5.5 0 0 0-.708-.708L7.5 7.793 6.354 6.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0z"
                      />
                    </svg>
                  </div>
                </div>
                <hr></hr>
              </div>
            );
          })}
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={handleClose}>Close</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
