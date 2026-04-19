import axios from "axios";
import { useEffect, useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import "../CSS/Navbar.css";
import "../CSS/UserHome.css";
import NavBar from "./NavBar";
import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";
import UserArtistPage from "../Components/User/UserArtistPage"
import Overlay from "./Overlay";
import { debounce } from "lodash";
import stackIcon from "../assets/stack.png";
import plusIcon from "../assets/plus.png";


const HomePage = () => {

  const [filteredSongs, setFilteredSongs] = useState([]);
  const [topArtists, setTopArtists] = useState([]);
  const [filteredArtists, setFilteredArtists] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [songloading, setSongloading] = useState(false);
  const [artistsLoading, setArtistsLoading] = useState(false);
  const [topSongs, setTopSongs] = useState([]);
  const { VITE_ARTIST_TOP_GET, VITE_TOP_FIVE_SONGS_APPROVED_GET, VITE_ARTIST_GET, VITE_ARTIST_IMAGE, VITE_FETCH_SONG_IMAGE } = import.meta.env;
  const [ArtistHomePage, setArtistPage] = useState(false);
  const [selectedArtist, setSelectedArtist] = useState(null);
  const [searchLoading, setSearchLoading] = useState(false);



  useEffect(() => {
    const fetchTopArtists = async () => {
      try {
        setLoading(true);
        const response = await axios.get(VITE_ARTIST_TOP_GET);
        setTopArtists(response.data);
        setLoading(false);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch artists. Please try again.");
        setLoading(false);
      }
    };
    fetchTopArtists();
  }, []);

  const handlePlayList = () => {
    toast.info("Login to create and share Playlists ");
  }


  useEffect(() => {
    const fetchTopSongs = async () => {
      try {
        setSongloading(true);
        const response = await axios.get(VITE_TOP_FIVE_SONGS_APPROVED_GET);
        setTopSongs(response.data.body);
        console.log(response.data.body);
        setSongloading(false);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch songs. Please try again.");
        setSongloading(false);
      }
    };
    fetchTopSongs();
  }, []);

  const handleSearch = debounce(async (term) => {
    setSearchTerm(term);


    if (!term) {
      setFilteredSongs([]);
      setFilteredArtists([]);
    } else if (term.length >= 3) {
      try {
        setSearchLoading(true);
        const response = await axios.get(`http://localhost:8080/artist/songs/search/${term}`);
        console.log(response.data);
        setFilteredSongs(response.data.body.song);
        setFilteredArtists(response.data.body.artist);
        setSearchLoading(false);
      } catch (error) {
        console.error("Search failed:", error);
        toast.error("Search failed. Please try again.");
      }
    }
  }, 500);



  const handleSongSelect = () => {
    toast.info("Sign up to start listening for free on Melodify");
    setSearchTerm("");
  };

  const handleArtistSelect = (artist) => {

    const fetchArtist = async () => {
      try {
        setArtistsLoading(true);
        const response = await axios.get(`${VITE_ARTIST_GET}/${artist.id}`);
        console.log(response.data)
        setArtistPage(true);
        setSelectedArtist(response.data);
        setArtistsLoading(false);
        setSearchTerm("");
      } catch (error) {
        console.log(error);
        toast.error("Failed to fetch songs. Please try again.");

      }
    };
    fetchArtist();
  };


  return (
    <>
      {searchTerm &&
        (filteredSongs.length > 0 || filteredArtists.length > 0) && (
          <div className="overlay visible" onClick={() => setSearchTerm("")} />
        )}
      {loading ? <div className="overlay visible" /> : ""}
      {artistsLoading && <Overlay show={true} />}

      <div
        style={{
          position: "fixed",
          top: 0,
          width: "100%",
          backgroundColor: "rgb(11, 11, 11)",
          height: "70px",
          zIndex: 10,
        }}
      >
        <NavBar handleSearch={handleSearch} setSearchTerm={setSearchTerm} artistHomePage={setArtistPage} />
        {searchTerm && (
          <div className="home-searchSuggestions">
            {filteredSongs.length > 0 && (
              <div>
                {filteredSongs
                  .map((song) => (
                    <div
                      key={song.id}
                      className="home-suggestionItem"
                      onClick={() => handleSongSelect(song)}
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        gap: 20,
                        alignItems: "center",
                      }}
                    >
                      {song.title}
                      <img
                        src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                        style={{
                          height: "50px",
                          width: "50px",
                          objectFit: "contain",
                          borderRadius: "50%",
                        }}
                        loading="lazy"
                      />
                    </div>
                  ))}
              </div>
            )}
            {filteredArtists.length > 0 && (
              <div>
                {filteredArtists
                  .map((artist) => (
                    <div
                      key={artist.id}
                      className="home-suggestionItem"
                      onClick={() => handleArtistSelect(artist)}
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        gap: 20,
                        alignItems: "center",
                      }}
                    >
                      {artist.name}
                      <img
                        src={`${VITE_ARTIST_IMAGE}/${artist.id}`}
                        style={{
                          height: "50px",
                          width: "50px",
                          objectFit: "contain",
                          borderRadius: "50%",
                        }}
                        loading="lazy"
                      />
                    </div>
                  ))}
              </div>
            )}
            {searchLoading ? (
              <div className="spinner-search-container">
                 <Skeleton count={5} height={67} width={370} className="skeleton-item"/>
              </div>
            ) : (filteredSongs.length === 0 && filteredArtists.length === 0) ? (
              <div className="home-suggestionItem">No results found</div>
            ) : null}

          </div>
        )}
      </div>

      <div className="mainContent">
        <div className="sidePane">
          <div className="library">
            <div>
              <img src={stackIcon} alt="library-icon" />
            </div>
            <div>Your Library</div>
            <div onClick={handlePlayList} className="addButton">
            <img src={plusIcon} alt="add-icon" />
            </div>
          </div>

          <br />
          <div
            style={{
              backgroundColor: "#212121",
              height: "140px",
              borderRadius: "10px",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
            className="playList"
          >
            <div>
              <b>Create your first Playlist</b>
            </div>
            <br />
            <div>
              <p>It`s easy, we will help you</p>
            </div>
            <div><button onClick={handlePlayList}>Create playlist</button></div>
          </div>
        </div>


        <div className="songs">
          {ArtistHomePage ? (
            <div className="artistsContainer">
              <UserArtistPage
                artist={selectedArtist}
                email={selectedArtist?.email || ""}
              />
            </div>
          ) : (
            <div className="artistsContainer">
              <h3 className="sectionTitle">Popular Artists</h3>
              <div className="popularArtists">
                {
                  loading
                    ? Array.from({ length: 5 }).map((_, index) => (
                      <div key={index} style={{ padding: "10px" }}>
                        <Skeleton className="Artist-skeleton" circle={true} height={150} width={150} />
                        <Skeleton className="Artist-skeleton" height={20} width={150} style={{ marginTop: 5 }} />
                      </div>))
                    :
                    topArtists
                      .map((artist) => (
                        <div key={artist.id} className="artistItem">
                          <img
                            src={`${VITE_ARTIST_IMAGE}/${artist.id}`}
                            alt={artist.name}
                            className="artistImage"
                            onClick={() => handleArtistSelect(artist)}
                            loading="lazy"
                          />
                          <div className="artistName">{artist.name}</div>
                        </div>
                      ))}
              </div>
              <h3 className="sectionTitle">Songs</h3>
              <div className="popularArtists">
                {
                  
                  songloading
                    ? Array.from({ length: 5 }).map((_, index) => (
                      <div key={index} style={{ padding: "10px" }}>
                        <Skeleton className="Artist-skeleton" circle={true} height={150} width={150} />
                        <Skeleton className="Artist-skeleton" height={20} width={150} style={{ marginTop: 5 }} />
                      </div>))
                    :
                    topSongs
                      .map((song) => (
                        <div
                          key={song.id}
                          className="artistItem"
                          onClick={() => handleSongSelect(song)}
                        >
                          <img
                            src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                            alt={song.title}
                            className="artistImage"
                            loading="lazy"
                          />
                          <div className="artistName">{song.title}</div>
                        </div>
                      ))}
              </div>
            </div>
          )}
        </div>
        <ToastContainer />
      </div>
    </>
  );
};

export default HomePage;
