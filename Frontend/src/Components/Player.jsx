import React, { useRef, useEffect, useState } from "react";
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import SkipPreviousIcon from "@mui/icons-material/SkipPrevious";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import PauseIcon from "@mui/icons-material/Pause";
import SkipNextIcon from "@mui/icons-material/SkipNext";
import { toast } from "react-toastify";
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import VolumeUpIcon from "@mui/icons-material/VolumeUp";
import VolumeOffIcon from "@mui/icons-material/VolumeOff";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import Button from "@mui/material/Button";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import axiosInstance from "../Components/AxiosInstance";
import "../CSS/Player.css";
import RepeatIcon from '@mui/icons-material/Repeat';
import Dropdown from 'react-bootstrap/Dropdown';
import ShuffleIcon from '@mui/icons-material/Shuffle';
import { NotificationsContext } from "../Context/NotificationsContext";
import Overlay from "./Overlay";



const Player = ({ song, setlyrics, lyricsVisible, setAddToPlayList, user, songSelect, setReorderQueue }) => {
  const theme = useTheme();
  const audioRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [artistName, setArtistName] = useState("");
  const [sliderValue, setSliderValue] = useState(0);
  const [isSeeking, setIsSeeking] = useState(false);
  const [repeat, setRepeat] = useState(false);
  const [timeoutId, setTimeoutId] = useState(null);
  const [listening, setListening] = useState(false);

  const { VITE_ADD_SONGS_TO_LISTENING_HISTORY, VITE_PREVIOUS_SONG_QUEUE, VITE_NEXT_SONG_QUEUE, VITE_GET_ARTIST_NAME, VITE_SONG_PLAYCOUNT, VITE_CLEAR_SONGQUEUE, VITE_FETCH_SONG_IMAGE, VITE_SONG_STREAM, VITE_SHUFFLE_SONGQUEUE, VITE_AUTOSLEEP } = import.meta.env;

  const { setAddedToPlyList, setSongAddedToPlayList, transcript, resetTranscript } = React.useContext(NotificationsContext);
  const [volume, setVolume] = useState(100); // Volume state (0-100)
  const [isMuted, setIsMuted] = useState(false); // Mute state
  const [loading, setLoading] = useState(false);

  // Initialize volume and mute state when song changes
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = 1; 
      setVolume(100);
      setIsMuted(false);
    }
  }, [song]);

//increase volume
  const increaseVolume = () => {
    const newVolume = Math.min(100, volume + 30); // Increase by 30, maximum 100
    setVolume(newVolume);
    if (audioRef.current) {
      audioRef.current.volume = newVolume / 100;
      if (isMuted && newVolume > 0) {
        setIsMuted(false); // Unmute if volume is increased from 0
      }
    }
  };

  //voice assistant 
  useEffect(() => {

    if (timeoutId) {
      clearTimeout(timeoutId);
    }

    const newTimeoutId = setTimeout(() => {
      const lowerCaseTranscript = transcript.toLowerCase();
      console.log(transcript.toLowerCase());

      if (lowerCaseTranscript.includes("hey melodify") || lowerCaseTranscript.includes("hey") || lowerCaseTranscript.includes("hey mel") || lowerCaseTranscript.includes("melody") || lowerCaseTranscript.includes("35") || lowerCaseTranscript.includes("hey melody") || lowerCaseTranscript.includes("melodify") || lowerCaseTranscript.includes("5")) {
        setListening(true);
        if (lowerCaseTranscript.includes("next")) {
          console.log("Next song");
          handleNextSong();
          resetTranscript();
        }
        else if (lowerCaseTranscript.includes("previous")) {
          console.log("Previous song");
          handlePreviousSong();
          resetTranscript();
        }
        else if (lowerCaseTranscript.includes("increase volume") || lowerCaseTranscript.includes("increase")) {
          console.log("increase volume");
          increaseVolume()
          resetTranscript();
        }
        else if (lowerCaseTranscript.includes("decrease volume") || lowerCaseTranscript.includes("decrease") || lowerCaseTranscript.includes("reduce")) {
          console.log("decrease volume");
          decreaseVolume();
          resetTranscript();
        }
        else if (lowerCaseTranscript.includes("mute") || lowerCaseTranscript.includes("unmute")) {
          console.log("mute");
          toggleMute();
          resetTranscript();
        }
        else if ((lowerCaseTranscript.includes("play") && !lowerCaseTranscript.includes("next")) || lowerCaseTranscript.includes("start")) {
          console.log("Playing");

          let songName = lowerCaseTranscript
            .replace(/(play|hey|5|melody|melodify|song|track|\.|,)/g, "")
            .replace(/\s\s+/g, ' ')
            .trim();
          console.log(songName);
          if (songName) {
            handleVoiceSearch(songName);
      
          } else {
            console.log("No song name provided");
          }

          setIsPlaying(true);

          resetTranscript();
        }
        else if (lowerCaseTranscript.includes("pause") || lowerCaseTranscript.includes("stop")) {
          console.log("Paused");
          setIsPlaying(false);
          resetTranscript();
        }
        else {
          resetTranscript();
        }
      }
      else {
        resetTranscript();
      }

      setListening(false)
    }, 1500);

    setTimeoutId(newTimeoutId);

    return () => clearTimeout(newTimeoutId);

  }, [transcript]);

  //voice search
  const handleVoiceSearch = async (term) => {
    try {
      const response = await axiosInstance.get(`http://localhost:8080/artist/songs/voiceSearch/${term}`);
      console.log(response.data);
      songSelect(response.data.body);

    } catch (error) {

      if (window.speechSynthesis) {
        const utterance = new SpeechSynthesisUtterance("Song is not available");
        window.speechSynthesis.speak(utterance);

      } else {
        console.error("Search failed:", error);
        toast.error("Search failed. Please try again.");
      }
    }
  }

