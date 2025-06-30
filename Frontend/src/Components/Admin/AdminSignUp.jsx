import { useState } from 'react'
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
const AdminSignUp = ({ setLogin, setSignup }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(false);
  const [backendError, setBackendError] = useState("");
  const [next, setNext] = useState(false);
  const [OTP, setAdminOTP] = useState("");
  const [userOTP, setAdminUserOTP] = useState("");
  const [next1, setNext1] = useState(false);
  const [name, setName] = useState("");
  const handleEmail = (e) => {
    setEmail(e.target.value);
  };
  const { VITE_ADMIN_OTP, VITE_ADMIN_POST } = import.meta.env;

  const handleFinalSubmit = (e) => {
    e.preventDefault();
    const userData = {
      name: name,
      email: email,
      password: password
    };
    axios.post(VITE_ADMIN_POST, userData)
      .then((response) => {
        setLogin(true);
        setSignup(false);
        console.log(response);

      })
      .catch((error) => {
        console.error("Error posting user data:", error);
        if (error.response && error.response.data) {
          toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
          setBackendError(error.response.data.message);
        } else {
          setBackendError("Something went wrong. Please try again.");
          toast.error("Something went wrong. Please try again.");
        }
      });
  }

  const handleName = (e) => {
    setName(e.target.value);
  }

  const handleVerifyOTP = (e) => {

    e.preventDefault();
    console.log(OTP);
    if (OTP.body == userOTP) {
      toast.success("Valid OTP!");
      setNext1(true);
      setError(false)
    }
    else {
      setError(true);
      setBackendError("Invalid OTP")
    }
  }
  const handleNext = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    formData.append("email", email);

    try {
      const response = await axios.post(VITE_ADMIN_OTP, formData);

      setAdminOTP(response.data);
      setNext(true);
      setError(false);

    }
    catch (error) {
      console.error("Error posting user data:", error);
      if (error.response && error.response.data) {
        toast.error(`${error.response.data.errorCode}\n${error.response.data.details}`);
        setError(true);
        setBackendError(error.response.data.message);

      } else {
        setBackendError("Something went wrong. Please try again.");
        toast.error("Something went wrong. Please try again.");
        setError(true);
      }
    };
  }
  return (
    <div className="signup-container">
      {(!next) ? (<form className="signup-form">
        <label>Email Address</label>
        <input
          type="email"
          placeholder="name@galaxe.com"
          value={email}
          onChange={handleEmail}
          maxLength="20"
        />
        {error ? <span>{backendError}</span> : ""}
        <button type="submit" onClick={(e) => handleNext(e)}>
          Next
        </button>
        <ToastContainer />
      </form>) :
        (
          (!next1) ? (<form className="signup-form">
            <label>Enter OTP</label>
            <p>OTP is sent to your email</p>
            <input
              type="text"
              value={userOTP}
              maxLength="4"
              onChange={(e) => setAdminUserOTP(e.target.value)}
            />
            {error ? <span>{backendError}</span> : ""}
            <button type="submit" onClick={(e) => handleVerifyOTP(e)}>Verify OTP</button>
            <ToastContainer />
          </form>) : (
            <form className="signup-form">
              <label>Name</label>
              <input
                type="text"
                value={name}
                maxLength="50"
                onChange={handleName}
              />
              <label>Password</label>
              <input
                type="password"
                value={password}
                maxLength="50"
                onChange={(e) => setPassword(e.target.value)}
              />
              {error ? <span>{backendError}</span> : ""}
              <button type="submit" onClick={(e) => handleFinalSubmit(e)}>Submit</button>
              <ToastContainer />
            </form>
          )
        )}

    </div>


  );

}

export default AdminSignUp