"use client";
import React, { useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import TagWithCntList from "@/types/Bookmarks/TagWithCntList";
import TagWithCnt  from "@/types/Bookmarks/TagWithCnt";

interface TagProp {
  tags: TagWithCnt[];
}

// 
const Tags = ({ tags }: TagProp) => {
  return (
    <div>
      <ListGroup>
        {tags.map((tag) => (
          <ListGroup.Item
            key={tag.tag.id}
            className="d-flex justify-content-between align-items-start"
          >
            {tag.tag.title}
            <Badge bg="primary" pill>
              { tag.cnt }
            </Badge>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </div>
  );
};

// Pass in our TagsWithCnt[] i.e., the TagsWithCntList.
function TagList({ tagsCounted }: TagWithCntList) { 
  return <div>{Tags({ tags: tagsCounted })}</div>;
};

export default TagList;
