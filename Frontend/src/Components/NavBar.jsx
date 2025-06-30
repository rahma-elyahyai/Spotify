import { Navbar, Nav, Form, FormControl, Container } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';

const NavBar = ({ handleSearch, setSearchTerm, artistHomePage }) => {


  const [search, setSearch] = useState("");

    const handleSearchChange = (e) => {
      setSearch(e.target.value);
    };
  
    useEffect(() => {
      console.log(search)
      handleSearch(search);
      setSearchTerm(search);
    },[search])

  return (
    <Navbar variant="dark" expand="lg" fixed="top">
      <Container>
        <Navbar.Brand as={Link} to="/" onClick={() => artistHomePage(false)}>
          <i className="bi bi-house-door-fill" style={{ fontSize: '24px' }}></i>
        </Navbar.Brand>

        <Nav className="me-auto">
          <Nav.Link as={Link} to="/" onClick={() => artistHomePage(false)}>Home</Nav.Link>
        </Nav>
        <Form inline className="d-flex mx-auto home-searchContainer">
          <FormControl
            type="text"
            placeholder="Search for songs..."
            className="me-sm-2"
            style={{ width: "400px" }}
            onChange={handleSearchChange}
            value={search}
          />
        </Form>

        <Nav className="ms-auto">
          <Nav.Link as={Link} to="/signup">Sign Up</Nav.Link>
          <Nav.Link as={Link} to="/login">Log In</Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
}

export default NavBar;
