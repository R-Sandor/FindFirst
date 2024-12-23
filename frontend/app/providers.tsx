"use client";

import { BookmarkProvider } from "@/contexts/BookmarkContext";
import { TagCntProvider } from "contexts/TagContext";
import { SelectedTagProvider } from "contexts/SelectedContext";

export function Providers({
  children,
}: {
  children: Readonly<React.ReactNode>;
}) {
  return (
    <BookmarkProvider>
      <TagCntProvider>
        <SelectedTagProvider>{children}</SelectedTagProvider>
      </TagCntProvider>
    </BookmarkProvider>
  );
}