//decrease volume
  const decreaseVolume = () => {
    const newVolume = Math.max(0, volume - 30); // Decrease by 30, minimum 0
    setVolume(newVolume);
    if (audioRef.current) {
      audioRef.current.volume = newVolume / 100;
      if (isMuted && newVolume > 0) {
        setIsMuted(false); // Unmute if volume is increased from 0
      }
    }
  };

  //Toggle mute
  const toggleMute = () => {
    if (audioRef.current) {
      if (isMuted) {
        audioRef.current.volume = volume / 100;
      } else {
        audioRef.current.volume = 0;
      }
      setIsMuted(!isMuted);
    }
  };
  // When the song changes, update the audio element source and load the new file.
  useEffect(() => {
    if (song && audioRef.current) {
      audioRef.current.src = `${VITE_SONG_STREAM}/${song.id}`;
      audioRef.current.load();
      setCurrentTime(0);
      setDuration(0);
      setSliderValue(0);
      setIsPlaying(true);
      if (isPlaying) {
        audioRef.current.play().catch((err) => {
          console.error("Error playing audio:", err);
          setIsPlaying(false);
        });
      }
    }
  }, [song]);


  // When isPlaying changes, toggle play/pause without reloading the audio.
  useEffect(() => {
    if (audioRef.current) {
      if (isPlaying) {
        updateSongs();
        audioRef.current.play().catch((err) => {
          console.error("Error playing audio:", err);
          setIsPlaying(false);
        });
      } else {
        audioRef.current.pause();
      }
    }
  }, [isPlaying]);

  // clear song queue
  const handleClearQueue = async () => {
    try {
      setLoading(true);
      const response = await axiosInstance.delete(`${VITE_CLEAR_SONGQUEUE}${user.id}`);
      console.log(response.data);
      setLoading(false);
      toast.warning("Cleared queue");

    } catch (error) {
      toast.error("Error deleting resource:", error);
    }
  }

  // Update play count and add song to listening history 
  const updateSongs = async () => {
    const formData = new FormData();
    formData.append("id", Number(song.id));
    try {
      await axiosInstance.put(VITE_SONG_PLAYCOUNT, formData);
    } catch (error) {
      if (error.response && error.response.data) {
        toast.error(
          `${error.response.data.errorCode}\n${error.response.data.details}`
        );
      } else {
        console.log("Something went wrong updating play count");
      }
    }
  };

  // Fetch the artist's name 
  useEffect(() => {
    const fetchArtistName = async () => {
      if (song) {
        try {
          const response = await axiosInstance.get(`${VITE_GET_ARTIST_NAME}/${song.artistId}`);
          setArtistName(response.data);
        } catch (error) {
          console.error("Error fetching artist name:", error);
          if (error.response && error.response.data) {
            toast.error(
              `${error.response.data.errorCode}\n${error.response.data.details}`
            );
          } else {
            toast.error("Something went wrong. Please try again.");
          }
        }
      }
    };
    fetchArtistName();
  }, [song]);

  // Toggle play/pause 
  const togglePlayPause = () => {
    if (!audioRef.current) return;
    setIsPlaying((prev) => !prev);
  };

  // Update currentTime and duration during playback.
  const handleTimeUpdate = () => {
    if (audioRef.current && !isSeeking) {
      const current = audioRef.current.currentTime;
      const dur = audioRef.current.duration;
      setCurrentTime(current);
      setDuration(dur);
      setSliderValue((current / dur) * 100);
    }
  };


  const handleNextSong = async () => {
    // Save listening history for the current song
    if (song && user) {
      const userId = user.id;
      const songId = song.id;
      const durationListened = audioRef.current.currentTime; // Duration listened
      const history = { userId, songId, duration: durationListened };
      setLoading(true);
      try {
        await axiosInstance.post(VITE_ADD_SONGS_TO_LISTENING_HISTORY, history);
        console.log("Listening history updated for song:", song.title);
      
      } catch (error) {
        console.error("Error updating listening history:", error);
      }
    }

    console.log(repeat);



    // Fetch and play the next song
    try {
   
      const response = await axiosInstance.get(`${VITE_NEXT_SONG_QUEUE}/${user.id}`);
      if (response.status === 200) {
        setDuration(0);
        songSelect(response.data);
        setIsPlaying(true); // Automatically play the next song
      }
      setLoading(false);
    } catch (error) {
      console.error("Error fetching next song:", error);
      toast.warning(
        `${error.response.data.errorCode}\n${error.response.data.message}`
      );
      setIsPlaying(false);
      setLoading(false);
    }
  };


  const handlePreviousSong = async () => {
    // Save listening history for the current song
    if (song && user) {
      const userId = user.id;
      const songId = song.id;
      const durationListened = audioRef.current.currentTime; // Duration listened
      const history = { userId, songId, duration: durationListened };
      setLoading(true);
      try {
        await axiosInstance.post(VITE_ADD_SONGS_TO_LISTENING_HISTORY, history);
        console.log("Listening history updated for song:", song.title);
      } catch (error) {
        console.error("Error updating listening history:", error);
      }
    }

    // Fetch and play the previous song
    try {
 
      const response = await axiosInstance.get(`${VITE_PREVIOUS_SONG_QUEUE}/${user.id}`);
      if (response.status === 200) {
        setDuration(0);
        songSelect(response.data);
        setIsPlaying(true); // Automatically play the previous song
      }
      setLoading(false);
    } catch (error) {
      console.error("Error fetching previous song:", error);

      toast.warning(
        `${error.response.data.errorCode}\n${error.response.data.message}`
      );
      setIsPlaying(false); // Pause if no previous song  
      setLoading(false);

    }
  };

  //shuffle queue 
  const handleShuffle = async () => {

    try {
      setLoading(true);
      const response = await axiosInstance.get(`${VITE_SHUFFLE_SONGQUEUE}/${user.id}`);
      console.log(response.data)
      toast.success("Shuffled your queue")
      setLoading(false);
    } catch (error) {
      console.error("Error shuffling:", error);
      if (error.response && error.response.data) {
        toast.warning(
          `${error.response.data.errorCode}\n${error.response.data.details}`
        );
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }

  };



  useEffect(() => {
    const audioElement = audioRef.current;

    if (audioElement) {
      audioElement.addEventListener("ended", handleSongEnd);
    }

    return () => {
      if (audioElement) {
        audioElement.removeEventListener("ended", handleSongEnd);
      }
    };
  }, [song, repeat]);

  const handleSongEnd = async () => {
    console.log("Song ended, going to the next song...");

    // Save listening history for the current song
    if (song && user) {
      const userId = user.id;
      const songId = song.id;
      const durationListened = audioRef.current.currentTime; // Duration listened
      const history = { userId, songId, duration: durationListened };

      try {
        await axiosInstance.post(VITE_ADD_SONGS_TO_LISTENING_HISTORY, history);
        console.log("Listening history updated for song:", song.title);
      } catch (error) {
        console.error("Error updating listening history:", error);
      }
    }

    // Check if repeat is enabled
    if (repeat) {
      audioRef.current.currentTime = 0; 
      audioRef.current.play(); 
      console.log("Song is on repeat");
      return; 
    }

    // Move to the next song if repeat is not enabled
    handleNextSong();
  };


  // repeat
  const handleRepeat = () => {
    setRepeat(prev => !prev); 
    toast.success(`${!repeat ? "Repeat ON" : "Repeat OFF"}`);
  };
  // When the user drags the seek bar, update the slider value.
  const handleSliderChange = (event) => {
    setSliderValue(event.target.value);
  };

  // When user releases the seek bar (onMouseUp), update the audio's currentTime.
  const handleSeek = () => {
    if (audioRef.current && duration) {
      const seekTime = (sliderValue / 100) * duration;
      audioRef.current.currentTime = seekTime;
      setCurrentTime(seekTime);
    }
    setIsSeeking(false);
  };

  // When user starts dragging the seek bar.
  const handleSeekMouseDown = () => {
    setIsSeeking(true);
  };

  // Toggle lyrics display.
  const handleLyricsToggle = () => {
    setlyrics((prev) => !prev);
  };

  // Format time in mm:ss.
  const formatTime = (time) => {
    if (isNaN(time)) return "0:00";
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  const [sleep, setSleep] = useState(false);
  useEffect(() => {

    setIsPlaying(false);
    setSleep(false);

  }, [sleep]);

  //Autosleep
  const handleAutoSleep = async (sleepduration) => {
    try {

      toast.info("Autosleep started");

      const response = await axiosInstance.get(`${VITE_AUTOSLEEP}/${sleepduration}`);
      console.log(response.data.body)
      setSleep(true);
      toast.info("Autosleep completed")

    } catch (error) {
      console.error("Error autosleep:", error);
      if (error.response && error.response.data) {
        toast.warning(
          `${error.response.data.errorCode}\n${error.response.data.details}`
        );
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }
  }
  return (

    <Card sx={{
      className: "compact-player",
      display: "flex", backgroundColor: "#121212", color: "white", justifyContent: "center", alignItems: "center",
    }}>
      {loading && <Overlay show={true} />}
      {listening && <Overlay show={true} />}
      {song ? (
        <Box sx={{
          className: "compact-content",
          display: "flex", flexDirection: "row", alignItems: "center", width: "100%",
        }}>
          <Box sx={{
            display: "flex", flexDirection: "column", flex: 1, padding: 1, textAlign: "left"
          }}>
            <CardContent sx={{ padding: 0 }}>
              <Typography component="div" variant="h5" sx={{ color: "white" }}>
                {song.title}
              </Typography>
              <Typography variant="subtitle1" color="white" component="div">
                {artistName}
              </Typography>
              <Typography variant="subtitle1" color="white" component="div">
                {song.playCount} Plays
              </Typography>
            </CardContent>

            <Box sx={{ display: "flex", alignItems: "center", justifyContent: "center", gap: 1, className: "compact-controls" }}>

              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">

                  {repeat ? "Disable repeat" : "Enable repeat"}

                </Tooltip>}
              >

                <IconButton sx={{ color: repeat ? "#1DB954" : "gray" }} onClick={handleRepeat}>
                  <RepeatIcon />
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  Previous
                </Tooltip>}
              >
                <IconButton aria-label="previous" sx={{ color: "#1DB954" }} onClick={() => handlePreviousSong()}>
                  {theme.direction === "rtl" ? <SkipNextIcon /> : <SkipPreviousIcon />}
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  {isPlaying ? "pause" : "play"}
                </Tooltip>}
              >
                <IconButton aria-label={isPlaying ? "pause" : "play"} sx={{ color: "#1DB954" }} onClick={togglePlayPause}>
                  {isPlaying ? <PauseIcon sx={{ height: 38, width: 38 }} /> : <PlayArrowIcon sx={{ height: 38, width: 38 }} />}
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  Next
                </Tooltip>}
              >
                <IconButton aria-label="next" sx={{ color: "#1DB954" }} onClick={() => handleNextSong()}>
                  {theme.direction === "rtl" ? <SkipPreviousIcon /> : <SkipNextIcon />}
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  Shuffle
                </Tooltip>}
              >
                <IconButton sx={{ color: "#1DB954" }} onClick={() => handleShuffle()}>
                  <ShuffleIcon />
                </IconButton>
              </OverlayTrigger>

            </Box>
            <Box sx={{ width: "100%", mt: 1 }}>

              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  Decrease volume
                </Tooltip>}
              >
                <IconButton onClick={decreaseVolume} sx={{ color: "#1DB954" }}>
                  <RemoveCircleOutlineIcon />
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  {isMuted ? 'Unmute' : 'mute'}
                </Tooltip>}
              >
                <IconButton onClick={toggleMute} sx={{ color: '#1DB954' }}>
                  {isMuted ? <VolumeOffIcon /> : <VolumeUpIcon />}
                </IconButton>
              </OverlayTrigger>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip id="tooltip-top">
                  Increase volume
                </Tooltip>}
              >
                <IconButton onClick={increaseVolume} sx={{ color: "#1DB954" }}>
                  <AddCircleOutlineIcon />
                </IconButton>
              </OverlayTrigger>
              <input
                type="range"
                value={sliderValue}
                onChange={handleSliderChange}
                onMouseDown={handleSeekMouseDown}
                onMouseUp={handleSeek}
                className="compact-slider"
                min="0"
                max="100"
                step="0.1"
                style={{ width: "100%" }}
              />

              <Typography variant="body2" color="#1DB954" align="center">
                {formatTime(currentTime)} / {formatTime(duration)}
              </Typography>
            </Box>
            <Box sx={{ display: "flex", gap: 2 }}>
              <Button
                variant="contained"
                sx={{ backgroundColor: "#1DB954", color: "black", fontWeight: "bold", "&:hover": { backgroundColor: "#169c45" } }}
                onClick={handleLyricsToggle}
              >
                {lyricsVisible ? "Hide Lyrics" : "Show Lyrics"}
              </Button>

              <Button
                variant="contained"
                sx={{ backgroundColor: "#FF6347", color: "black", fontWeight: "bold", "&:hover": { backgroundColor: "#cc4c38" }, }}
                onClick={handleClearQueue}
              >
                Clear queue
              </Button>
              <Button
                variant="contained"
                sx={{
                  backgroundColor: "#D4AF37",
                  color: "black",
                  fontWeight: "bold",
                  "&:hover": { backgroundColor: "#C58D29" },
                }}
                onClick={() => setReorderQueue(true)}
              >
                Reorder queue
              </Button>
              <Dropdown>
                <Dropdown.Toggle variant="secondary" id="dropdown-basic" style={{ color: "black", fontFamily: "system-ui", fontWeight: "bold" }}>
                  AUTOSLEEP
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item onClick={() => handleAutoSleep(1)}>1 minute</Dropdown.Item>
                  <Dropdown.Item onClick={() => handleAutoSleep(5)}>5 minutes</Dropdown.Item>
                  <Dropdown.Item onClick={() => handleAutoSleep(10)}>10 minutes</Dropdown.Item>
                  <Dropdown.Item onClick={() => handleAutoSleep(10)}>15 minutes</Dropdown.Item>
                  <Dropdown.Item onClick={() => handleAutoSleep(10)}>30 minutes</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </Box>
          </Box>
          <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: "50px" }}>
            <OverlayTrigger placement="top" overlay={<Tooltip id="tooltip-top">Add to playlist</Tooltip>}>
              <IconButton sx={{ height: "10px", color: "white" }}>
                <PlaylistAddIcon onClick={() => {
                  setAddToPlayList(true)
                  setAddedToPlyList(true);
                  setSongAddedToPlayList(song);
                }} />
              </IconButton>

            </OverlayTrigger>
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", width: "150px", height: "150px" }}>
              <CardMedia
                component="img"
                sx={{ width: 151, height: 151, padding: "5px", objectFit: "contain" }}
                image={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                alt={song.title}
              />

            </Box>

          </div>

          <audio
            ref={audioRef}
            controls
            crossOrigin="anonymous"
            style={{ display: "none" }}
            onTimeUpdate={handleTimeUpdate}
            onLoadedMetadata={() => {
              setDuration(audioRef.current.duration)
              setSliderValue(0)
            }}
          >
            <source
              src={`${VITE_SONG_STREAM}/${song.id}`}
              type="audio/mpeg"
            />
          </audio>
        </Box>
      ) : (
        <Typography variant="h6" sx={{ m: 2 }}>
          Select a song to play
        </Typography>
      )}

    </Card>


  );
};

export default Player;

