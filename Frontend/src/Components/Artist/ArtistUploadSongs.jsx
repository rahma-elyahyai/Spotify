import React from "react";
import "../../CSS/SignUp.css";
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import axiosInstance from "../AxiosInstance";
import { ClipLoader } from "react-spinners";
const ArtistUploadSongs = ({ artist, setArtistHome, setUploadSongs }) => {
  const [formData, setFormData] = React.useState({
    title: "",
    genre: "",
    coverImage: null,
    mp3File: null,
    language: "",
    releaseDate: "",
    lyrics: "",
    albumId: ""
  });

  const [data, setData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [uploading,setUploading]=React.useState(false);
  const [backendError, setBackendError] = React.useState("");
  const [error, setError] = React.useState(false);
  const { VITE_SONG_POST, VITE_ARTIST_GET } = import.meta.env;


  React.useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get(`${VITE_ARTIST_GET}/${artist.id}`);
        setData(response.data);
        setLoading(false);

      }
      catch (error) {
        console.error("Error posting data:", error);

        if (error.response && error.response.data) {

          toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
          setBackendError(error.response.data.error);
        } else {

          toast.error("Something went wrong. Please try again.");
        }
      }
    };
    fetchData();
  }, [artist.id]);


  console.log(artist.albums)
  const handleTitle = (e) => {
    setFormData((prev) => ({ ...prev, title: e.target.value }));
  };

  const handleGenre = (e) => {
    setFormData((prev) => ({ ...prev, genre: e.target.value }));
  };

  const handleLanguage = (e) => {
    setFormData((prev) => ({ ...prev, language: e.target.value }));
  };

  const handleReleaseDate = (e) => {
    setFormData((prev) => ({ ...prev, releaseDate: e.target.value }));
  };

  const handleLyrics = (e) => {
    setFormData((prev) => ({ ...prev, lyrics: e.target.value }));
  };

  const handleAlbum = (e) => {
    setFormData((prev) => ({ ...prev, albumId: e.target.value }));
  };


  const handleCoverImage = (e) => {
    const file = e.target.files[0];
    if (file) {
      const fileExtension = file.name.split('.').pop().toLowerCase();
      const allowedExtensions = ['jpg', 'jpeg'];

      if (!allowedExtensions.includes(fileExtension)) {
        toast.error("Cover image must be a JPG or JPEG file.");
        return;
      }
      setFormData((prev) => ({ ...prev, coverImage: file }));
    }
  };

  const handleMusicFile = (e) => {
    const file = e.target.files[0];
    if (file) {

      const fileExtension = file.name.split('.').pop().toLowerCase();
      const allowedExtensions = ['mp3'];

      if (!allowedExtensions.includes(fileExtension)) {
        toast.error("Audio file must be an MP3 file.");
        return;
      }
      setFormData((prev) => ({ ...prev, mp3File: file }));
    }
  };

  const handleSubmitLastStep = async (e) => {
    e.preventDefault();
    setUploading(true);
    const form = new FormData();
    form.append("title", formData.title);
    form.append("genre", formData.genre);
    form.append("language", formData.language);
    form.append("playCount", 0);
    form.append("coverImage", formData.coverImage);
    form.append("releaseDate", formData.releaseDate);
    form.append("mp3File", formData.mp3File);
    form.append("favorite", 0);
    form.append("artistId", artist.id);
    form.append("lyrics", formData.lyrics);
    form.append("albumId", formData.albumId);


    try {
      const response = await axios.post(VITE_SONG_POST, form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      toast.success('Successful');
      setTimeout(() => {
        setUploadSongs(false);
        setArtistHome(true);
        setError(false);
      }, 500);
      setUploading(false);
      console.log(response.data);
    } catch (error) {
        console.log(error.response.data);
        if (error.response && error.response.data.errorResponse) {
          toast.error(`${error.response.data.errorResponse.errorCode}\n${error.response.data.errorResponse.details}`);
          console.log(error.response.data.errorResponse.message);
          setError(true);
          setBackendError(error.response.data.errorResponse.message);
          setLoading(false);
          setUploading(false);
        } else {
          setError(true);
          setUploading(false);
          setBackendError("Something went wrong. Please try again.");
          toast.error("Something went wrong. Please try again.");
        }
    }

  };

  if (loading) {
    return (
      <div style={{ color: "white", textAlign: "center", marginTop: "20px", position: "fixed", top: "40%", left: "45%" }}>
        <h3>Loading...</h3> <ClipLoader color="white" size={50} />
      </div>
    );
  }

  return (
<>
{uploading ? <div className="overlay visible" /> : ""}
<div className="signup-container">
      <h3 className="signup-title">Upload Songs</h3>
      <form className="signup-form">
        <label>Title</label>
        <input type="text" name="title" value={formData.title} onChange={handleTitle} />

        <label>Genre</label>
        <input type="text" name="genre" value={formData.genre} onChange={handleGenre} />

        <label>Album</label>
        <select onChange={handleAlbum}>
          <option value="">-- Select an Album --</option>
          {
            (data.albums) ? data.albums.map(album => (
              <option key={album.id} value={album.id} >
                {album.title}
              </option>
            )) : ""


          }

        </select>
        <br/>
        <label>Language</label>
        <input type="text" name="language" value={formData.language} onChange={handleLanguage} />

        <label>Release Date</label>
        <input type="date" name="releaseDate" value={formData.releaseDate} onChange={handleReleaseDate} />

        <label>Cover Image</label>
        <input type="file" name="coverImage" accept=".jpg" onChange={handleCoverImage} />
        {formData.coverImage && (
          <div className="profile-picture-preview">
            <img
              src={URL.createObjectURL(formData.coverImage)}
              alt="Cover Preview"
              width="100"
              height="100"
            />
          </div>
        )}

        <label>Audio File</label>
        <input type="file" name="mp3File" accept=".mp3" onChange={handleMusicFile} />

        <label>Lyrics</label>
        <input type="text" name="lyrics" value={formData.lyrics} onChange={handleLyrics} />

        <button type="submit" onClick={handleSubmitLastStep}>
          Submit
        </button>
       
        {error ? <span>{backendError}</span> : ""}
      </form>
    </div>
    <ToastContainer/>
</>
   
  );
};

export default ArtistUploadSongs;
