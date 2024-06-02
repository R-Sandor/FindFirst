"use client";
import React, { useEffect, useState } from "react";
import { Badge, ListGroup } from "react-bootstrap";
import { useTags } from "@/contexts/TagContext";
import useAuth from "@components/UseAuth";
import api from "@/api/Api";
import { TagReqPayload, TagWithCnt } from "@/types/Bookmarks/Tag";
import styles from "./tag.module.scss";
import { useSelectedTags } from "@/contexts/SelectedContext";

const TagList = () => {
  const userAuth = useAuth();
  const tagMap = useTags();
  const [loading, setLoading] = useState(true);
  const { selected, setSelected } = useSelectedTags();
  useEffect(() => {
    if (userAuth) {
      api
        .getAllTags()
        .then((results) => {
          const tags: TagReqPayload[] = results.data as TagReqPayload[];
          console.log("getting all Tags:", tags);
          for (let tag of tags) {
            const twc: TagWithCnt = {
              tagTitle: tag.tag_title,
              count: tag.bookmarks.length,
              associatedBkmks: tag.bookmarks,
            };
            tagMap.set(tag.id, twc);
          }
        })
        .then(() => {
          setLoading(false);
        });
    }
  });

  function selectTag(event: any, title: string) {
    const idx = selected.indexOf(title);
    console.log(idx);
    if (idx >= 0) {
      console.log("Selected - remove");
      const updated = [...selected];
      updated.splice(idx, 1);
      setSelected(updated);
      event.target.classList.remove(styles.on);
    } else {
      console.log("OnSelected - on", title);
      setSelected([...selected, title]);
      event.target.classList.add(styles.on);
    }
  }

  let groupItems: any = [];
  tagMap.forEach((tagCnt, key) => {
    groupItems.push(
      <ListGroup.Item key={key} className="w-full m-0 p-0">
        <button
          onClick={(event) => selectTag(event, tagCnt.tagTitle)}
          className={`d-flex m-0 w-full btn ${styles.btn} justify-content-between align-items-start`}
        >
          {tagCnt.tagTitle}
          <Badge bg="primary" pill>
            {tagCnt.count}
          </Badge>
        </button>
      </ListGroup.Item>,
    );
  });

  if (groupItems.length == 0) {
    groupItems.push(
      <ListGroup.Item className="h-10 d-flex justify-content-between align-items-start">
        Tag List
        <Badge bg="primary" pill>
          Count
        </Badge>
      </ListGroup.Item>,
    );
  }

  return (
    <div>
      {!loading ? (
        <ListGroup className="pt-3 w-full  ml-1 p-0">{groupItems}</ListGroup>
      ) : (
        <div> loading</div>
      )}
    </div>
  );
};

export default TagList;
