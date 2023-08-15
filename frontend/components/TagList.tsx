"use client";
import React, { useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import Bookmark from "@/types/Bookmarks/Bookmark";
import Tag from "@/types/Bookmarks/Tag";
import api from "@api/Api";

interface TagProp {
  tags: Tag[];
}

const Tags = ({ tags }: TagProp) => {
  return (
    <div>
      <ListGroup>
        {tags.map((tag) => (
          <ListGroup.Item
            key={tag.id}
            className="d-flex justify-content-between align-items-start"
          >
            {tag.title}
            <Badge bg="primary" pill>
              3
            </Badge>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </div>
  );
};

const TagList: React.FC = () => {
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let list: Tag[] = [];
    api.getAllTags().then((response) => {
      for (let tag of response.data) {
        list.push(tag);
      }
      setLoading(false);
    });
    setTags(list);
    console.log(list);
  }, []);

  if (loading) return <p> Loading Data</p>;

  return <div>{Tags({ tags: tags })}</div>;
};

export default TagList;
