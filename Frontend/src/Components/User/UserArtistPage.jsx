import React from "react";
import "../../CSS/UserArtistPage.css";
import { toast } from "react-toastify";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import axiosInstance from "../AxiosInstance";
import QueueMusicIcon from '@mui/icons-material/QueueMusic';
import { NotificationsContext } from "../../Context/NotificationsContext";
import { toast as toastHot } from "react-hot-toast";
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';

const UserArtistPage = ({ artist, setsong, email, userId, setAddToPlayList }) => {
  const [options, setOptions] = React.useState("songs");
  const [album, setAlbum] = React.useState([]);
  const [albumPage, setAlbumPage] = React.useState(false);
  const [isFollowing, setIsFollowing] = React.useState(false);
  const [queueLoading, setQueueLoading] = React.useState(false);
  const { VITE_ADD_SONGQUEUE, VITE_ARTIST_IMAGE, VITE_ALBUM_IMAGE, VITE_FETCH_SONG_IMAGE, VITE_UNFOLLOW_ARTIST, VITE_CHECK_FOLLOWING, VITE_FOLLOW_ARTIST } = import.meta.env;
  const { setSongAddedToPlayList } = React.useContext(NotificationsContext);
  const [followLoading, setFollowLoading] = React.useState(true);

  const handleSelect = (song) => {
    if (!localStorage.getItem("token")) {
      console.log("song select")
      toast.info("Sign up to start listening for free on Melodify");
    }
    setsong(song);
  };

  const handleSongs = () => {
    setOptions("songs");
    setAlbumPage(false);
  };

  const handleAlbums = () => {
    setOptions("albums");
    setAlbumPage(false);
  };

  const handleSelectAlbum = (album) => {
    setAlbum(album);
    setAlbumPage(true);
  };

  const handleUnfollow = async () => {
    try {
      const response = await axiosInstance.delete(`${VITE_UNFOLLOW_ARTIST}/${userId}/${artist.id}`);
      console.log(response.data);
      toast.warning("Unfollowed " + artist.name);
      setIsFollowing(false);

    } catch (error) {
      toast.error("Error in unfollowing:", error);
    }
  };

  const handleAddToQueue = (song) => {
    let songId = song.id;
    let songQueue = { userId, songId }

    if (!localStorage.getItem("token")) {
      toast.info("Sign up to start listening for free on Melodify");
    }
    else {
      setQueueLoading(true);
      console.log(`Adding ${song.title} to queue`);
      axiosInstance
        .post(VITE_ADD_SONGQUEUE, songQueue)
        .then((response) => {
          toast.success("Added to queue!");
          setQueueLoading(false);
          console.log(response.data);
        })
        .catch((error) => {
          if (error.response && error.response.data) {
            toast.error(`${error.response.data.errorCode} \n${error.response.data.details}`);

          } else {

            toast.error("Something went wrong. Please try again.");
          }
        });
    }
  };
  const checkFollowing = async () => {
    if (localStorage.getItem("token")) {
      try {
        setFollowLoading(true);
        const response = await axiosInstance.get(
          `${VITE_CHECK_FOLLOWING}/${artist.id}/${email}`
        );
        console.log("Followed status:", response.data);
        setIsFollowing(response.data);
        setFollowLoading(false);
      } catch (error) {
        console.error("Error fetching followed artists:", error);
      }
    }
  };

  React.useEffect(() => {
    console.log(
      "Checking follow status for artist:",
      artist.id,
      "and email:",
      email
    );
    checkFollowing();
  }, [email, artist]);

  const handleFollow = async () => {
    try {
      setFollowLoading(true);
      const response = await axiosInstance.put(
        `${VITE_FOLLOW_ARTIST}/${email}`,
        artist,
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );
      toast.success("Following " + artist.name);
      console.log("Artist followed successfully:", response.data);
      setFollowLoading(false);
      setIsFollowing(true);
    } catch (error) {
      if (!localStorage.getItem("token")) {
        toast.info("Sign up to start listening for free on Melodify");
      }
      console.error("Error following artist:", error);
    }
  };

  return (
    <>
      {queueLoading && localStorage.getItem("token") ? <div className="overlay visible" /> : ""}
      {followLoading && localStorage.getItem("token") ? <div className="overlay visible" /> : ""}
      <div className="artistsName">
        <div>
          <img
            src={`${VITE_ARTIST_IMAGE}/${artist.id}`}
            alt={`${artist.name}`}
            style={{ height: "250px", width: "250px", borderRadius: "50%" }}
          />

          {artist.users ? <p>{artist.users.length} followers</p> : ""}
        </div>
        <div className="artistsDetails">
          <b>
            <h1>{artist.name}</h1>
          </b>
          <p>{artist.bio}</p>
          <div className="follow-artists">
            <button className="follow" onClick={handleFollow} disabled={isFollowing}>
              {isFollowing ? <i>Following</i> : "Follow"}
            </button>

            {
              isFollowing ? (
                <button className="unfollow" onClick={() => handleUnfollow()}>
                  Unfollow
                </button>
              ) : ("")
            }
          </div>


        </div>
      </div>

      <div className="options">
        <button id="songs" onClick={handleSongs}>
          Songs
        </button>
        <button id="albums" onClick={handleAlbums}>
          Albums
        </button>
      </div>

      {options === "songs" ? (
        <div className="songsContainer">
          {artist
            .songs
            .filter((song) => song.status === "APPROVED")
            .map((song) => (
              <div
                key={song.id}
                className="artistSongs"
                onClick={() => {
                  handleSelect(song);
                  toastHot(`Playing ${song.title}`, {
                    icon: '🎵 🎶',
                  });
                }}
              >
                <div> <img
                  src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                  alt={song.title}
                  className="artistsImage"
                  loading="lazy"
                /></div>


                <h2>{song.title}</h2>
                <OverlayTrigger
                  placement="top"
                  overlay={
                    <Tooltip id="tooltip-top">Add to queue</Tooltip>
                  }
                >
                  <div className="queue-container-artist" onClick={(e) => {
                    e.stopPropagation();
                    handleAddToQueue(song);
                  }}> <QueueMusicIcon className="queue" /></div>
                </OverlayTrigger>

                <OverlayTrigger placement="top" overlay={<Tooltip id="tooltip-top">Add to playlist</Tooltip>}>
                  <PlaylistAddIcon onClick={(e) => {
                    e.stopPropagation();
                    setAddToPlayList(true);
                    setSongAddedToPlayList(song);
                  }

                  } />
                </OverlayTrigger>
              </div>
            ))}
        </div>
      ) : albumPage ? (
        <div className="songsContainer">
          <div className="albumHeader">
            <div>
              <img
                src={`${VITE_ALBUM_IMAGE}/${album.id}`}
                alt={album.title}
                className="albumHeaderImage"
                loading="lazy"
              />
            </div>
            <div className="albumDetails">
              <h5>Album</h5>
              <h3>{album.title}</h3>
            </div>
          </div>
          {album.songs
            .filter((song) => song.status === "APPROVED")
            .map((song) => (
              <div
                key={song.id}
                className="artistSongs"
                onClick={() => {
                  handleSelect(song);
                  toastHot(`Playing ${song.title}`, {
                    icon: '🎵 🎶',
                  });
                }}
              >
                <div><img
                  src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                  alt={song.title}
                  className="artistsImage"
                  loading="lazy"
                /></div>

                <h2>{song.title}</h2>

                <OverlayTrigger
                  placement="top"
                  overlay={
                    <Tooltip id="tooltip-top">Add to queue</Tooltip>
                  }
                >
                  <div className="queue-container-artist" onClick={(e) => {
                    e.stopPropagation();
                    handleAddToQueue(song);
                  }}> <QueueMusicIcon className="queue" /></div>
                </OverlayTrigger>

                <OverlayTrigger placement="top" overlay={<Tooltip id="tooltip-top">Add to playlist</Tooltip>}>
                  <PlaylistAddIcon onClick={(e) => {
                    setAddToPlayList(true)
                    e.stopPropagation();
                  
                  }} />
                </OverlayTrigger>

              </div>
            ))}
        </div>
      ) : (
        <div>
          {artist.albums.map((album) => (
            <div
              key={album.id}
              className="artistSongs"
              onClick={() => handleSelectAlbum(album)}
            >
              <img
                src={`${VITE_ALBUM_IMAGE}/${album.id}`}
                alt={album.title}
                className="albumImage"
                loading="lazy"
              />
              <h2>{album.title}</h2>
            </div>
          ))}

        </div>
      )}
    </>
  );
};

export default UserArtistPage;
