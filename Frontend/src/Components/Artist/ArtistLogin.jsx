import { useState } from 'react'
import "../../CSS/Login.css"
import axios from 'axios';
import ArtistHomePage from './ArtistHomePage';
import Overlay from "../Overlay";
import { toast, ToastContainer } from 'react-toastify';

const ArtistLogin = ({ setArtistHomebuttons, songs, setSongs, artistHome, setArtistHome, uploadSongs, setUploadSongs, concert, setConcert, album, setAlbum }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [home, setHome] = useState(false);
  const [artist, setArtist] = useState(null);
  const { VITE_ARTIST_LOGIN } = import.meta.env;
  const [loading,setLoading]=useState(false);

  const handleEmail = (e) => {
    setEmail(e.target.value);
  };

  const userData = { email, password };

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    axios
      .post(VITE_ARTIST_LOGIN, userData)
      .then((response) => {
        setLoading(false);
        console.log(response.data);
        const name = response.data.name;
        const token = response.data.token;
        localStorage.setItem("token", token);
        console.log(name);
        setArtist(response.data.artist);
        setHome(true);
        

      })
      .catch((error) => {
        console.error(error);
        setLoading(false);
        if (error.response && error.response.data) {
          toast.error(` ${error.response.data.errorCode}\n ${error.response.data.details}`);
          setError(error.response.data.message);
        } else {
          setError("Something went wrong. Please try again.");
        }
      });

  }

  const handlePassword = (e) => {
    setPassword(e.target.value);
  };
  return (
    <div>
    {loading && <Overlay show={true}/>}
      {
        (!home) ? (<div className="login-container">
          <h3 className="login-title">Log in to Melodify </h3>
          <form className="login-form">
            <label>Email</label>
            <input
              type="email"
              placeholder="enter your email"
              value={email}
              onChange={handleEmail}
               maxLength="50"
            />
            <label>Password</label>
            <input type="password" placeholder="enter your password" maxLength="50" value={password} onChange={handlePassword} />

            {error && <span>{error}</span>}
            <button
              type="submit"
              onClick={(e) => {
                handleSubmit(e);
              }}
            >
              Login
            </button>
          </form>
        </div>) : (<ArtistHomePage artist={artist} setArtistHomebuttons={setArtistHomebuttons} songs={songs} setSongs={setSongs} artistHome={artistHome} setArtistHome={setArtistHome} uploadSongs={uploadSongs} setUploadSongs={setUploadSongs} concert={concert} setConcert={setConcert} album={album} setAlbum={setAlbum} />)
      }
      <ToastContainer />
    </div>
  );
}

export default ArtistLogin