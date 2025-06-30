import  { useRef, useState, useEffect } from "react";
import { MdPlayArrow, MdPause } from "react-icons/md";
import "../CSS/AudioPlayer.css";

const AudioPlayer = ({ audioSrc, ImgSrc }) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const audioRef = useRef(null);
  const canvasRef = useRef(null);
  const animationRef = useRef(null);
  const audioCtxRef = useRef(null);
  const analyserRef = useRef(null);

  const handleSeek = (e) => {
    audioRef.current.currentTime = e.target.value;
    setCurrentTime(e.target.value);
  };

  const handlePlay = () => {
    audioRef.current.play();
    setIsPlaying(true);
    visualizeAudio();
  };

  const handlePause = () => {
    audioRef.current.pause();
    setIsPlaying(false);
    cancelAnimationFrame(animationRef.current);
  };

  const handlePlayPause = () => {
    if (isPlaying) {
      handlePause();
    } else {
      handlePlay();
    }
  };

  const formatDuration = (time) => {
    if (isNaN(time)) return "0:00";
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  const handleTimeUpdate = () => {
    setCurrentTime(audioRef.current.currentTime);
    setDuration(audioRef.current.duration);
  };

  const visualizeAudio = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");
    const audio = audioRef.current;

    if (!audioCtxRef.current) {
      audioCtxRef.current = new (window.AudioContext || window.webkitAudioContext)();
      analyserRef.current = audioCtxRef.current.createAnalyser();
      const source = audioCtxRef.current.createMediaElementSource(audio);
      source.connect(analyserRef.current);
      analyserRef.current.connect(audioCtxRef.current.destination);
      analyserRef.current.fftSize = 256;
    }

    const analyser = analyserRef.current;
    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    const draw = () => {
      animationRef.current = requestAnimationFrame(draw);
      analyser.getByteFrequencyData(dataArray);

      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "rgba(0, 0, 0, 0.1)";
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      const barWidth = (canvas.width / bufferLength) * 2.5;
      let x = 0;

      for (let i = 0; i < bufferLength; i++) {
        const barHeight = dataArray[i] / 2;
        ctx.fillStyle = `rgb(29, ${barHeight + 50}, 84)`;
        ctx.fillRect(x, canvas.height - barHeight, barWidth, barHeight);
        x += barWidth + 2;
      }
    };

    draw();
  };

  useEffect(() => {
    const audioElement = audioRef.current;

    if (audioSrc && audioElement) {
      audioElement.src = audioSrc;
      audioElement.load(); 
      setCurrentTime(0);
      setDuration(0);
      handlePlay();
      audioElement.addEventListener("timeupdate", handleTimeUpdate);
      if (isPlaying) {
        audioRef.current.play().catch((err) => {
          console.error("Error playing audio:", err);
          setIsPlaying(false);
        });
      }
    }
  }, [audioSrc]);

  return (
    <div className="player-card">
      <img src={ImgSrc} alt="Album Cover" />
      <canvas ref={canvasRef} width="300" height="100"></canvas>

      <input
        type="range"
        min="0"
        max={duration}
        value={currentTime}
        onChange={handleSeek}
      />
      <audio ref={audioRef} crossOrigin="anonymous">
        <source src={audioSrc} type="audio/mp3" />
      </audio>

      <div className="track-duration">
        <p>{formatDuration(currentTime)}</p>
        <p>{formatDuration(duration)}</p>
      </div>

      <button onClick={handlePlayPause}>
        {isPlaying ? <MdPause /> : <MdPlayArrow />}
      </button>
    </div>
  );
};

export default AudioPlayer;
