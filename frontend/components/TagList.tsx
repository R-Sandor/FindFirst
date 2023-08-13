'use client'
import React, { useEffect, useState } from "react";
import Badge from "react-bootstrap/Badge";
import ListGroup from "react-bootstrap/ListGroup";
import Bookmark from "@components/Bookmarks/Bookmark";
import Tag from "@components/Bookmarks/Tag";
import api from "@api/Api";

const TagList: React.FC = () => {
  const [bookmark, setBookmarks] = useState<Bookmark[] | null>([]);

  useEffect(() => {
      let list: Bookmark [] = [];
      api.getAll().then((response) => {
      for (let bkmk of response.data) {
        list.push(bkmk)
      }
    });
    console.log(list)
  }, []);

  return (
    <ListGroup>
      <ListGroup.Item>No style</ListGroup.Item>
      <ListGroup.Item variant="primary">Primary</ListGroup.Item>
      <ListGroup.Item variant="secondary">Secondary</ListGroup.Item>
      <ListGroup.Item variant="success">Success</ListGroup.Item>
      <ListGroup.Item variant="danger">Danger</ListGroup.Item>
      <ListGroup.Item variant="warning">Warning</ListGroup.Item>
      <ListGroup.Item variant="info">Info</ListGroup.Item>
      <ListGroup.Item variant="light">Light</ListGroup.Item>
      <ListGroup.Item variant="dark">Dark</ListGroup.Item>
    </ListGroup>
  );
};

export default TagList;
