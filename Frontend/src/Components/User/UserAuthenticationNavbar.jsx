import { Navbar, Nav, Container } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { Link } from 'react-router-dom';

const UserAuthenticationNavbar = () => {
    return (
        <Navbar variant="dark" expand="lg" fixed="top">
            <Container>

                <Navbar.Brand as={Link} to="/">
                    <i className="bi bi-house-door-fill" style={{ fontSize: '24px' }}></i>
                </Navbar.Brand>

                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/">Home</Nav.Link>
                </Nav>

                <Nav className="ms-auto">
                    <Nav.Link as={Link} to="/signup">Sign Up</Nav.Link>
                    <Nav.Link as={Link} to="/login">Log In</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
    )
}

export default UserAuthenticationNavbar