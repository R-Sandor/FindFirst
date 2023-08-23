import Form from 'react-bootstrap/Form';

function FormTextExample() {
  return (
    <>
      <Form.Label></Form.Label>
      <Form.Control
        type="text"
        id="searchText"
        aria-describedby="passwordHelpBlock"
      />
    
      {/*
        Wouldn't it be cool that as they search one can see some articles related start filling suggestions here!

      <Form.Text id="passwordHelpBlock" muted>
        Your password must be 8-20 characters long, contain letters and numbers,
        and must not contain spaces, special characters, or emoji.
      </Form.Text> */}
    </>
  );
}

export default FormTextExample;