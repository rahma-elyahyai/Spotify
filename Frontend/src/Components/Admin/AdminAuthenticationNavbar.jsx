import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import "/src/CSS/ArtistNavBar.css";
import { Link } from 'react-router-dom';

const AdminAuthenticationNavbar = ({ setLogin, setSignup, admin, setAdminHomePage, setAnalyicsPage }) => {

  const handleHome = () => {
    setAdminHomePage(true);
    setAnalyicsPage(false);
  }
  const handleAnalytics = () => {
    setAdminHomePage(false);
    setAnalyicsPage(true);
  }
  return (

    <Navbar variant="dark" expand="lg" fixed="top">
      {
        admin ? (
          <Container>
            <h2 style={{ color: "white" }}>forAdmin</h2>
            <div style={{ display: "flex", flexDirection: "row", gap: "30px" }}>
              <div className='navButtons' onClick={() => handleHome()}>Song Requests</div>
              <div className='navButtons' onClick={() => handleAnalytics()}>Analytics</div>
            </div>
            <Navbar.Brand as={Link} to="/" className="ml-auto d-flex align-items-center">
              <Button onclick={() => localStorage.clear()} variant="outline-danger" style={{ fontSize: "20px" }}>
                <i className="bi bi-box-arrow-right" />
              </Button>
            </Navbar.Brand>
          </Container>
        ) : (<Container>

          <Navbar.Brand as={Link} to="/">
            <i className="bi bi-house-door-fill" style={{ fontSize: '24px' }}></i>
          </Navbar.Brand>

          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">Home</Nav.Link>

          </Nav>

          <Nav className="ms-auto">
            <Nav.Link onClick={() => {
              setSignup(true)
              setLogin(false)
            }}>Sign Up</Nav.Link>
            <Nav.Link onClick={() => {
              setLogin(true)
              setSignup(false)
            }}>Log In</Nav.Link>
          </Nav>
        </Container>)
      }
    </Navbar>

  )
}

export default AdminAuthenticationNavbar