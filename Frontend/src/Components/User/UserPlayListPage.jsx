import React from "react";
import "/src/CSS/UserArtistPage.css";
import QueueMusicIcon from '@mui/icons-material/QueueMusic';
import { toast } from "react-toastify";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import ShareIcon from "@mui/icons-material/Share";
import axiosInstance from "../AxiosInstance";
import PlayCircleFilledIcon from '@mui/icons-material/PlayCircleFilled';
import { IconButton } from "@mui/material";
import { toast as toastHot } from "react-hot-toast";
import { NotificationsContext } from "../../Context/NotificationsContext";
import CryptoJS from 'crypto-js'
import SharePlayListModal from "./sharePlayListModal";
import { SyncLoader } from "react-spinners";
import playlistImage from "../../assets/stock-vector-my-playlist-stylized-hand-drawn-vector-lettering-musical-disk-cartoon-clipart-grunge-background-1276279186.jpg";
const UserPlayListPage = ({ playlist, setCurrentSong, setPlayListPage, userId }) => {
  const [userPlaylist, setPlaylist] = React.useState([]);
  const [share, setShare] = React.useState(false);
  const [queueLoading, setQueueLoading] = React.useState(false);
  const { VITE_FETCH_PLAYLIST, VITE_FETCH_SONG_IMAGE, VITE_DELETE_PLAYLIST, VITE_ADD_SONGQUEUE, VITE_PLAY_PLAYLIST } = import.meta.env;
  const [deleteLoading, setDeleteLoading] = React.useState(false);
  const { setAddedToPlyList, addedToPlyList } = React.useContext(NotificationsContext);


  const handleShare = () => {
    setShare(true);
  };

  const encrypt = (text) => {
    const secretKey = 'your-secret-key';
    const encrypted = CryptoJS.AES.encrypt(text, secretKey).toString();
    return encodeURIComponent(encrypted);
  }
  const handleAddToQueue = (song) => {
    let songId = song.id;
    let songQueue = { userId, songId }
    setQueueLoading(true);
    console.log(`Adding ${song.title} to queue`);
    axiosInstance
      .post(VITE_ADD_SONGQUEUE, songQueue)
      .then((response) => {
        toast.success("Added to queue!")
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
  };


  const handlePlay = async () => {

    let id = Number(playlist?.id);
    const formData = { id, userId }

    try {
      const response = await axiosInstance.post(
        VITE_PLAY_PLAYLIST, formData
      );
      toast.success("Playing songs from " + playlist.playListName)
      console.log(response.data);
      setCurrentSong(playlist.songs[0]);

    } catch (error) {
      if (error.response && error.response.data) {
        toast.error(`${error.response.data.errorCode} \n${error.response.data.details}`);

      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }
  };

  const handleShareOutlook = () => {
    const encryptedId = encrypt(userPlaylist.id.toString());
    const subject = `Check out this playlist: ${userPlaylist?.playListName}`;
    const songDetails = userPlaylist?.songs
      .map((song) => `Title: ${song.title}`)
      .join("\n");
  
    const websiteLink = `http://localhost:5173/sharedPlaylist/${encryptedId}`;
    const body = `
      Here are the details of the playlist:\n\n
      Name: ${userPlaylist?.playListName}\n\n
      Songs:\n${songDetails}\n\n
      Enjoy listening! You can explore more playlists at the link below:\n
      ${websiteLink}
    `;
  
    window.location.href = `mailto:?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
  };
  

  const handleShareTeams = () => {
    const encryptedId = encrypt(userPlaylist.id.toString());
 
    const songDetails = userPlaylist?.songs
      .map((song) => `Title: ${song.title}`)
      .join("\n");
  
    const websiteLink = `http://localhost:5173/sharedPlaylist/${encryptedId}`;
    const message = `Check out this playlist:\n\nName: ${userPlaylist?.playListName}\n\nSongs:\n${songDetails}\n\nEnjoy listening! You can explore more playlists at: <a href="${websiteLink}">${websiteLink}</a>`;
  
    const encodedMessage = encodeURIComponent(message);
    const teamsUrl = `https://teams.microsoft.com/l/chat/0/0?users=&message=${encodedMessage}`;
  
    window.open(teamsUrl, "_blank");
  };
  

  const deletePlaylist = async (id) => {
    try {
      setDeleteLoading(true);
      const response = await axiosInstance.delete(`${VITE_DELETE_PLAYLIST}/${id}`);
      console.log(response.data);
      toast.warning("Playlist deleted successfully");
      setPlayListPage(false);
      setDeleteLoading(false);
    } catch (error) {
      toast.error("Error deleting resource:", error);
    }
  };
  const deleteSongs = async (id, song) => {
    try {
      setDeleteLoading(true)
      const response = await axiosInstance.delete(
        `${VITE_DELETE_PLAYLIST}/${userPlaylist.id}/songs/${id}`
      );
      console.log(response.data);
      const updatedPlaylist = await axiosInstance.get(
        `${VITE_FETCH_PLAYLIST}/${playlist.id}`
      );
      setPlaylist(updatedPlaylist.data);
      setDeleteLoading(false);
      toast.warning(`Removed ${song} from ${userPlaylist.playListName}`);
    } catch (error) {
      toast.error("Error deleting resource:", error);
    }
  };
  React.useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const response = await axiosInstance.get(
          `${VITE_FETCH_PLAYLIST}/${playlist.id}`
        );
        console.log(response.data);

        setPlaylist(response.data);

        setAddedToPlyList(false);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch playlists. Please try again.");
      }
    };
    fetchPlaylists();
  }, [playlist.id, addedToPlyList]);
  console.log("Playlist:", playlist);

  if (!userPlaylist || !userPlaylist.songs) {
    return <SyncLoader />;
  }

  if (userPlaylist.songs.length === 0) {
    return (
      <>
        <div style={{ color: "white" }}><h2>{userPlaylist.playListName}</h2></div>
        <div style={{ color: "white", display: "flex", flexDirection: "row", gap: "500px" }}>

          <h4><i>No songs to display</i></h4>
          <button
            type="button"
            className="btn btn-danger"
            onClick={() => deletePlaylist(userPlaylist.id)}
          >
            Delete playlist
          </button>
        </div>
      </>

    );
  }

  return (
    <>
      {share ? <div className="overlay visible" /> : ""}
      {queueLoading || deleteLoading ? <div className="overlay visible" /> : ""}
      <div className="artistsName">
        <div>
          <img
            src={playlistImage}
            alt={playlist.playListName}
          />
        </div>
        <div className="artistsDetails">
          <b>
            <h1>{userPlaylist.playListName}</h1>
          </b>
        </div>

        <OverlayTrigger
          placement="top"
          overlay={<Tooltip id="tooltip-top">AutoPlay</Tooltip>}
        >
          <div className="share" onClick={handlePlay}>
            <IconButton>
              <PlayCircleFilledIcon sx={{ color: "white" }} />
            </IconButton>
          </div>
        </OverlayTrigger>

        <OverlayTrigger
          placement="top"
          overlay={<Tooltip id="tooltip-top">Share playlist</Tooltip>}
        >
          <div className="share" onClick={handleShare}>
            <ShareIcon />
          </div>
        </OverlayTrigger>


        <div>
          <button
            type="button"
            className="btn btn-danger"
            onClick={() => deletePlaylist(userPlaylist.id)}
          >
            Delete playlist
          </button>
        </div>
      </div>
      <div style={{ color: "white" }}>
        <h3>Songs</h3>
      </div>
      <div className="songsContainer">
        {userPlaylist?.songs.map((song) => (
          <div
            key={song.id}
            className="artistSongs"
            onClick={() => {
              if (song.status === "APPROVED") {
                setCurrentSong(song);
                toastHot(`Playing ${song.title}`, {
                  icon: '🎵 🎶',
                });
              }
              else {
                toast.warning("Song is currently unavailable")
              }
            }}
          >
            <div><img
              src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
              alt={song.title}
              className="artistsImage"
            /></div>

            <h2>{song.title}</h2>
            <OverlayTrigger
              placement="top"
              overlay={
                <Tooltip id="tooltip-top">Add to queue</Tooltip>
              }
            >
              <div className="queue-container-playlist" onClick={(e) => {
                e.stopPropagation();
                handleAddToQueue(song)
              }}> <QueueMusicIcon className="queue" /></div>
            </OverlayTrigger>

            <OverlayTrigger
              placement="top"
              overlay={
                <Tooltip id="tooltip-top">Remove song from playlist</Tooltip>
              }
            >
              <div
                className="deleteSong"
                onClick={(e) => {
                  e.stopPropagation();
                  deleteSongs(song.id, song.title)
                }}
              >
                <RemoveCircleOutlineIcon />
              </div>
            </OverlayTrigger>
          </div>
        ))}
      </div>
      {share ? (
        <div className="shareDialogbox">
          <SharePlayListModal
            handleClose={setShare}
            playlist={userPlaylist}
            handleShareOutlook={handleShareOutlook}
            handleShareTeams={handleShareTeams}
          />
        </div>
      ) : (
        ""
      )}

    </>
  );
};

export default UserPlayListPage;
