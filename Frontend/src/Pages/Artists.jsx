import VideoCarousel from "../Components/VideoCarousel";
import ArtistNavBar from "../Components/Artist/ArtistNavBar";
import { useState } from "react";
import ArtistLogin from "../Components/Artist/ArtistLogin";
import ArtistSignUp from "../Components/Artist/ArtistSignUp";

const Artists = () => {
  const [login, setLogin] = useState(false);
  const [signup, setSignup] = useState(false);
  const [artistHomebuttons, setArtistHomebuttons] = useState(true);
  const [songs, setSongs] = useState(false);
  const [artistHome, setArtistHome] = useState(true);
  const [uploadSongs, setUploadSongs] = useState(false);
  const [concert, setConcert] = useState(false);
  const [album, setAlbum] = useState(false)
  return (
    <div style={{ display: "flex", flexDirection: "column", backgroundColor: "black", height: "100vh" }}>
      <div
        style={{
          position: "fixed",
          top: 0,
          width: "100%",
          backgroundColor: "rgb(11, 11, 11)",
          height: "80px",
        }}
      >
        <ArtistNavBar login={login} setLogin={setLogin} signup={signup} setSignup={setSignup} artistHomebuttons={artistHomebuttons} setArtistHomebuttons={setArtistHomebuttons} songs={songs} setSongs={setSongs} artistHome={artistHome} setArtistHome={setArtistHome} uploadSongs={uploadSongs} setUploadSongs={setUploadSongs} concert={concert} setConcert={setConcert} album={album} setAlbum={setAlbum} />
      </div>

      <div style={{ marginTop: "80px"}}>
        <VideoCarousel login={login} signup={signup} />
      </div>
      <div>
        {(login) ? <ArtistLogin setArtistHomebuttons={setArtistHomebuttons} songs={songs} setSongs={setSongs} artistHome={artistHome} setArtistHome={setArtistHome} uploadSongs={uploadSongs} setUploadSongs={setUploadSongs} concert={concert} setConcert={setConcert} album={album} setAlbum={setAlbum} /> : ""}
        {(signup) ? <ArtistSignUp setLogin={setLogin} setSignup={setSignup} /> : ""}
      </div>


    </div>
  );
};

export default Artists;
