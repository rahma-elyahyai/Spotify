import { useState } from "react";
import axios from 'axios';
import "../CSS/SignUp.css";
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from "react-router-dom";
import UserAuthenticationNavbar from "../Components/User/UserAuthenticationNavbar";

const SignUp = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [next, setNext] = useState(true);
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [next1, setNext1] = useState(true);
  const [backendError, setBackendError] = useState();
  const [nameError, setNameError] = useState("");
  const [locationError, setLocationError] = useState("");
  const { VITE_USER_SIGN_UP } = import.meta.env;

  const navigate = useNavigate();

  const handleEmail = (e) => {
    const value = e.target.value;
    if (/\s/.test(value)) {
      setEmailError("Email cannot contain spaces");
    } else {
      setEmailError("");
    }
    setEmail(value);
  };


  const handlePassword = (e) => {
    setPassword(e.target.value.trim()); 
  };

  const handleName = (e) => {
    const value = e.target.value;
    if (/^\s|\s$/.test(value)) {
      setNameError("Name cannot start or end with a space");
    } else if (/\s{2,}/.test(value)) {
      setNameError("Name cannot contain multiple consecutive spaces");
    } else {
      setNameError("");
    }
    setName(value);
  };


  const handleLocation = (e) => {
    const value = e.target.value;
    if (/^\s|\s$/.test(value)) {
      setLocationError("Location cannot start or end with a space");
    } else {
      setLocationError("");
    }
    setLocation(value);
  };


  const handleNext = (e) => {
    e.preventDefault();
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(!emailPattern.test(email) )
    {
      setEmailError("Enter a valid email");
    }
    else if (email.includes(" ")) {
      setEmailError("Enter a valid email without spaces");
    } else {
      setEmailError("");
      setNext(false);
    }
  };

  const handleNext1 = (e) => {
    e.preventDefault();
    if (password.length < 10) {
      setPasswordError("Password must contain at least 10 characters");
    } else {
      setPasswordError("");
      setNext1(false);
    }
  };

  const handleSubmitLastStep = (e) => {
    e.preventDefault();

    if (nameError || locationError || !name.trim() || !location.trim()) {
      if (!name.trim()) setNameError("Name cannot be empty");
      if (!location.trim()) setLocationError("Location cannot be empty");
      return;
    }

    const userData = { email, password, name, location };

    axios.post(VITE_USER_SIGN_UP, userData)
      .then((response) => {
        console.log(response);
        navigate("/userhome", { state: { email, name } });
      })
      .catch((error) => {
        console.error("Error posting user data:", error);
        if (error.response && error.response.data) {
          toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
          setBackendError(error.response.data.message);
        } else {
          toast.error("Something went wrong. Please try again.");
        }
      });
  };

  return (
    <>
      <div style={{ position: "fixed", top: 0, width: "100%", backgroundColor: "rgb(11, 11, 11)", height: "70px" }}>
        <UserAuthenticationNavbar />
      </div>

      {next ? (
        <div className="signup-container">
          <h2 className="signup-title">Sign up to start listening</h2>
          <form className="signup-form">
            <label>Email Address</label>
            <input type="email" placeholder="name@domain.com" value={email} onChange={handleEmail} maxLength="50" />
            {emailError && <span style={{ color: "red" }}>{emailError}</span>}
            <button type="submit" onClick={handleNext}>Next</button>
          </form>
        </div>
      ) : next1 ? (
        <div className="signup-container">
          <h5 className="signup-title">Create a password</h5>
          <form className="signup-form">
            <label>Password</label>
            <input type="password" value={password} onChange={handlePassword} maxLength="50" />
            {passwordError && <span style={{ color: "red" }}>{passwordError}</span>}
            <button type="submit" onClick={handleNext1}>Next</button>
          </form>
        </div>
      ) : (
        <div className="signup-container">
          <h3 className="signup-title">Tell us about yourself</h3>
          <form className="signup-form">
            <label>Name</label>
            <p>This name will appear on your profile</p>
            <input type="text" value={name} onChange={handleName} required maxLength="50" />
            {nameError && <span style={{ color: "red" }}>{nameError}</span>}

            <label>Location</label>
            <input type="text" value={location} onChange={handleLocation} maxLength="50" />
            {locationError && <span style={{ color: "red" }}>{locationError}</span>}

            <p>We will recommend songs based on your location</p>
            {backendError && <p className="error-message">{backendError}</p>}
            <button type="submit" onClick={handleSubmitLastStep}>Next</button>
            <ToastContainer />
          </form>
        </div>
      )}
    </>
  );
};

export default SignUp;
