import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import axiosInstance from "../AxiosInstance";
import { toast } from "react-toastify";

const EditSongModal = ({ song, fetchData}) => {
    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const [loading,setLoading] = useState(false);
    const [formData, setFormData] = useState({
        title: "",
        genre: "",
        language: "",
        lyrics: "",
        audioFile: null,
        coverImage: null,
    });

    useEffect(() => {
        if (song) {
            setFormData({
                title: song.title || "",
                genre: song.genre || "",
                language: song.language || "",
                lyrics: song.lyrics || "",
                audioFile: null,
                coverImage:  null,
            });
        }
    }, [song]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!song?.id) {
            toast.error("Invalid song data");
            return;
        }

        try {
            setLoading(true);
            const formDataObj = new FormData();
            formDataObj.append("title", formData.title);
            formDataObj.append("genre", formData.genre);
            formDataObj.append("language", formData.language);
            formDataObj.append("lyrics", formData.lyrics);
            if (formData.audioFile) formDataObj.append("mp3File", formData.audioFile);
            if (formData.coverImage) formDataObj.append("coverImage", formData.coverImage);

            await axiosInstance.put(`/artist/songs/editSong/${song.id}`, formDataObj, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            toast.success("Song updated successfully!", {
                autoClose: 3000,
              });
            setLoading(false);
            fetchData();
            handleClose();
        } catch (error) {
            console.error("Error posting data:", error);
            setLoading(false);
            if (error.response && error.response.data) {
                toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
            } else {
                toast.error("Something went wrong. Please try again.");
            }
        }
    };

    return (
        <>
          
            <div>
            {loading ? <div className="overlay-modal visible" /> : ""}
            <Button variant="warning" onClick={handleOpen}>Edit</Button>
          
            <Modal show={open} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Song</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formTitle">
                            <Form.Label>Title</Form.Label>
                            <Form.Control type="text" name="title" value={formData.title} onChange={handleChange} required />
                        </Form.Group>

                        <Form.Group controlId="formGenre">
                            <Form.Label>Genre</Form.Label>
                            <Form.Control type="text" name="genre" value={formData.genre} onChange={handleChange} required />
                        </Form.Group>

                        <Form.Group controlId="formLanguage">
                            <Form.Label>Language</Form.Label>
                            <Form.Control type="text" name="language" value={formData.language} onChange={handleChange} required />
                        </Form.Group>

                        <Form.Group controlId="formLyrics">
                            <Form.Label>Lyrics</Form.Label>
                            <Form.Control as="textarea" rows={3} name="lyrics" value={formData.lyrics} onChange={handleChange} />
                        </Form.Group>

                        <Form.Group controlId="formAudioFile">
                            <Form.Label>Audio File</Form.Label>
                            <Form.Control type="file" name="audioFile" accept="audio/*" onChange={handleFileChange} />
                        </Form.Group>

                        <Form.Group controlId="formCoverImage">
                            <Form.Label>Cover Image</Form.Label>
                            <Form.Control type="file" name="coverImage" accept="image/*" onChange={handleFileChange} />
                        </Form.Group>

                        <br />
                        <Button variant="primary" type="submit">Save Changes</Button>
                    </Form>
                </Modal.Body>
            </Modal>
        </div>
        </>
       
    );
};

export default EditSongModal;
