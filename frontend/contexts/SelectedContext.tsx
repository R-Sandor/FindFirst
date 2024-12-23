import {
  Dispatch,
  SetStateAction,
  createContext,
  useContext,
  useMemo,
  useState,
} from "react";

export interface TagProvider {
  selected: string[];
  setSelected: Dispatch<SetStateAction<string[]>>;
}

export const SelectedTagContext = createContext<TagProvider>({
  selected: [],
  setSelected: () => {},
});

export function SelectedTagProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [selected, setSelected] = useState<string[]>([]);

  const selectedMemo = useMemo(() => ({ selected, setSelected }), [selected]);

  return (
    <SelectedTagContext.Provider value={selectedMemo}>
      {children}
    </SelectedTagContext.Provider>
  );
}

export function useSelectedTags() {
  return useContext(SelectedTagContext);
}
