import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";
import "../CSS/UserHome.css";
import QueueMusicIcon from '@mui/icons-material/QueueMusic';
import axiosInstance from "../Components/AxiosInstance";
import CloseIcon from "@mui/icons-material/Close";
import { toast, ToastContainer } from "react-toastify";
import Player from "../Components/Player";
import Settings from "../Components/User/Settings";
import UserArtistPage from "../Components/User/UserArtistPage"
import UserPlayListPage from "../Components/User/UserPlayListPage";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import UserNotifications from "../Components/User/UserNotifications";
import { NotificationsContext } from "../Context/NotificationsContext";
import UserReorderQueue from "../Components/User/UserReorderQueue";
import UserFilterSongs from "../Components/User/UserFilterSongs";
import { toast as toastHot } from "react-hot-toast";
import { Toaster } from "react-hot-toast";
import Overlay from "../Components/Overlay";
import UserAnalytics from "../Components/User/UserAnalytics";
import { debounce } from "lodash";
import VoiceCommand from "../Components/VoiceCommand";
import UserNavBar from "../Components/User/UserNavBar";
import AddToPlaylistModal from "../Components/AddToPlayModal";
import playlistImage from "../assets/stock-vector-my-playlist-stylized-hand-drawn-vector-lettering-musical-disk-cartoon-clipart-grunge-background-1276279186.jpg";
import stackIcon from "../assets/stack.png";
import plusIcon from "../assets/plus.png";
import clockIcon from "../assets/clock_icon.png";

