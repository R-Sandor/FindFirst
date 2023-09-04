import { Form, Formik } from "formik";
import { useState } from "react";
import { Button, Card } from "react-bootstrap";
import "./bookmarkCard.scss";


export interface NewBookmark {
  title: string;
  url: string;
  tags: string[];
}

// function testSubmit(requestParams.id, ticket)
const newcard: NewBookmark = {
  title: "",
  url: "",
  tags: [],
};

const handleOnSubmit = async (newbookmark: NewBookmark, actions: any) => {
  console.log(newbookmark);
};

export default function NewBookmarkCard() {
  const [input, setInput] = useState("");


  return (
    <div className="px-1">
      <Formik initialValues={newcard} onSubmit={handleOnSubmit}>
        <Form>
          <Card> 
            
            <Card.Body>
              <Card.Title>Add New Bookmark</Card.Title>
              <Card.Text className="title"> test</Card.Text>
              <JournalIcon/>
            </Card.Body>
           <Card.Footer className="card-footer">
              <div className="container">
                <input placeholder="Enter a tag" />
              </div>
              <Button type="submit">Submit</Button>
            </Card.Footer>
          </Card>
        </Form>
      </Formik>
    </div>
  );
}
