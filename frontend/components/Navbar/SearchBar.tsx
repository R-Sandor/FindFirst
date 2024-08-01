import { useState } from "react";
import { InputGroup, FormControl, Button } from "react-bootstrap";

interface SearchBarProps {
  onSearch: (query: string) => void;
}

// const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
//   const [query, setQuery] = useState("");

//   const handleSearch = () => {
//     onSearch(query);
//   };

//   return (
//     <InputGroup>
//       <FormControl
//         placeholder="Search bookmarks..."
//         value={query}
//         onChange={(e) => setQuery(e.target.value)}
//         data-testid="search-input"
//       />
//       <Button variant="secondary" onClick={handleSearch} data-testid="search-button">
//         Search
//       </Button>
//     </InputGroup>
//   );
// };

const SearchBar: React.FC = () => {
  return (
    <div className="input-group" data-testid="search-bar">
      <input
        className="form-control"
        data-testid="search-input"
        placeholder="Search bookmarks..."
      />
      <button className="btn btn-secondary" data-testid="search-button" type="button">
        Search
      </button>
    </div>
  );
};

export default SearchBar;
