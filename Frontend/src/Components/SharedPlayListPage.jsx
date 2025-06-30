import { useParams } from 'react-router-dom';
import UserAuthenticationNavbar from './User/UserAuthenticationNavbar'
import { useEffect, useState } from 'react';
import axios from 'axios';
import "../CSS/UserArtistPage.css";
import AddIcon from '@mui/icons-material/Add';
import playListPage from "../assets/stock-vector-my-playlist-stylized-hand-drawn-vector-lettering-musical-disk-cartoon-clipart-grunge-background-1276279186.jpg";
import { toast, ToastContainer } from 'react-toastify';
import CryptoJS from 'crypto-js'
import AudioPlayer from './AudioPlayer';
import axiosInstance from './AxiosInstance';




const SharedPlayListPage = () => {

    const { playListId } = useParams();
    const { VITE_FETCH_SHARED_PLAYLIST, VITE_FETCH_SONG_IMAGE, VITE_SONG_STREAM, VITE_SAVE_SHARED_PLAYLIST } = import.meta.env;
    const [data, setData] = useState([]);
    const [currentSong, setCurrentSong] = useState();
    const handleSong = (song) => {
        if (localStorage.getItem('token'))
            setCurrentSong(song);
        else {
            toast.warning("Log in to listen")
        }

    }
    const decrypt = (ciphertext) => {
        const secretKey = 'your-secret-key';
        const bytes = CryptoJS.AES.decrypt(ciphertext, secretKey);
        return bytes.toString(CryptoJS.enc.Utf8);
    }
    useEffect(() => {
        const fetchSharedPlayList = async () => {
            try {
                const decryptedId = decrypt(playListId);
                const response = await axios.get(`${VITE_FETCH_SHARED_PLAYLIST}/${decryptedId}`)
                setData(response.data);
                console.log(response.data);
            }
            catch (error) {
                console.error(error);
                toast.error("Failed to fetch playlist. Please try again.");
            }
        }
        fetchSharedPlayList();
    }, [])

    const handleSave = () => {
        const decryptedId = decrypt(playListId);
        const token = localStorage.getItem("token");
        console.log(token);
        axiosInstance
            .post(`${VITE_SAVE_SHARED_PLAYLIST}/${decryptedId}/${token}`)
            .then((response) => {
                console.log(response.data);
                console.log(token);

                toast.success(`${response.data.playListName} added to your playlists`)
            })
            .catch((error) => {
                console.error(error);
                if (error.response && error.response.data) {
                    toast.error(
                        ` ${error.response.data.errorCode}\n ${error.response.data.details}`
                    );
                } else {
                    toast.info("Log in to save playlist");
                }
            });
    }

    return (
        <div >
            {
                (localStorage.getItem("token")) ?
                    (<div className='banner'>

                        <h4><i>Use Melodify to share playlists for free</i></h4>
                    </div>) : (<div
                        style={{
                            position: "fixed",
                            top: 0,
                            width: "100%",
                            backgroundColor: "rgb(11, 11, 11)",
                            height: "70px",
                            zIndex: 10,
                        }}
                    ><UserAuthenticationNavbar />
                    </div>)
            }

            <div className="artistsName" style={{ marginTop: "auto" }}>
                <div>
                    <img
                        src={playListPage}

                        alt={data.playListName}
                    />
                </div>
                <div className="artistsDetails">
                    <b>
                        <h1>{data.playListName}</h1>
                    </b>
                    <h3><i>{data.userName}`s playlist</i></h3>
                </div>
                <div >
                    <button style={{ display: "flex", gap: "20px" }} onClick={() => handleSave()}>
                        <b>Save playlist</b>
                        <AddIcon />
                    </button>
                </div>
            </div>
            <div style={{ color: "white" }}>
                <h3>Songs</h3>
            </div>
            <div className="songsContainer">
                {data.songs?.map((song) => (
                    <div
                        key={song.id}
                        className="artistSongs"
                        onClick={() => handleSong(song)}
                    >
                        <div><img
                            src={`${VITE_FETCH_SONG_IMAGE}/${song.id}`}
                            alt={song.title}
                            className="artistsImage"
                        /></div>

                        <h2>{song.title}</h2>
                    </div>
                ))}
            </div>
            {currentSong ? (<AudioPlayer audioSrc={`${VITE_SONG_STREAM}/${currentSong.id}`} ImgSrc={`${VITE_FETCH_SONG_IMAGE}/${currentSong.id}`} />) : ""}

            <ToastContainer />
        </div>
    )
}

export default SharedPlayListPage