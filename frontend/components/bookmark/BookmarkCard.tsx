import { Card, Button } from "react-bootstrap";

export default function Bookmarkcard() {
  return (
    <div className="mr-2 ml-0">
      <Card>
        <Card.Body>
          <Card.Title>Card title</Card.Title>
          <Card.Text>
            This is a wider card with supporting text below as a natural lead-in
            to additional content. This content is a little bit longer.
          </Card.Text>
        </Card.Body>
        <Card.Footer>
          <span className="badge rounded-pill bg-primary">Light </span>
          <small className="text-muted">Last updated 3 mins ago</small>
          <span className="badge rounded-pill bg-primary">
  <span>Example Tag</span>
  <a><i className="bi bi-x"></i></a> 
</span>
        </Card.Footer>
      </Card>
    </div>
  );
}
