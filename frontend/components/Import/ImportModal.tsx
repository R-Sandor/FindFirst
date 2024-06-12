import { useState } from "react";
import { Modal, Button } from "react-bootstrap";
import "./modal.scss";

export default function ImportModal(props: any) {
  const [modalShow, setModalShow] = useState(false);
  return (
    <div className="float-left mr-4">
      <button className="btn" onClick={() => setModalShow(true)}>
        <i className="bi bi-file-earmark-arrow-up-fill"></i>
      </button>

      <Modal
        {...props}
        show={modalShow}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
      >
        <Modal.Header closeButton onClick={() => setModalShow(false)}>
          <Modal.Title id="contained-modal-title-vcenter">
            Import Bookmarks
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <h4>Centered Modal</h4>
          <p>
            Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
            dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta
            ac consectetur ac, vestibulum at eros.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={() => setModalShow(false)}>Close</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
