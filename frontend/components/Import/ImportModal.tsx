import { useState } from "react";
import { Modal, Button } from "react-bootstrap";
import "./modal.scss";
import FilePicker from "./FilePicker";

export default function ImportModal(props: any) {
  const [modalShow, setModalShow] = useState(false);
  const [uploadFileName, setUploadFileName] = useState(
    "Import your bookmarks from a .html.",
  );

  const handleClose = () => setModalShow(false);
  const handleShow = () => setModalShow(true);

  return (
    <div className="float-left mr-4">
      <button className="btn" data-testid="import-btn" onClick={handleShow}>
        <i className="bi bi-file-earmark-arrow-up-fill"></i>
      </button>

      <Modal
        {...props}
        show={modalShow}
        onHide={handleClose}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
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
            <FilePicker setUpload={setUploadFileName} />
          </div>
          <hr />
          <p>
            Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
            dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta
            ac consectetur ac, vestibulum at eros.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={handleClose}>Close</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
