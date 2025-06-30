import { useState } from 'react'
import "../CSS/Admin.css"
import AdminLogin from '../Components/Admin/AdminLogin'
import AdminSignUp from '../Components/Admin/AdminSignUp'
import AdminAuthenticationNavbar from '../Components/Admin/AdminAuthenticationNavbar'
import AdminHome from '../Components/Admin/AdminHome'
import AdminAnalytics from '../Components/Admin/AdminAnalytics'

const Admin = () => {

  const [login, setLogin] = useState(false);
  const [signup, setSignup] = useState(false);
  const [admin, setAdmin] = useState(true);
  const [loggedIn, setLoggedIn] = useState(false);
  const [adminPage, setAdminHomePage] = useState(true);
  const [analyicsPage, setAnalyicsPage] = useState(false);
  const [adminDetails, setAdminDetails] = useState(null);
  const handleLogin = () => {
    setLogin(true);
    setSignup(false);
    setAdmin(false);
  }

  const handleSignUp = () => {
    setSignup(true);
    setLogin(false);
    setAdmin(false);
  }

  return (
    <>
      <div
        style={{
          position: "fixed",
          top: 0,
          width: "100%",
          backgroundColor: "rgb(11, 11, 11)",
          height: "70px",
          zIndex: "1000"
        }}
      >
        <AdminAuthenticationNavbar setLogin={setLogin} setSignup={setSignup} admin={loggedIn} setAdminHomePage={setAdminHomePage} setAnalyicsPage={setAnalyicsPage} />
      </div>
      {(admin) ? <div className='admin-card'>
        <h1>Admin</h1>
        <form className='admin-form'>
          <button onClick={handleLogin} >Login</button>
          <button onClick={handleSignUp}>Sign Up</button>
        </form>
      </div> : ("")}
      {(login) ? <AdminLogin setAdminHomePage={setLoggedIn} setLogin={setLogin} setAdminDetails={setAdminDetails} /> : ("")}
      {(signup) ? <AdminSignUp setSignup={setSignup} setLogin={setLogin} /> : ("")}
      {(adminPage && loggedIn) ? <AdminHome admin={adminDetails} /> : ("")}
      {(analyicsPage) ? <AdminAnalytics admin={adminDetails} /> : ("")}
      { }
    </>

  )
}

export default Admin