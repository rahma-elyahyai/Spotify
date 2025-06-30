
import React from 'react'
import { toast, ToastContainer } from 'react-toastify';
import axiosInstance from '../AxiosInstance';

const ArtistConcert = ({ artist, setArtistHome, setConcert }) => {
  const [backendError, setBackendError] = React.useState("");
  const [error, setError] = React.useState(false)
  const { VITE_ARTIST_CONCERT_POST } = import.meta.env

  const [formData, setFormData] = React.useState({
    date: "",
    location: "",
    artistId: artist.id,
  });
  const handleLocation = (e) => {
    setFormData((prev) => ({ ...prev, location: e.target.value }));
  };

  const handleDate = (e) => {
    setFormData((prev) => ({ ...prev, date: e.target.value }));
  };


  const handleSubmitLastStep = async (e) => {
    e.preventDefault();



    try {
      const response = await axiosInstance.post(VITE_ARTIST_CONCERT_POST, formData);
      console.log(response)
      toast.success('Succesful');
      setTimeout(() => {
        setArtistHome(true);
        setConcert(false);
        setError(false);
      }, 500);

    } catch (error) {
      console.error("Error posting data:", error);

      if (error.response && error.response.data) {
        setError(true);
        setBackendError(error.response.data.message);
        toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
      } else {

        toast.error("Something went wrong. Please try again.");
      }
    }

  };
  const today = new Date().toISOString().split('T')[0];
  return (
    <div className="signup-container">
      <h3 className="signup-title">New Concert</h3>
      <form className="signup-form">
        <label>Concert Date</label>
        <input type="date" name="Date" value={formData.date} onChange={handleDate} min={today} />

        <label>Location</label>
        <input type="text" name="location" value={formData.location} onChange={handleLocation} />

        <button type="submit" onClick={handleSubmitLastStep}>
          Submit
        </button>
        <ToastContainer />
        {error ? <span>{backendError}</span> : ""}
      </form>
    </div>
  )
}

export default ArtistConcert