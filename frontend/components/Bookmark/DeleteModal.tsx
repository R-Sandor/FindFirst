import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

export interface ModalProps {
  show: Readonly<boolean>;
  readonly handleClose: () => void;
  readonly deleteBkmk: () => void;
}

function DeleteModal({ show, handleClose, deleteBkmk }: ModalProps) {
  return (
    <div style={{ display: "block", position: "initial" }}>
      <Modal className="" show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Delete</Modal.Title>
        </Modal.Header>

        <Modal.Body className="">
          <p>Are you sure you would like to delete this bookmark?</p>
        </Modal.Body>

        <Modal.Footer>
          <Button onClick={handleClose} variant="secondary">
            No
          </Button>
          <Button
            // Delete the bookmark and close the modal.
            onClick={() => {
              deleteBkmk();
              handleClose();
            }}
            variant="primary"
          >
            Yes
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default DeleteModal;
