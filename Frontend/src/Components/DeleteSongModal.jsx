import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import axiosInstance from './AxiosInstance';
import { toast, ToastContainer } from 'react-toastify';

const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 400,
    bgcolor: "#333",
    color: "white",
    borderRadius: "10px",
    boxShadow: 24,
    p: 4,
    textAlign: "center",
};
const DeleteSongModal = ({ songId, songName, fetchData}) => {
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const [loading, setLoading] = React.useState(false);
    const handleDelete = async () => {
        try {
        setLoading(true);
          await axiosInstance.delete(`/artist/songs/${songId}`);
          toast.success(`Song "${songName}" deleted successfully!`);
          fetchData();
          setLoading(false);
        } catch (error) {
          toast.error("Failed to delete the song. Try again.");
          console.error(error);
        }
        handleClose();
    };

    return (
        <div>
          {loading ? <div className="overlay visible" /> : ""}
            <Button onClick={handleOpen}>Delete</Button>
            <Modal open={open} onClose={handleClose}>

                <Box sx={style}>
                    <Typography id="delete-song-modal-title" variant="h6">
                        Delete Song
                    </Typography>
                    <Typography sx={{ mt: 2 }}>
                        Are you sure you want to delete {songName}?
                    </Typography>
                    <Box sx={{ mt: 3, display: "flex", justifyContent: "space-around" }}>
                        <Button
                            variant="contained"
                            style={{ backgroundColor: '#8b1414', color: 'white' }}
                            onClick={handleDelete}
                        >
                            Yes, Delete
                        </Button>
                        <Button
                            variant="contained"
                            style={{ backgroundColor: ' #319b56', color: 'white' }}
                            onClick={handleClose}
                        >
                            Cancel
                        </Button>

                    </Box>
                </Box>
            </Modal>
            <ToastContainer/>
        </div>
    );
}

export default DeleteSongModal;