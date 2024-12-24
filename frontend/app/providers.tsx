"use client";

import { BookmarkProvider } from "@/contexts/BookmarkContext";
import { TagCntProvider } from "contexts/TagContext";
import { SelectedTagProvider } from "contexts/SelectedContext";
import ChildrenProp from "@type/Common/ChildrenProp";

export function Providers({ children }: Readonly<ChildrenProp>) {
  return (
    <BookmarkProvider>
      <TagCntProvider>
        <SelectedTagProvider>{children}</SelectedTagProvider>
      </TagCntProvider>
    </BookmarkProvider>
  );
}
