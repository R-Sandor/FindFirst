import Pagination from 'react-bootstrap/Pagination';

// Basic scaffolding 
export default function PaginationBar({active}: {active: number}) {
  let pageItem = [];
  let actPage = active;
  for (let pnum: number = 1; pnum <= 5; pnum++) {
    pageItem.push(
      <Pagination.Item key={pnum} active={pnum === active }>
        {pnum}
      </Pagination.Item>
    );
  }
}
