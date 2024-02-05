import {
  Dispatch,
  SetStateAction,
  createContext, useContext, useState
} from "react";

export interface TagProvider {
  selected: string[],
  setSelected: Dispatch<SetStateAction<string[]>>
}

export const SelectedTagContext = createContext<TagProvider>({
  selected: [],
  setSelected: () => { }
});

export function SelectedTagProvider({ children }: { children: React.ReactNode }) {
  const [selected, setSelected] = useState<string[]>([]);

  return (
    <SelectedTagContext.Provider value={{ selected, setSelected }}>
      {children}
    </SelectedTagContext.Provider>
  )
}

export function useSelectedTags() {
  return useContext(SelectedTagContext)
}
