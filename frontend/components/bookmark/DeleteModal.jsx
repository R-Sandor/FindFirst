import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

function DeleteModal() {
  return (
    <div
      className="modal show"
      style={{ display: 'block', position: 'initial' }}
    >
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Delete</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>Are you sure you would like to delete this bookmark?</p>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary">No</Button>
          <Button variant="primary">Yes</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default DeleteModal;