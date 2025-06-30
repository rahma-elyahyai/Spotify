import React from 'react'
import ArtistSongs from './ArtistSongs';
import ArtistUploadSongs from './ArtistUploadSongs';
import ArtistConcert from './ArtistConcert';
import ArtistAlbum from './ArtistAlbum';
import { ClipLoader } from "react-spinners";


const ArtistHomePage = ({ artist, setArtistHomebuttons, songs, artistHome, setArtistHome, uploadSongs, setUploadSongs, concert, setConcert, album, setAlbum }) => {
  setArtistHomebuttons(false);
  const [imageLoading, setImageLoading] = React.useState(true);
  React.useEffect(() => {
    setImageLoading(true);
  }, [artistHome])
  return (
    <>
      {(artistHome) ? (
        <div style={{ color: "white", alignContent: "center", margin: "10px", height: "auto" }}>
          {imageLoading && (
            <ClipLoader color="white" size={50} />
          )}
          <img
            src={`http://localhost:8080/artist/image/${artist.id}`}
            alt="Artist Profile"
            style={{
              width: "auto",
              height: "auto",
              maxWidth: "400px",
              maxHeight: "400px",
              display: imageLoading ? "none" : "block"
            }}
            onLoad={() => setImageLoading(false)}
            onError={() => setImageLoading(false)}
          />
          <h1><b>Name: </b>{artist.name}</h1>
          <p><b>Bio: </b>{artist.bio}</p>
          <b>Social links: </b><a href={artist.socialLinks}>Instagram</a>
          <h4>{artist.users ? <p>{artist.users.length} Followers</p> : ""}</h4>
        </div>
      ) : (
        <div style={{ height: "100vh" }}>
          {songs ? <ArtistSongs artist={artist} /> : ""}
          {uploadSongs ? <ArtistUploadSongs artist={artist} setArtistHome={setArtistHome} setUploadSongs={setUploadSongs} /> : ""}
          {album ? <ArtistAlbum artist={artist} setArtistHome={setArtistHome} setAlbum={setAlbum} /> : ""}
          {concert ? <ArtistConcert artist={artist} setArtistHome={setArtistHome} setConcert={setConcert} /> : ""}
        </div>
      )}
  
    </>
  );
}

export default ArtistHomePage;