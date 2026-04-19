import { Container, Row, Col, Nav, Navbar } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/Footer.css';
import { Link, useNavigate } from 'react-router-dom';
import footerImage from "../assets/file-CoN3ZXq7YCM9mdeVHKJG1Z.jpg";

const Footer = () => {
  const navigate = useNavigate();
  const handleSignUpClick = () => {
    navigate('/signup');
  };
  return (
    <footer className="footer">
      <Container>
        <Row>
          <Col md={4} className="footer-column">
            <h5>About Us</h5>
            <p>
              Melodify - Music streaming system
            </p>
            <img src={footerImage} style={{ height: "200px", width: "200px" }} alt="Melodify" />
          </Col>

          <Col md={4} className="footer-column">
            <h5>Quick Links</h5>
            <Nav className="flex-column">

              <Nav.Link as={Link} to="/artists" className="footer-link">
                For Artists
              </Nav.Link>

              <Nav.Link as={Link} to="/admin" className="footer-link">
                For Admin
              </Nav.Link>

              <Nav.Link as={Link} to="/about" className="footer-link">
                About
              </Nav.Link>
            </Nav>
          </Col>

          <Col md={4} className="footer-column">
            <h5>Follow Us</h5>
            <Nav>
              <Nav.Link
                href="https://www.facebook.com"
                target="_blank"
                className="footer-social-link"
              >
                Facebook
              </Nav.Link>
              <Nav.Link
                href="https://www.twitter.com"
                target="_blank"
                className="footer-social-link"
              >
                Twitter
              </Nav.Link>
              <Nav.Link
                href="https://www.instagram.com"
                target="_blank"
                className="footer-social-link"
              >
                Instagram
              </Nav.Link>
            </Nav>
            <Navbar.Text>
              &copy; {new Date().getFullYear()} Melodify. All rights reserved.
            </Navbar.Text>
          </Col>
        </Row>
      </Container>
      <Navbar fixed="bottom" bg="dark" className="footer-navbar">
        <Container>
          <Navbar.Text>
            Preview of Melodify
            <br></br>
            Sign up to get unlimited songs.No credit card needed.
          </Navbar.Text>
          <button className="signUp" onClick={handleSignUpClick}>
            Sign Up free
          </button>
        </Container>
      </Navbar>

    </footer>
  );
};

export default Footer;
