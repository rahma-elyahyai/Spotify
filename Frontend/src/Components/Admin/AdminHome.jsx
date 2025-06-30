import React, { useEffect, useState } from "react";
import axiosInstance from "../AxiosInstance";
import { toast, ToastContainer } from "react-toastify";
import { SyncLoader } from "react-spinners";
import "../../CSS/AdminHome.css"
import BasicPagination from "../Pagination";



const AdminHome = ({ admin }) => {
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [audioUrl, setAudioUrl] = useState("");
  const [audioError, setAudioError] = useState("");
  const [statusFilter, setStatusFilter] = useState("PENDING");
  const audioRef = React.useRef(null);
  const [page,setPage]=useState(0);
  const [totalPages,setTotalPages] = useState();
  const { VITE_SONG_ACTION, VITE_SONG_STREAM, VITE_FETCH_SONG_IMAGE } = import.meta.env;

  const handlePlay = (id) => {
    setAudioUrl(`${VITE_SONG_STREAM}/${id}`);
    audioRef.current.load();
    audioRef.current.play();

  };
  const fetchData = async () => {
    console.log("fetch")
    try {
      console.log(statusFilter)
      const response = await axiosInstance.get(`http://localhost:8080/artist/songs/songRequests`, {
        params: { status: statusFilter, page: page, size: 5 }
      });
      console.log("fetched")
      console.log(response);
      setTotalPages(response.data.body.totalPages);
      setData(response.data.body.content);
      console.log(response.data.body)
      console.log(data)
      setLoading(false);
    } catch (error) {
      console.error("Error fetching data:", error);
      if (error.response && error.response.data) {
        toast.error(`${error.response.data.errorResponse.errorCode}\n${error.response.data.errorResponse.details}`);
        setLoading(false)
      } else {
        toast.error("Something went wrong. Please try again.");
      }
      setLoading(false);
    }
  };

  const handleApprove = async (Id) => {
    const formData = new FormData();
    formData.append("songId", Id);
    formData.append("action", "accepted");
    try {
      await axiosInstance.put(VITE_SONG_ACTION, formData);
      toast.success("Approved");
      fetchData()
    } catch (error) {
      console.error("Error approving song:", error);
      toast.error("Something went wrong. Please try again.");
    }
  };

  const handleReject = async (Id) => {
    const formData = new FormData();
    formData.append("songId", Id);
    formData.append("action", "rejected");
    console.log(formData);

    try {
      await axiosInstance.put(VITE_SONG_ACTION, formData);
      toast.error("Rejected");
      fetchData();
    } catch (error) {
      console.error("Error rejecting song:", error);
      toast.error("Something went wrong. Please try again.");

    }
  };

  useEffect(() => {
    if (admin && admin.Id) {
      fetchData();
      setPage(0)
    }
  }, [admin, statusFilter]);

  if (loading) {
    return <div style={{ marginTop: "70px", height: "100vh", color: "white", textAlign: "center" }}><h2>Loading...</h2><SyncLoader /></div>;
  }

  const filteredSongs = data.filter((song) => song.status === statusFilter) || [];

  return (
    <div style={{ marginTop: "70px", height: "auto", minHeight: "100vh" }}>
      <div style={{ color: "white", height: "auto" }} className="container">
        <br />
        <h1 className="text-center">Admin Name : {admin.name}</h1>
        <br />
        <div className="text-center">
          <label style={{ color: "white", marginRight: "10px" }}>Filter by Status:</label>
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="PENDING">PENDING</option>
            <option value="APPROVED">APPROVED</option>
            <option value="REJECTED">REJECTED</option>
          </select>
        </div>
        <br />
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flex: "1",
            marginTop: "20px",
            marginBottom: "30px",
          }}
        >
          {audioUrl ? (
            <audio
              ref={audioRef}
              crossOrigin="anonymous"
              controls
              style={{ width: "80%", Color: "black" }}
              onError={() => setAudioError("Failed to load the audio.")}
            >
              <source src={audioUrl} type="audio/mp3" />
              Your browser does not support the audio element.
            </audio>
          ) : (
            audioError && <div style={{ color: "red" }}>{audioError}</div>
          )}
        </div>
        <br />
        <table className="table table-striped table-dark">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Genre</th>
              <th>Language</th>
              <th>Release Date</th>
              <th>Status</th>
              <th>Cover Image</th>
              <th>Actions</th>
              <th>Play Song</th>
            </tr>
          </thead>
          <tbody>
            {filteredSongs.length > 0 ? filteredSongs.map((item) => (
              <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.title}</td>
                <td>{item.genre}</td>
                <td>{item.language}</td>
                <td>{item.releaseDate}</td>
                <td>{item.status}</td>
                <td>
                  <img
                    src={`${VITE_FETCH_SONG_IMAGE}/${item.id}`}
                    style={{ borderRadius: "50%", height: "100px", width: "100px" }}
                    alt={item.title}
                  />
                </td>
                <td>
                  <div >
                    {
                      item.status === "PENDING" ? (
                        <div >
                          <button className="actions-success" onClick={() => handleApprove(Number(item.id))}>Approve</button>
                          {" "}
                          <button className="actions-reject" onClick={() => handleReject(Number(item.id))}>Reject</button>
                        </div>
                      ) : (
                        item.status === "APPROVED" ? (<button className="actions-reject" onClick={() => handleReject(Number(item.id))}>Reject</button>)
                          : (<button className="actions-success" onClick={() => handleApprove(Number(item.id))}>Approve</button>)
                      )
                    }

                  </div>
                </td>
                <td>
                  <button className="play" onClick={() => handlePlay(item.id)}>
                    Play
                  </button>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="9" className="text-center">No songs found for this status</td>
              </tr>
            )}
          </tbody>
          <div className="pagination-wrapper">
          <BasicPagination page={page} setPage={setPage} fetchData={fetchData} totalPages={totalPages}/>
          </div>
        </table>
        <ToastContainer />
      </div>

    </div>
  );
};

export default AdminHome;
