import "../CSS/Overlay.css"
import loadingVideo from "../assets/music-loading-animated-icon-download-in-lottie-json-gif-static-svg-file-formats--audio-sound-instrument-pack-services-icons-7989956.mp4";
const Overlay = ({ show, onClick }) => {
    if (!show) return null; 
  
    return (
      <div className="overlay visible" onClick={onClick}>
        <div className="overlay visible">
          <video autoPlay loop muted>
            <source
              src={loadingVideo}
              type="video/mp4"
            />
          </video>
        </div>
      </div>
    );
  };

export default Overlay

  