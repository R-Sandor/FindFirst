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
  const tagMap = useTags();
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (userAuth) {
      api.getAllTags().then((results) => {
        for (let tagCnt of results.data) {
          tagMap.set(tagCnt.tag.id, tagCnt);
        }
        setLoading(false);
      });
    }
  }, [userAuth]);

  let groupItems: any = [];
  tagMap.forEach((tagCnt, key) => {
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

  return (
    <div>
      {!loading ? <ListGroup>{groupItems}</ListGroup> : <div> loading</div>}
    </div>
  );
};

export default TagList;
