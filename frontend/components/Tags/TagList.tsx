"use client";
import React, { useEffect, useState } from "react";
import { ArrowsCollapseVertical } from 'react-bootstrap-icons';
import { Badge, Button, ListGroup } from "react-bootstrap";
import { useTags } from "@/contexts/TagContext";
import useAuth from "@components/UseAuth";
import api from "@/api/Api";
import { TagReqPayload, TagWithCnt } from "@/types/Bookmarks/Tag";
import styles from "./tag-pill.module.scss";
import { useSelectedTags } from "@/contexts/SelectedContext";

const TagList = () => {
  const userAuth = useAuth();
  const tagMap = useTags();
  const [loading, setLoading] = useState(false);
  const { selected, setSelected } = useSelectedTags();
  useEffect(() => {
    if (userAuth && tagMap.size == 0) {
      setLoading(true);
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
  }, [tagMap, userAuth]);

  function selectTag(event: any, title: string) {
    const idx = selected.indexOf(title);
    console.log(idx);
    if (idx >= 0) {
      const updated = [...selected];
      updated.splice(idx, 1);
      setSelected(updated);
      event.target.classList.remove(styles.on);
    } else {
      setSelected([...selected, title]);
      event.target.classList.add(styles.on);
    }
  }

  let groupItems: any = [];
  tagMap.forEach((tagCnt, key) => {
    groupItems.push(
      <ListGroup.Item key={`${key}-item`} className="w-full m-0 p-0">
        <button
          onClick={(event) => selectTag(event, tagCnt.tagTitle)}
          data-testid={`${tagCnt.tagTitle}-list-item`}
          key={`${tagCnt.tagTitle}-list-item`}
          className={`d-flex m-0 w-full btn ${styles.btn} justify-content-between align-items-start`}
        >
          {tagCnt.tagTitle}
          <Badge pill bg="" style={{ backgroundColor: '#717a83', color: 'white' }}>
            <div
              data-testid={`${tagCnt.tagTitle}-list-item-cnt`}
              key={`${tagCnt.tagTitle}-list-item-badge`}
            >
              {tagCnt.count}
            </div>
          </Badge>
        </button>
      </ListGroup.Item>,
    );
  });

  if (groupItems.length == 0) {
    groupItems.push(
      <ListGroup.Item
        key="no-items"
        className="h-10 d-flex justify-content-between align-items-start"
      >
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
        <><ListGroup className="pt-3 w-full">{groupItems}</ListGroup></>
      ) : (
        <div></div>
      )}
    </div>
  );
};

export default TagList;
