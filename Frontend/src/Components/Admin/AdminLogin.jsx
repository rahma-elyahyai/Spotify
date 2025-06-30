import { useState } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import Overlay from '../Overlay';

const AdminLogin = ({setAdminHomePage,setLogin,setAdminDetails}) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading,setLoading] = useState(false);
  const{VITE_ADMIN_LOGIN} = import.meta.env;
  const handleEmail = (e) => setEmail(e.target.value);
  const handlePassword = (e) => setPassword(e.target.value);
  const userData = { email, password };

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    axios
      .post(VITE_ADMIN_LOGIN, userData)
      .then((response) => {
        console.log(response.data.body.admin.body);
        const token = response.data.body.token;
        localStorage.setItem("token", token);
        setLoading(false);
        setLogin(false);
        setAdminDetails(response.data.body.admin.body);
        setAdminHomePage(true);
        toast.success("Login successful!");

      })
      .catch((error) => {
        console.log(error.response.data.errorResponse);
        if (error.response && error.response.data) {
          toast.error(`${error.response.data.errorResponse.errorCode}\n${error.response.data.errorResponse.details}`);
          setError(error.response.data.errorResponse.message);
          setLoading(false)
        } else {
          setError("Something went wrong. Please try again.");
          toast.error("Something went wrong. Please try again.");
        }
      });
  };
  return (
    <>
    {loading && <Overlay show={true}/>}
      <div className="login-container">
        <h3 className="login-title">Admin Log in to Melodify</h3>
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
            maxLength="20"
            onChange={handlePassword}
          />

          {error && <span>{error}</span>}
          <button
            type="submit"
            onClick={(e) => {
              handleSubmit(e);
            }}
          >
            Login
          </button>
          <ToastContainer />
        </form>
      </div>
      </>
  );
};

export default AdminLogin;
