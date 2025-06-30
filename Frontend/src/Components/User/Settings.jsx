import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../../CSS/Settings.css";
import axiosInstance from "../AxiosInstance";
import { toast } from "react-toastify";
import { OverlayTrigger, Tooltip } from "react-bootstrap";
import Overlay from "../Overlay";
import SpeechRecognition from 'react-speech-recognition';
import { ToggleSlider } from "react-toggle-slider";


const Settings = ({ setSettings, setEmail, setUserName }) => {
  const location = useLocation();
  const { email } = location.state;
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [Id, setId] = useState(0);
  const navigate = useNavigate();
  const [active, setActive] = useState(false);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    setEmail(email);
    setUserName(name);
  }, [name], [email]);

  useEffect(() => {
    if (active)
      SpeechRecognition.startListening({ continuous: true });
    else {
      SpeechRecognition.stopListening();
    }

  }, [active]);
  const { VITE_GET_USER_BY_EMAIL, VITE_UPDATE_USER, VITE_DELETE_USER } = import.meta.env;

  useEffect(
    () => {
      const getUserByEmail = async (emailId) => {
        try {
          setLoading(true);
          const response = await axiosInstance.get(VITE_GET_USER_BY_EMAIL, {
            params: { emailId: emailId },
          });
          console.log("User found:", response.data);
          setId(response.data.id);
          setName(response.data.name);
          setLoading(false);
          console.log(Id);
        } catch (error) {
          console.log(error.response.data);
          if (error.response && error.response.data.errorResponse) {
            toast.error(`${error.response.data.errorResponse.errorCode}\n${error.response.data.errorResponse.details}`);
            console.log(error.response.data.errorResponse.message);
            setLoading(false)
          } else {

            toast.error("Something went wrong. Please try again.");
          }
        };
      }

      if (email) {
        getUserByEmail(email);
      }
    },
    [email]
  );

  const handleName = (e) => {
    setName(e.target.value);
  };

  const handlePassword = (e) => {
    setPassword(e.target.value);
  };

  const userData = {
    id: Id,
    name: name,
    password: password,
  };

  const handleEdit = async (e) => {
    e.preventDefault();

    try {
      const response = await axiosInstance.put(
        `${VITE_UPDATE_USER}/${Id}`,
        userData
      );

      if (response.status === 200) {
        console.log("User updated successfully:", response.data);
        toast.success("Updated");
        setSettings(true);
        navigate("/userhome", { state: { email, name } });
      }
    } catch (error) {
      console.error("Error posting data:", error);

      if (error.response && error.response.data) {
        toast.error(
          `${error.response.data.errorCode}\n${error.response.data.details}`
        );
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }
  };

  const handleEdit2 = (e) => {
    e.preventDefault();

    if (password.length < 10) {
      toast.warning("Password should be minimum of 10 characters");
    } else {
      handleEdit(e);
    }
  };



  const handleDelete = async () => {
    try {
      const response = await axiosInstance.delete(`${VITE_DELETE_USER}/${Id}`);
      console.log(response.data);
      console.log("User deleted successfully");
      toast.warning("User deleted successfully!");
      localStorage.removeItem('token');
      navigate("/");
    } catch (error) {
      if (error.response) {
        console.error("Error deleting user:", error.response.data);
        toast.error(
          `${error.response.data.errorCode}\n ${error.response.data.details}`
        );
      } else {
        console.error("Error deleting user:", error.message);
        toast.error(`Error: ${error.message}`);
      }
    }
  };
  return (
    <>
      <div> {loading && <Overlay show={true} />}</div>
      <div className="settings-container">
        <h3 className="settings-title">Settings</h3>
        <form className="settings-form">
          <label>Name</label>
          <p>You can edit your profile name</p>
          <input
            type="text"
            value={name}
            onChange={(e) => handleName(e)}
            required
          />
          <OverlayTrigger placement="right" overlay={<Tooltip id="tooltip-right">Edit profile name</Tooltip>}>
            <button type="submit" onClick={(e) => handleEdit(e)}>
              Edit
            </button>
          </OverlayTrigger>

        </form>

        <form className="settings-form">
          <label>Password</label>
          <p>You can edit your password</p>
          <input
            type="password"
            value={password}
            onChange={(e) => handlePassword(e)}
          />
          <OverlayTrigger placement="right" overlay={<Tooltip id="tooltip-right">Edit password </Tooltip>}>
            <button type="submit" onClick={(e) => handleEdit2(e)}>
              Edit
            </button>
          </OverlayTrigger>
          <br />
          <div>
            <div style={{ display: "flex", flexDirection: "row", gap: "10px" }}>
              <label>Voice assistant</label>
              <OverlayTrigger
                placement="right"
                overlay={
                  <Tooltip id="tooltip-right">
                    The voice assistant feature allows you to control your music playback and navigate the app using simple voice commands. You can play, pause, skip tracks, and more, all hands-free.
                  </Tooltip>
                }
              >
                <img src={"https://cdn-icons-png.flaticon.com/512/4355/4355361.png"} style={{ height: "50px", width: "50px", borderRadius: "50%" }}></img>
              </OverlayTrigger>
            </div>


            <div>
              <ToggleSlider className="voice-assistant" onToggle={() => setActive(prev => !prev)} />
              <p>{`Voice assistant is ${active ? 'active' : 'inactive'}`}</p>
            </div>

          </div>

          <OverlayTrigger placement="right" overlay={<Tooltip id="tooltip-right">Delete account</Tooltip>}>
            <button className="deleteAccount" onClick={(e) => handleDelete(e)}>
              Delete Account
            </button>
          </OverlayTrigger>

        </form>

      </div>
    </>

  );
};

export default Settings;
