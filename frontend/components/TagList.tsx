"use client";
import React, { useContext, useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import {
  useTags,
  useTagsDispatch,
} from "@/contexts/TagContext";
import useAuth from "@components/UseAuth";
import api from "@/api/Api";

const TagList = () => {
  const userAuth = useAuth();
  const dispatch = useTagsDispatch();
  const tagMap = useTags();
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (userAuth) {
      api.getAllTags().then((results) => {
        for (let tagCnt of results.data) {
          console.log(tagCnt.tag.tag_title);
          tagMap.set(tagCnt.tag.id, tagCnt);
        }
        setLoading(false);
      });
      console.log(tagMap);
    }
  }, [userAuth]);

  let groupItems: any = [];
  console.log(tagMap);
  tagMap.forEach((tagCnt, key) => {
    console.log(tagCnt);
    groupItems.push(
      <ListGroup.Item
        key={tagCnt.tag.id}
        className="d-flex justify-content-between align-items-start"
      >
        {tagCnt.tag.tag_title}
        <Badge bg="primary" pill>
          {tagCnt.count}
        </Badge>
      </ListGroup.Item>
    );
  });
  console.log(groupItems);

  return (
    <div>
      {!loading ? <ListGroup>{groupItems}</ListGroup> : <div> loading</div>}
    </div>
  );
};

export default TagList;
