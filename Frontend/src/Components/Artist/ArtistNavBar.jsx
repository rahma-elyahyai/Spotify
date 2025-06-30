import {
  Navbar,
  Nav,
  Button,
  Container,
} from "react-bootstrap";

import "bootstrap-icons/font/bootstrap-icons.css";
import "/src/CSS/ArtistNavBar.css";
import { Link } from "react-router-dom";
const ArtistNavBar = ({
  setLogin,
  setSignup,
  artistHomebuttons,
  songs,
  setSongs,
  setArtistHome,
  uploadSongs,
  setUploadSongs,
  concert,
  setConcert,
  album,
  setAlbum,
}) => {
  const handleLogIn = () => {
    setLogin(true);
    setSignup(false);
  };
  const handleSignUp = () => {
    setSignup(true);
    setLogin(false);
  };
  const handleSongs = () => {
    setSongs(true);
    setArtistHome(false);
    setUploadSongs(false);
    setConcert(false);
    setAlbum(false);
  };
  const handleHome = () => {
    setArtistHome(true);
    setSongs(false);
    setUploadSongs(false);
    setConcert(false);
    setAlbum(false);
  };
  const handleUploadSongs = () => {
    setUploadSongs(true);
    setArtistHome(false);
    setSongs(false);
    setConcert(false);
    setAlbum(false);
  };
  const handleConcert = () => {
    setConcert(true);
    setArtistHome(false);
    setSongs(false);
    setUploadSongs(false);
    setAlbum(false);
  };
  const handleAlbum = () => {
    setAlbum(true);
    setConcert(false);
    setArtistHome(false);
    setSongs(false);
    setUploadSongs(false);
  };
  return (
    <Navbar variant="dark" expand="lg" fixed="top">
      <Container>
        <h2 style={{ color: "white" }}>forArtists</h2>

        {!artistHomebuttons || uploadSongs || concert || album || songs ? (
          ""
        ) : (
          <Nav className="me-auto" style={{ padding: "15px" }}>
            <Nav.Link as={Link} to="/">Home</Nav.Link>
          </Nav>
        )}
        <div style={{ display: "flex", flexDirection: "row", gap: 10 }}>
          {artistHomebuttons ? (
            <>
              <button onClick={handleLogIn}>Log in</button>
              <button onClick={handleSignUp}>Get access</button>
            </>
          ) : (
            <div
              style={{
                color: "white",
                display: "flex",
                flexDirection: "row",
                gap: 30,
              }}
            >
              <div
                className="navButtons"
                style={{ cursor: "pointer" }}
                onClick={handleHome}
              >
                <h6>Home</h6>
              </div>
              <div
                className="navButtons"
                style={{ cursor: "pointer" }}
                onClick={handleSongs}
              >
                <h6>Songs</h6>
              </div>
              <div
                className="navButtons"
                style={{ cursor: "pointer" }}
                onClick={handleUploadSongs}
              >
                {" "}
                <h6>Upload Songs</h6>
              </div>
              <div
                className="navButtons"
                style={{ cursor: "pointer" }}
                onClick={handleAlbum}
              >
                {" "}
                <h6>Albums</h6>
              </div>
              <div
                className="navButtons"
                style={{ cursor: "pointer" }}
                onClick={handleConcert}
              >
                <h6>Concerts</h6>
              </div>
            </div>
          )}
        </div>

        {!artistHomebuttons || uploadSongs || concert || album || songs ? (
          <Navbar.Brand as={Link} to="/" className="ml-auto d-flex align-items-center">
            <Button
              variant="outline-danger"
              style={{ fontSize: "20px", margin: "10px" }}
            >
              <i className="bi bi-box-arrow-right" />
            </Button>
          </Navbar.Brand>
        ) : (
          ""
        )}
      </Container>
    </Navbar>
  );
};

export default ArtistNavBar;
