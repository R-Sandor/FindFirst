'use client'
import React, { useEffect, useState } from "react";
import Badge from "react-bootstrap/Badge";
import ListGroup from "react-bootstrap/ListGroup";
import Bookmark from "@components/Bookmarks/Bookmark";
import Tag from "@components/Bookmarks/Tag";
import api from "@api/Api";

interface TagProp {
  tags: Tag[]
}


const Tags = ({ tags }:TagProp) => {
  return  (
    <div>
    <ListGroup>
      {tags.map(tag =>
        <ListGroup.Item key={tag.id}>
           {tag.title}
        </ListGroup.Item>
      )}
    </ListGroup>
    </div>
  )

}

const TagList: React.FC = () => {
  const [tags, setTags] = useState<Tag[]>([]);

  useEffect(() => {
      let list: Tag [] = [];
      api.getAllTags().then((response) => {
      for (let tag of response.data) {
        list.push(tag)
      }
    });
    setTags(list);
    console.log(list)
  }, []);

  return (
    <div>
    {Tags({tags: tags})}
    </div>
  );
};

export default TagList;
