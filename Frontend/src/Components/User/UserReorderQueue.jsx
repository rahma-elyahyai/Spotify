import  { useEffect, useState } from 'react';
import { Reorder } from 'framer-motion';
import { toast } from 'react-toastify';
import axiosInstance from '../AxiosInstance';
import "../../CSS/ReorderSongQueue.css"
import CloseIcon from '@mui/icons-material/Close';
import { ClipLoader } from 'react-spinners';
import DeleteIcon from '@mui/icons-material/Delete';
import { IconButton } from '@mui/material';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import Overlay from '../Overlay';

const UserReorderQueue = ({ user, reoderQueue, setReorderQueue }) => {
    const [items, setItems] = useState([]);
    const [queue, setQueue] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deleteLoading, setDeleteLoading] = useState(false);
    const { VITE_GET_SONGQUEUE, VITE_REORDER_SONGQUEUE, VITE_FETCH_SONG_IMAGE, VITE_DELETE_SONGQUEUE } = import.meta.env;
    const [saveLoading, setSaveLoading] = useState(false);

    useEffect(() => {
        const fetchSongQueue = async () => {
            try {
                const response = await axiosInstance.get(
                    `${VITE_GET_SONGQUEUE}/${user.id}`
                );
                const fetchedItems = response.data;


                const sortedItems = fetchedItems.sort((a, b) => a.currentPosition - b.currentPosition);

                setLoading(false);
                setItems(fetchedItems);
                setQueue(sortedItems);
                console.log("Fetched Items: ", fetchedItems);
                console.log("Sorted Queue: ", sortedItems);
            } catch (error) {
                console.log(error);
                toast.error("Failed to fetch song queue. Please try again.");
            }
        };
        fetchSongQueue();
    }, [user, reoderQueue]);

    const handleDelete = async (e, id, song) => {
        e.stopPropagation()
        console.log(id)
        try {
            setDeleteLoading(true);
            const response = await axiosInstance.delete(`${VITE_DELETE_SONGQUEUE}/${id}`);
            console.log(response.data);
            const revisedSongs = await axiosInstance.get(
                `${VITE_GET_SONGQUEUE}/${user.id}`
            );
            setQueue(revisedSongs.data);
            toast.warning(`${song} removed from queue successfully`);
            setDeleteLoading(false);
        } catch (error) {
            toast.error("Error deleting resource:", error);
        }
    }

    const handleReorder = (newOrder) => {
        setQueue(newOrder);
    };

    const handleSave = async () => {
        try {
            setSaveLoading(true);
            await axiosInstance.put(
                `${VITE_REORDER_SONGQUEUE}/${user.id}`,
                queue
            );
            toast.success("Queue reordered successfully!");
            setSaveLoading(false);
            setReorderQueue(false);
        } catch (error) {
            console.log(error);
            setLoading(false);
            toast.error("Failed to save the reordered queue.");
        }
    };

    return (

        <>
            {/* {saveLoading && <Overlay show={true} />} */}
            {deleteLoading || saveLoading ? <div className="overlay visible" /> : ""}
            <Reorder.Group values={queue} onReorder={handleReorder}>
                <div className="Song-queues">
                    <CloseIcon className="close" onClick={() => setReorderQueue(false)} />

                    {loading ? (
                        <div style={{ color: "white", textAlign: "center", marginTop: "20px" }}>
                            <h4>Loading queue</h4>
                            <ClipLoader color="white" size={50} />
                        </div>
                    ) : queue.length === 0 ? (
                        <h3>
                            <i style={{ color: "white" }}>Queue is empty</i>
                        </h3>
                    ) : (
                        <>
                            <h3 style={{ color: "white" }}>
                                <i>Current queue</i>
                            </h3>
                            <p style={{ color: "grey" }}>
                                <i>Drag to reorder songs in your queue</i>
                            </p>

                            {queue.map((item) => (
                                <Reorder.Item key={item.songId} value={item}>
                                    <div className="song-item">
                                        <h5>{item.songTitle}</h5>
                                        <div>
                                            <img
                                                src={`${VITE_FETCH_SONG_IMAGE}/${item.songId}`}
                                                alt="song"
                                                style={{
                                                    height: "50px",
                                                    width: "50px",
                                                    objectFit: "contain",
                                                    borderRadius: "50%",
                                                }}
                                            />
                                        </div>
                                        <OverlayTrigger
                                            placement="top"
                                            overlay={<Tooltip id="tooltip-top">Remove from queue</Tooltip>}
                                        >
                                            <IconButton sx={{ color: "white" }} onClick={(e) => handleDelete(e, item.id, item.songTitle)}>
                                                <DeleteIcon />
                                            </IconButton>
                                        </OverlayTrigger>


                                    </div>
                                </Reorder.Item>
                            ))}
                        </>
                    )}

                    <button onClick={handleSave}>Save</button>
                </div>
            </Reorder.Group>

        </>
    );
};

export default UserReorderQueue;
