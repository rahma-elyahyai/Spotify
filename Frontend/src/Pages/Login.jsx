import { useEffect, useState } from "react";
import axios from 'axios';
import "../CSS/Login.css";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';
import UserAuthenticationNavbar from "../Components/User/UserAuthenticationNavbar";


const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { VITE_USER_LOGIN } = import.meta.env;
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    localStorage.clear();
  }, [])

  const handleEmail = (e) => {
    setEmail(e.target.value);
  };

  const handlePassword = (e) => {
    setPassword(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const userData = { email, password };
    setLoading(true);
    axios.post(VITE_USER_LOGIN, userData)
      .then((response) => {
        console.log(response.data);
        const token = response.data.token;
        localStorage.setItem("token", token);
        const { name, email } = response.data.user;
        console.log(name, email);
        setLoading(false);
        navigate("/userhome", { state: { email, name } });
        toast.success("Login successful!");
      })
      .catch((error) => {
        console.error(error);
        if (error.response && error.response.data) {
          toast.error(`${error.response.data.errorCode} \n${error.response.data.details}`);
          setError(error.response.data.message);
          setLoading(false);
        } else {
          setError("Something went wrong. Please try again.");
          toast.error("Something went wrong. Please try again.");
        }
      });
  };

  return (
    <div>
      {loading ? (
        <div className="overlay visible">
          <video autoPlay loop muted>
            <source
              src="src/assets/music-loading-animated-icon-download-in-lottie-json-gif-static-svg-file-formats--audio-sound-instrument-pack-services-icons-7989956.mp4"
              type="video/mp4"
            />
          </video>
        </div>
      ) : ""}

      <div
        style={{
          position: "fixed",
          top: 0,
          width: "100%",
          backgroundColor: "rgb(11, 11, 11)",
          height: "70px",
        }}
      >
        <UserAuthenticationNavbar />
      </div>

      <div className="login-container">
        <h3 className="login-title">Log in to Melodify</h3>
        <form className="login-form">
          <label>Email</label>
          <input
            type="email"
            placeholder="Enter your email"
            value={email}
            maxLength="50"
            onChange={handleEmail}
          />
          <label>Password</label>
          <input
            type="password"
            placeholder="Enter your password"
            value={password}
            maxLength="50"
            onChange={handlePassword}
          />

          {error && <span>{error}</span>}

          <button
            type="submit"
            onClick={handleSubmit}
          >
            Login
          </button>

          <ToastContainer />
        </form>
      </div>
    </div>
  );
};

export default Login;
