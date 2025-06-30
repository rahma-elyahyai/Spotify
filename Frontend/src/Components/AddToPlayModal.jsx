import React, { useState } from "react";
import Dropdown from "react-bootstrap/Dropdown";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import "../CSS/AddToPlayModal.css"
import axiosInstance from "../Components/AxiosInstance";
import CloseIcon from '@mui/icons-material/Close';
import { NotificationsContext } from "../Context/NotificationsContext";
import Overlay from "./Overlay";
const AddToPlaylistModal = ({ userData, handleClose }) => {

  const [selectedPlaylist, setSelectedPlaylist] = useState(null);
  const { VITE_ADD_SONG_TO_PLAYLIST } = import.meta.env;
  const [loading,setLoading]=useState(true);
  const { setAddedToPlyList, songAddedToPlayList } = React.useContext(NotificationsContext);

  const handlePlaylistSelect = (playlist) => {
    console.log("first", playlist);
    setSelectedPlaylist(playlist);
  };

  const handleSave = () => {
    if (selectedPlaylist) {
      console.log(selectedPlaylist.id)
      const formData = new FormData();
      formData.append("playListId", Number(selectedPlaylist?.id));
      formData.append("songId", Number(songAddedToPlayList?.id));
      console.log(formData);
      setLoading(true);
      console.log(loading)
      axiosInstance
        .post(VITE_ADD_SONG_TO_PLAYLIST, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          console.log(response.data);
          setAddedToPlyList(true);
          toast.success(`Added to playlist: ${selectedPlaylist.playListName}`);
          setLoading(false);
        })
        .catch((error) => {
          console.error(error);
          setLoading(false);
          if (error.response && error.response.data) {
            toast.warning(
              ` ${error.response.data.errorCode}\n ${error.response.data.message}`
            );
          } else {
            toast.error("Something went wrong. Please try again.");
          }
        });
      handleClose();
    } else {
      toast.error("Please select a playlist.");
    }
  };

  return (
    <>
    {loading && <Overlay show={true}/>}
      <div className="playListDialogBox">
        <Modal.Dialog>
          <div style={{ display: "flex", flexDirection: "row", gap: "160px" }}>
            <Modal.Title>Add to playlist</Modal.Title>
            <Modal.Header><CloseIcon className="close" onClick={handleClose} /></Modal.Header>
          </div>
          <br />
          <Modal.Body>
            <Dropdown>
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                {selectedPlaylist ? selectedPlaylist.playListName : "Find playlist"}
              </Dropdown.Toggle>

              <Dropdown.Menu>
                {userData.playlists.length>0 ? (
                  <div>
                    {userData.playlists.map((playlist) => (
                      <Dropdown.Item
                        key={playlist.id}
                        onClick={() => handlePlaylistSelect(playlist)}
                        className={selectedPlaylist?.id === playlist.id ? "selected" : ""}
                      >
                        {playlist.playListName}
                      </Dropdown.Item>
                    ))}
                  </div>
                ) : (
                  <Dropdown.Item>No playlists</Dropdown.Item>
                )}
              </Dropdown.Menu>
            </Dropdown>
          </Modal.Body>
          <br />

          <Modal.Footer className="d-flex justify-content-between">
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
            <Button variant="success" onClick={handleSave}>
              Save changes
            </Button>

          </Modal.Footer>
        </Modal.Dialog>

      </div>
    </>
  );
};

export default AddToPlaylistModal;
