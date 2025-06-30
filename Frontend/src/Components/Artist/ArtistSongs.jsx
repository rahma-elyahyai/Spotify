import React, { useEffect, useState } from "react";
import { ClipLoader } from "react-spinners";
import axiosInstance from "../AxiosInstance";
import { ToastContainer, toast } from 'react-toastify';
import "../../CSS/ArtistSongs.css";
import DeleteSongModal from "../DeleteSongModal";
import EditSongModal from "./ArtistSongModal";

const ArtistSongs = ({ artist }) => {
  const [audioUrl, setAudioUrl] = useState("");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [audioError, setAudioError] = useState("");
  const audioRef = React.useRef(null);


  const { VITE_ARTIST_GET, VITE_SONG_STREAM, VITE_FETCH_SONG_IMAGE, VITE_ALBUM_IMAGE } = import.meta.env;

  const handlePlay = (id) => {
    const timestamp = new Date().getTime();
    setAudioUrl(`${VITE_SONG_STREAM}/${id}?${timestamp}`);
  };
  useEffect(() => {

    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await axiosInstance.get(`${VITE_ARTIST_GET}/${artist.id}`);
      console.log(response);
      setData(response.data);
      setLoading(false);
  
   
      if (audioRef.current) {
        audioRef.current.pause();
        audioRef.current.currentTime = 0; 
      }
      setAudioUrl(""); 
    } catch (error) {
      console.error("Error posting data:", error);
  
      if (error.response && error.response.data) {
        toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }
  };
  
  useEffect(() => {
    if (audioUrl && audioRef.current) {
      audioRef.current.load();
      audioRef.current.play();
    }
  }, [audioUrl]);

  if (loading) {
    return (
      <div style={{ color: "white", textAlign: "center", marginTop: "20px", position: "fixed", top: "40%", left: "43%" }}>
        <h3>Loading songs</h3> <ClipLoader color="white" size={50} />
      </div>
    );
  }

  if (!data || !data.songs || data.songs.length === 0) {
    return (
      <div style={{ color: "white", textAlign: "center", marginTop: "20px" }}>
        No songs available for this artist.
      </div>
    );
  }

  return (
    <div style={{ height: "auto", minHeight: "100vh" }}>
      <br />
      <h4 style={{ color: "white", height: "100%" }}>
        <b>Uploaded Songs</b>
      </h4>
      <br />
      <div className="container">
        <table className="table table-striped table-dark">
          <ToastContainer />
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Genre</th>
              <th>Language</th>
              <th>Release Date</th>
              <th>Status</th>
              <th>Cover Image</th>
              <th>Album</th>
              <th>Play Song</th>
              <th>Edit Song</th>
              <th>Delete song</th>
            </tr>
          </thead>
          <tbody>
            {data.songs.map((item) => (
              <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.title}</td>
                <td>{item.genre}</td>
                <td>{item.language}</td>
                <td>{item.releaseDate}</td>
                <td>{item.status}</td>
                <td>
                  <img
                      src={`${VITE_FETCH_SONG_IMAGE}/${item.id}?${new Date().getTime()}`}
                    style={{
                      borderRadius: "50%",
                      height: "100px",
                      width: "100px",
                    }}
                    alt={item.title}
                  />
                </td>

                <td>

                  {(item.albumId) ? (<img
                    src={`${VITE_ALBUM_IMAGE}/${item.albumId}`}
                    style={{
                      borderRadius: "50%",
                      height: "100px",
                      width: "100px",
                    }}
                    alt={item.title}
                  />) : ("---")
                  }

                </td>

                <td>
                  <button onClick={() => handlePlay(item.id)} aria-label={`Play ${item.title}`}>
                    Play
                  </button>
                </td>
                <td>
                  <EditSongModal  song={item} fetchData={fetchData}/>
                </td>
                <td>
                  <DeleteSongModal songId={item.id} songName={item.title} fetchData={fetchData} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flex: "1",
            marginTop: "20px",
            marginBottom: "30px",
          }}
        >
          {audioUrl ? (
            <audio
              ref={audioRef}
              crossOrigin="anonymous"
              controls
              style={{ width: "80%", Color: "black" }}
              onError={() => setAudioError("Failed to load the audio.")}
            >
              <source src={audioUrl} type="audio/mp3" />
              Your browser does not support the audio element.
            </audio>
          ) : (
            audioError && <div style={{ color: "red" }}>{audioError}</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ArtistSongs;
