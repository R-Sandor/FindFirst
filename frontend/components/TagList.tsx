"use client";
import React, { useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import TagWithCntList from "@/types/Bookmarks/TagWithCntList";
import TagWithCnt  from "@/types/Bookmarks/TagWithCnt";

interface TagProp {
  tagsWithCnt: TagWithCnt[];
}

// 
const Tags = ({ tagsWithCnt: tagsWithCnt }: TagProp) => {
  console.log("tagsWithCnt " + tagsWithCnt)
  return (
    <div>
      <ListGroup>
        {tagsWithCnt.map((tagCnt) => (
          <ListGroup.Item
            key={tagCnt.tag.id}
            className="d-flex justify-content-between align-items-start"
          >
            {tagCnt.tag.tag_title}
            <Badge bg="primary" pill>
              { tagCnt.count }
            </Badge>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </div>
  );
};

// Pass in our TagsWithCnt[] i.e., the TagsWithCntList.
function TagList({ tagsCounted }: TagWithCntList) { 
  return <div>{Tags({ tagsWithCnt: tagsCounted })}</div>;
};

export default TagList;
