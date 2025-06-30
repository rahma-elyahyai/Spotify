import React from 'react'
import { ToastContainer } from 'react-toastify';
import { toast } from 'react-toastify/unstyled';
import axiosInstance from '../AxiosInstance';

const ArtistAlbum = ({ artist, setArtistHome, setAlbum }) => {
  const [formData, setFormData] = React.useState({
    title: "",
    releaseDate: "",
    coverImage: null,
    artistId: "",
  });

  const [backendError, setBackendError] = React.useState("");
  const [error, setError] = React.useState(false)
  const { VITE_ARTIST_ALBUM_POST } = import.meta.env

  const handleTitle = (e) => {
    setFormData((prev) => ({ ...prev, title: e.target.value }));
  };


  const handleReleaseDate = (e) => {
    setFormData((prev) => ({ ...prev, releaseDate: e.target.value }));
  };

    const handleCoverImage = (e) => {
      const file = e.target.files[0];
      if (file) {
        const fileExtension = file.name.split('.').pop().toLowerCase();
        const allowedExtensions = ['jpg', 'jpeg'];
  
        if (!allowedExtensions.includes(fileExtension)) {
          toast.error("Cover image must be a JPG or JPEG file.");
          return;
        }
        setFormData((prev) => ({ ...prev, coverImage: file }));
      }
    };

  const handleSubmitLastStep = async (e) => {
    e.preventDefault();

    const form = new FormData();
    form.append("title", formData.title);
    form.append("releaseDate", formData.releaseDate);
    form.append("coverImage", formData.coverImage);
    form.append("artistId", artist.id);

    try {
      const response = await axiosInstance.post(VITE_ARTIST_ALBUM_POST, form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      toast.success('Successful');
      console.log(response);
      setTimeout(() => {
        setArtistHome(true);
        setAlbum(false);
        setError(false);
      }, 1000);


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

  return (
    <div className="signup-container">
      <h3 className="signup-title">Create new Album</h3>
      <form className="signup-form">
        <label>Title</label>
        <input type="text" name="title" value={formData.title} onChange={handleTitle} />

        <label>Release Date</label>
        <input type="date" name="releaseDate" value={formData.releaseDate} onChange={handleReleaseDate} />

        <label>Cover Image</label>
        <input type="file" name="coverImage" accept=".jpg" onChange={handleCoverImage} />
        {formData.coverImage && (
          <div className="profile-picture-preview">
            <img
              src={URL.createObjectURL(formData.coverImage)}
              alt="Cover Preview"
              width="100"
              height="100"
            />
          </div>
        )}

        <button type="submit" onClick={handleSubmitLastStep}>
          Submit
        </button>
        <ToastContainer />
        {error ? <span>{backendError}</span> : ""}
      </form>
    </div>
  );
}

export default ArtistAlbum