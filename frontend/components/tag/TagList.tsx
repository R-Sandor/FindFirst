"use client";
import React, { useContext, useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import {
  useTags,
  useTagsDispatch,
} from "@/contexts/TagContext";
import useAuth from "@components/UseAuth";
import api from "@/api/Api";
import { TagReqPayload, TagWithCnt } from "@/types/Bookmarks/Tag";

const TagList = () => {
  const userAuth = useAuth();
  const tagMap = useTags();
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (userAuth) {
      api.getAllTags().then((results) => {
        const tags: TagReqPayload[] =  results.data as TagReqPayload[]
        console.log("getting all Tags:", tags);
        for (let tag of tags) {
          const twc: TagWithCnt = { 
            tagTitle: tag.tag_title,
            count: tag.bookmarks.length
          }
          tagMap.set(tag.id,  twc);
        }
      }).then(() => { 
        setLoading(false);
      });
    }
  }, [tagMap, userAuth]);

  let groupItems: any = [];
  tagMap.forEach((tagCnt, key) => {
    groupItems.push(
      <ListGroup.Item
        key={key}
        className="d-flex justify-content-between align-items-start"
      >
        {tagCnt.tagTitle}
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
