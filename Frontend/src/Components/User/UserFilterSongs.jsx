import React from 'react'
import { NotificationsContext } from '../../Context/NotificationsContext';
import axiosInstance from '../AxiosInstance';
import "../../CSS/UserHome.css"
import { toast } from 'react-toastify';
import { ClipLoader } from 'react-spinners';
import "../../CSS/UserFilter.css";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import QueueMusicIcon from '@mui/icons-material/QueueMusic';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { toast as toastHot } from "react-hot-toast";

const UserFilterSongs = ({ setCurrentSong, userId, setAddToPlayList, }) => {
  const { selectedFilters, songs, setsongs, setSongAddedToPlayList } = React.useContext(NotificationsContext);
  const { VITE_GET_GENRES, VITE_GET_LANGUAGES, VITE_FETCH_SONG_IMAGE, VITE_GET_FILTER, VITE_ADD_SONGQUEUE } = import.meta.env;
  const [loading, setLoading] = React.useState(true);
  const [data, setData] = React.useState([]);
  const [category, setCategory] = React.useState("");
  const [fetchedSongs, setFetchedSongs] = React.useState([]);


  React.useEffect(() => {

    if (selectedFilters === "genre") {
      const fetchGenres = async () => {
        try {
          setLoading(true);
          const response = await axiosInstance.get(VITE_GET_GENRES);
          console.log(response.data.body);
          setData(response.data.body);
          setLoading(false);

        } catch (error) {
          console.error(error);
          toast.error("Failed to fetch genres. Please try again.");
          setLoading(true);

        }
      };
      fetchGenres();
    }
    else if (selectedFilters === "language") {
      const fetchLanguange = async () => {
        try {
          setLoading(true);
          const response = await axiosInstance.get(VITE_GET_LANGUAGES);
          console.log(response.data.body);
          setData(response.data.body);
          setLoading(false);

        } catch (error) {
          console.error(error)
          toast.error("Failed to fetch genres. Please try again.");
          setLoading(false);

        }
      };
      fetchLanguange();
    }

  }, [selectedFilters])

  const handleSelect = (song) => {
    if (!localStorage.getItem("token")) {
      console.log("song select")
      toast.info("Sign up to start listening for free on Melodify");
    }
    setCurrentSong(song);
  };

  const handleAddToQueue = (song) => {
    let songId = song.id;
    let songQueue = { userId, songId }

    if (!localStorage.getItem("token")) {
      toast.info("Sign up to start listening for free on Melodify");
    }
    else {

      console.log(`Adding ${song.title} to queue`);
      axiosInstance
        .post(VITE_ADD_SONGQUEUE, songQueue)
        .then((response) => {
          toast.success("Added to queue!");

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

  const handleFilter = (filter) => {
    setsongs(false);


    console.log(filter)
    setCategory(filter)
    const params = new URLSearchParams();
    if (selectedFilters === "genre") params.append("genre", filter);
    else params.append("language", filter);

    const fetchSongs = async () => {
      try {
        setLoading(true);

        const response = await axiosInstance.get(`${VITE_GET_FILTER}?${params.toString()}`);
        setFetchedSongs(response.data.body);
        setLoading(false);
        setsongs(true);

      } catch (error) {
        console.error(error)
        toast.error("Failed to fetch songs. Please try again.");
        setLoading(false);

      }
    };
    fetchSongs();

  }

  return (

    <div className='filter-options'>

      {
        (fetchedSongs.length > 0 && songs) ? (<div className="songs-container">

          <div>
            <h3> <i>Songs from {category}</i></h3>
          </div>

          {
            fetchedSongs
              .filter((song) => song.status === "APPROVED")
              .map((song) => (
                <div
                  key={song.id}
                  className="filter-Songs"
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
                    className="artists-Image"
                  /></div>

                  <h5>{song.title}</h5>
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
                    <MoreVertIcon onClick={(e) => {
                      e.stopPropagation();
                      setAddToPlayList(true);
                      setSongAddedToPlayList(song);
                    }} />
                  </OverlayTrigger>
                </div>
              ))}
        </div>) : (
          loading ? (
            <div className="spinner-container-filter">
              <ClipLoader color="blue" size={50} />
            </div>
          ) : (
            data
            .filter(s=>s!=null)
            .map((filter, index) => (
              <div
                key={index}
                className="filter-cards"
                onClick={() => handleFilter(filter)}
              >
                {filter}
              </div>
            ))
          )
        )
      }

    </div>


  )
}

export default UserFilterSongs