const UserHome = () => {
  const location = useLocation();
  const { email, name } = location.state || {};
  const [username, setName] = useState(name);
  const [useremail, setEmail] = useState(email);
  const [filteredSongs, setFilteredSongs] = useState([]);
  const [filteredArtists, setFilteredArtists] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentSong, setCurrentSong] = useState(null);
  const [loading, setLoading] = useState(true);
  const [songloading, setSongLoading] = useState(false);
  const [artistloading, setArtistloading] = useState(false);
  const [settings, setSettings] = useState(true);
  const [queueLoading, setQueueLoading] = useState(false);
  const [topSongs, setTopSongs] = useState([]);
  const [topArtists, setTopArtists] = useState([]);
  const [addToPlayList, setAddToPlayList] = React.useState(false);
  const [userAnalyticsPage, setUserAnlayticsPage] = useState(false);
  const { setNotificationNumber, filter, setFilter, setSelectedFilters } = React.useContext(NotificationsContext);
  const [reoderQueue, setReorderQueue] = React.useState(false);
  const [searchLoading, setSearchLoading] = useState(false);
  const {
    VITE_TOP_FIVE_SONGS_APPROVED_GET,
    VITE_CREATE_PLAYLIST,
    VITE_ARTIST_TOP_GET,
    VITE_GET_USER_BY_EMAIL,
    VITE_FETCH_LISTENING_HISTORY,
    VITE_CLEAR_LISTENING_HISTORY,
    VITE_GET_ARTIST,
    VITE_ADD_SONGQUEUE,
    VITE_ARTIST_IMAGE,
    VITE_FETCH_SONG_IMAGE,
  } = import.meta.env;
  const [ArtistHomePage, setArtistPage] = useState(false);
  const [selectedArtist, setSelectedArtist] = useState(null);
  const [createPlayList, setCreatePlayList] = useState(false);
  const [lyrics, setlyrics] = useState(false);
  const [playListName, setPlayListName] = useState("");
  const [userData, setUserData] = useState(null);
  const [userId, setUserId] = useState(0);
  const [notifications, setNotifications] = useState(false);
  const [playListPage, setPlayListPage] = useState(false);
  const [selectedPlaylist, setSelectedPlaylist] = useState(null);
  const [listeningHistory, setListeningHistory] = useState([]);
  const [history, setHistory] = useState(false);
  const [getArtist, setSingleArtist] = useState(false);


  const navigate = useNavigate();

  useEffect(() => {
    if (!location.state) {
      navigate('/');
    }
  }, [location.state, navigate]);


  const handleClose = () => {
    setCreatePlayList(false);
    setAddToPlayList(false);
    setPlayListName("");
  };

  useEffect(
    () => {

      const fetchData = async () => {
        try {
          const response = await axiosInstance.get(
            `${VITE_GET_USER_BY_EMAIL}?emailId=${useremail}`
          );
          console.log(response.data);
          setUserData(response.data);
          setUserId(response.data.id);
          setNotificationNumber(response.data.notifications.length);
        } catch (error) {
          console.error("Error posting data:", error);

          if (error.response && error.response.data) {
            toast.error(
              `${error.response.data.errorCode}\n${error.response.data.details}`
            );
          } else {
            toast.error("Something went wrong. Please try again.");
          }
        }
      };
      if (localStorage.getItem("token"))
        fetchData();
    },
    [playListName, playListPage, playListName, addToPlayList]

  );

  useEffect(() => {
    const fetchTopArtists = async () => {
      try {
        setArtistloading(true);
        const response = await axiosInstance.get(VITE_ARTIST_TOP_GET);
        setTopArtists(response.data);
        setArtistloading(false);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch artists. Please try again.");
        setLoading(false);
      }
    };
    fetchTopArtists();
  }, []);

  let playlist = { playListName, userId };
  const handlePlayListSave = () => {
    axiosInstance
      .post(VITE_CREATE_PLAYLIST, playlist)
      .then((response) => {
        console.log(response.data);
        setCreatePlayList(false);
        setPlayListName("");
        toast.success(`${playListName} added to your playlists`)
      })
      .catch((error) => {
        console.error(error);
        if (error.response && error.response.data) {
          toast.error(
            ` ${error.response.data.errorCode}\n ${error.response.data.details}`
          );
        } else {
          toast.error("Something went wrong. Please try again.");
        }
      });
  };



  useEffect(() => {
    const fetchTopSongs = async () => {
      try {
        setSongLoading(true);
        const response = await axiosInstance.get(VITE_TOP_FIVE_SONGS_APPROVED_GET);
        setTopSongs(response.data.body);
        console.log(response.data.body);
        setSongLoading(false)
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch songs. Please try again.");
        setSongLoading(false);
      }
    };
    fetchTopSongs();
  }, []);

  const handleSearch = debounce(async (term) => {
    console.log("search CALL" + term);
    if (!term) {
      setFilteredSongs([]);
      setFilteredArtists([]);
    } else if (term.length >= 3) {
      try {
        setSearchLoading(true);
        const response = await axiosInstance.get(`http://localhost:8080/artist/songs/search/${term}`);
        console.log(response.data.body);
        setFilteredSongs(response.data.body.song);
        setFilteredArtists(response.data.body.artist);
        setSearchLoading(false);
      } catch (error) {
        console.error("Search failed:", error);
        toast.error("Search failed. Please try again.");
      }
    }
  }, 500);


  const handleSongSelect = (song) => {
    if (song.status === "APPROVED") {
      setCurrentSong(song);

      toastHot(`Playing ${song.title}`, {
        icon: '🎵 🎶',
      });
    }
    else {
      toast.warning("Song is currently unavailable")
    }
  };

  const handlePlayListSelect = (playList) => {
    setPlayListPage(true);
    setSelectedPlaylist(playList);
    setArtistPage(false);
    setFilter(false);
    setSelectedFilters(false);
    setlyrics(false);
    setUserAnlayticsPage(false);

  };

  const handleClearHistory = async () => {
    try {
      const response = await axiosInstance.delete(
        `${VITE_CLEAR_LISTENING_HISTORY}/${userId}`
      );
      toast.info("History cleared");
      console.log(response.data);
      setHistory(false);
    } catch (error) {
      toast.error("Error clearing history:", error);
    }
  };

  const handleArtistSelect = (artist) => {
    const fetchArtist = async () => {
      try {
        setSingleArtist(true);
        const response = await axiosInstance.get(`${VITE_GET_ARTIST}/${artist.id}`);
        console.log(response.data)
        setSelectedArtist(response.data);
        setArtistPage(true);
        setFilter(false);
        setSelectedFilters(false);
        setSingleArtist(false);
      } catch (error) {
        console.log(error);
        toast.error("Failed to fetch songs. Please try again.");


      }
    };
    fetchArtist();


  };



  const handleAddToQueue = (song) => {
    let songId = song.id;
    let songQueue = { userId, songId }
    console.log(`Adding ${song.title} to queue`);
    setQueueLoading(true);
    axiosInstance
      .post(VITE_ADD_SONGQUEUE, songQueue)
      .then((response) => {
        setQueueLoading(false);
        toast.success("Added to queue!")
        if (currentSong == null) {
          setCurrentSong(song);
        }
        console.log(response.data);
      })
      .catch((error) => {
        if (error.response && error.response.data) {
          toast.error(`${error.response.data.errorCode} \n${error.response.data.details}`);

        } else {
          toast.error("Something went wrong. Please try again.");
        }
      });
  };
  const handlePlayList = () => {
    setCreatePlayList(true);
  };

  const handleHistory = () => {
    setHistory(true);
    setLoading(true);
    const fetchHistory = async () => {
      try {
        const response = await axiosInstance.get(
          `${VITE_FETCH_LISTENING_HISTORY}/${userId}`
        );
        setListeningHistory(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (error) {
        console.log(error);
        toast.error("Failed to fetch listening history. Please try again.");
        setLoading(false);
      }
    };
    fetchHistory();
  };
  return (
    <>
      {searchTerm && (filteredSongs.length > 0 || filteredArtists.length > 0) && (
        <div className="overlay visible" onClick={() => setSearchTerm("")} />
      )}

      {createPlayList && <Overlay show={true} />}
      {addToPlayList && <Overlay show={true} />}
      {queueLoading && <Overlay show={true} />}
      {getArtist && <Overlay show={true} />}

      {artistloading || songloading ? <div className="overlay visible" /> : ""}

      {notifications && (
        <>

          <div
            className="notifications-overlay"
            onClick={() => setNotifications(false)}
          />


          <div className="notifications">
            <div className="notification-heading"><h5>Notifications</h5>
              <CloseIcon className="close" onClick={() => setNotifications(false)} />
            </div>
            <UserNotifications user={userData} />
          </div>
        </>
      )}
      {reoderQueue && (
        <>

          <div
            className="notifications-overlay"
            onClick={() => setReorderQueue(false)}
          />


          <div className="songs-reorder">

            <UserReorderQueue user={userData} reoderQueue={reoderQueue} setReorderQueue={setReorderQueue} />
          </div>
        </>
      )}
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
        <UserNavBar
          name={username}
          email={useremail}
          setEmail={setEmail}
          setName={setName}
          onSearch={handleSearch}
          setSearchTerm={setSearchTerm}
          setSettings={setSettings}
          settings={settings}
          setArtistPage={setArtistPage}
          setPlayListPage={setPlayListPage}
          setNotifications={setNotifications}
          setlyrics={setlyrics}
          setUserAnlayticsPage={setUserAnlayticsPage}

        />
        {searchTerm && (
          <div className="searchSuggestions">
            {filteredSongs.length > 0 && (
              <div>
                {filteredSongs.map((song) => (
                  <div
                    key={song.id}
                    className="suggestionItem"
                    onClick={() => {
                      handleSongSelect(song);
                      setSearchTerm("")
                    }}
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
                    />
                  </div>
                ))}
              </div>
            )}
            {filteredArtists.length > 0 && (
              <div>
                {filteredArtists.map((artist) => (
                  <div
                    key={artist.id}
                    className="suggestionItem"
                    onClick={() => {
                      handleArtistSelect(artist);
                      setSearchTerm("")
                    }}
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
                    />
                  </div>
                ))}
              </div>
            )}
            {searchLoading || searchTerm.length < 3 ? (
              <div className="spinner-search-container">
                <Skeleton count={5} height={67} width={380} className="skeleton-item" />
              </div>
            ) : (filteredSongs.length === 0 && filteredArtists.length === 0) ? (
              <div className="home-suggestionItem">No results found</div>
            ) : null}
          </div>
        )}
      </div>
      {settings ? (
        <div className="mainContent">
          {createPlayList ? (
            <div className="playListDialogBox">
              <Modal.Dialog>
                <div
                  style={{
                    display: "flex",
                    flexDirection: "row",
                    gap: "130px",
                  }}
                >
                  <Modal.Title>Create new playlist</Modal.Title>
                  <Modal.Header>
                    <CloseIcon className="close" onClick={handleClose} />
                  </Modal.Header>
                </div>
                <br />
                <Modal.Body>
                  <input
                    type="text"
                    placeholder="Enter playlist name"
                    value={playListName}
                    onChange={(e) => setPlayListName(e.target.value)}
                  ></input>
                </Modal.Body>
                <br />

                <Modal.Footer className="d-flex justify-content-between">
                  <Button variant="secondary" onClick={handleClose}>
                    Close
                  </Button>

                  <Button variant="success" onClick={handlePlayListSave}>
                    Save changes
                  </Button>
                </Modal.Footer>
              </Modal.Dialog>
            </div>
          ) : (
            ""
          )}
          {addToPlayList ? (
            <AddToPlaylistModal
              userData={userData}
              handleClose={handleClose}
              currentSong={currentSong}

            />
          ) : (
            ""
          )}
          <div className="sidePane">
            <div className="library">
              <div>
            <img src={stackIcon} alt="library-icon" />  
              </div>
              <div>Your Library</div>
              <div>
                <OverlayTrigger
                  placement="top"
                  overlay={<Tooltip id="tooltip-top">Create new playlist</Tooltip>}
                >
                 <img
                  src={plusIcon}
                  alt="add-icon"
                  onClick={handlePlayList}
                  className="addButton"
                />
                                </OverlayTrigger>
              </div>
            </div>

            <div className="Options">
              <button onClick={() => setHistory(false)}>Playlists</button>
              <button onClick={() => handleHistory()}>History</button>
            </div>

            <div className="playlists">
              {userData && !history ? (
                <div>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "row",
                      gap: "10px",
                      color: "grey",
                    }}
                  >
                    <h6>Your playlists</h6>
                  </div>

                  {userData.playlists.map((playlistItem) => (
                    <div
                      key={playlistItem.id}
                      className="playlistOption"
                      onClick={() => handlePlayListSelect(playlistItem)}
                    >
                      {playlistItem.playListName}
                    </div>
                  ))}
                </div>
              ) : (
                ""
              )}

              {history ? (
                <div>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "row",
                      gap: "40px",
                      alignItems: "center",
                      color: "grey",
                      justifyContent: "center",
                      alignContent: "center"

                    }}
                  >
                    <div
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        alignItems: "center",
                        gap: "10px",
                        marginTop:"8px",
                        justifyContent: "center",
                      }}
                    >
                      <div>
                       <img
                        src={clockIcon}
                        alt="Clock Icon"
                        className="icon"
                      />
                      </div>
                      <h6 className="recentText">Recents</h6>
                    </div>

                    <div className="clearHistory" onClick={handleClearHistory}>
                      <h6>Clear history</h6>
                    </div>
                  </div>
                  <br />

                  {loading ? (
                    <div className="spinner-container">
                      <Skeleton count={5} height={77} width={270} className="skeleton-item" />
                    </div>
                  ) : (
                    listeningHistory?.map((song, index) => (
                      <div
                        key={index}
                        className="playlistOption"
                        onClick={() => {
                          if (song.status === "APPROVED")
                            setCurrentSong(song)
                          else {
                            toast.warning("Song is currently unavailable")
                          }
                        }}
                        style={{
                          display: "flex",
                          flexDirection: "row",
                          gap: "50px",
                        }}
                      >
                        {song.title}
                        <img
                          src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                          alt={song.title}
                          style={{
                            height: "50px",
                            width: "50px",
                            borderRadius: "50%",
                          }}
                          loading="lazy"
                        />
                      </div>
                    ))
                  )}
                </div>
              ) : (
                ""
              )}
            </div>
          </div>

          {lyrics ? (
            <div className="lyrics">{currentSong.lyrics}</div>
          ) : filter ? (
            <UserFilterSongs setCurrentSong={setCurrentSong} userId={userId} setAddToPlayList={setAddToPlayList} />
          ) : (
            <div className="songs">
              {ArtistHomePage ? (
                <div className="artistsContainer">
                  <UserArtistPage
                    artist={selectedArtist}
                    setsong={setCurrentSong}
                    email={email}
                    userId={userId}
                    setAddToPlayList={setAddToPlayList}
                  />
                </div>
              )
                : playListPage ? (
                  <div className="artistsContainer">
                    <UserPlayListPage
                      playlist={selectedPlaylist}
                      setCurrentSong={setCurrentSong}
                      setPlayListPage={setPlayListPage}
                      userId={userId}
                    />
                  </div>
                ) : userAnalyticsPage ? (
                  <div className="artistsContainer">
                    <UserAnalytics userId={userId} />
                  </div>) : (
                  <div className="artistsContainer">
                    <h3 className="sectionTitle">Artists</h3>
                    <div className="popularArtists">
                      {artistloading ? (
                        Array.from({ length: 5 }).map((_, index) => (
                          <div key={index} style={{ padding: "10px" }}>
                            <Skeleton className="Artist-skeleton" circle={true} height={150} width={150} />
                            <Skeleton className="Artist-skeleton" height={20} width={150} style={{ marginTop: 5 }} />
                          </div>
                        ))
                      ) : (
                        topArtists
                          .map((artist) => (
                            <div key={artist.id} className="artistItem">
                              <img
                                src={`${VITE_ARTIST_IMAGE}/${artist.id}`}
                                alt={artist.name}
                                className="artistImage"
                                onClick={() => handleArtistSelect(artist)
                                }
                                loading="lazy"
                              />
                              <div className="artistName">{artist.name}</div>
                            </div>
                          ))
                      )}
                    </div>

                    <h3 className="sectionTitle">Songs</h3>
                    <div className="popularArtists">
                      {songloading ? (
                        Array.from({ length: 5 }).map((_, index) => (
                          <div key={index} style={{ padding: "10px" }}>
                            <Skeleton className="Artist-skeleton" circle={true} height={150} width={150} />
                            <Skeleton className="Artist-skeleton" height={20} width={150} style={{ marginTop: 5 }} />
                          </div>
                        ))
                      ) : (
                        topSongs
                          .map((song) => (
                            <div key={song.id} className="artistItem">
                              <div className="HomeSongs">
                                <img
                                  src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                                  alt={song.title}
                                  className="artistImage"
                                  onClick={() => handleSongSelect(song)}
                                  loading="lazy"
                                />
                                <OverlayTrigger
                                  placement="top"
                                  overlay={<Tooltip id="tooltip-top">Add to queue</Tooltip>}
                                >
                                  <div
                                    className="queue-container"
                                    onClick={(e) => {
                                      e.stopPropagation();
                                      handleAddToQueue(song);
                                    }}
                                  >
                                    <QueueMusicIcon
                                      className="queue"
                                      style={{ position: "relative", top: "15%", left: "15%" }}
                                    />
                                  </div>
                                </OverlayTrigger>
                              </div>

                              <div className="artistName">{song.title}</div>
                            </div>
                          ))
                      )}
                    </div>

                    <h3 className="sectionTitle">PlayLists</h3>
                    <div className="popularArtists">
                      {userData?.playlists.map((playlist) => (
                        <div
                          key={playlist.id}
                          className="artistItem"
                          onClick={() => handlePlayListSelect(playlist)}
                        >
                          <img
                            src={playlistImage}
                            alt={playlist.playListName}
                            className="playListImage"
                          />
                          <div className="artistName">{playlist.playListName}</div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
            </div>
          )}

          <div className="playerContainer">
            <Player
              song={currentSong}
              setlyrics={setlyrics}
              lyricsVisible={lyrics}
              setAddToPlayList={setAddToPlayList}
              user={userData}
              songSelect={setCurrentSong}
              setReorderQueue={setReorderQueue}
            />
          </div>
        </div>
      ) :

        (
          <Settings
            setSettings={setSettings}
            setEmail={setEmail}
            setUserName={setName}
          />
        )


      }

      <ToastContainer />
      <Toaster />
      
<VoiceCommand/>
    </>
  );
};

export default UserHome;
