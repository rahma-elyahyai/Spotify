import { Button, Modal } from "react-bootstrap";
import { FaEnvelope, FaMicrosoft } from 'react-icons/fa'; // Import icons for Outlook and Teams
import "../../CSS/UserArtistPage.css"; // Add your own CSS if needed
import CloseIcon from '@mui/icons-material/Close';
const SharePlayListModal = ({ handleClose, playlist, handleShareOutlook, handleShareTeams }) => {
  return (
    <Modal.Dialog>
      <div style={{ display: "flex", flexDirection: "row", gap: "180px" }}>
        <Modal.Title>Share Playlist</Modal.Title>
        <Modal.Header><CloseIcon className="close" onClick={() => handleClose(false)} /></Modal.Header>
      </div>
      <br />
      <Modal.Body>
        <h5>Sharing Playlist: {playlist?.playListName}</h5>
        <p>Share this playlist with others by clicking on one of the options below.</p>

        <div className="share-icons">

          <Button
            variant="primary"
            onClick={() => handleShareOutlook(playlist)}
            style={{ marginRight: "10px" }}
          >
            <FaEnvelope style={{ marginRight: "5px" }} />
            Share via Outlook
          </Button>


          <Button
            variant="success"
            onClick={() => handleShareTeams(playlist)}
            style={{ marginRight: "10px" }}
          >
            <FaMicrosoft style={{ marginRight: "5px" }} />
            Share via Teams
          </Button>
        </div>
      </Modal.Body>
      <br />


    </Modal.Dialog>
  );
}

export default SharePlayListModal;
