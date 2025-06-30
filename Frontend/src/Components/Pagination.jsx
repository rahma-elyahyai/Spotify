import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import "../CSS/Pagination.css"
import { Typography } from '@mui/material';
import { useEffect } from 'react';

const AdminPagination = ({page,setPage,fetchData,totalPages}) => {
 
    const handleChange = (event, value) => {
      setPage(value-1);
    };
    useEffect(() => {
      fetchData();
    }, [page]); 

    return (
      <Stack spacing={2}>
        <Typography>Page: {page + 1}</Typography>
        <Pagination 
          count={totalPages} 
          page={page + 1} 
          onChange={handleChange} 
          color="primary" 
        />
      </Stack>
    );
}

export default AdminPagination