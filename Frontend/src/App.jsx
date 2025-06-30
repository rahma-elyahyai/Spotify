import 'regenerator-runtime/runtime';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import Home from "./Pages/Home";
import UserHome from "./Pages/UserHome";
import Admin from "./Pages/Admin";
import Artists from "./Pages/Artists";
import ProtectedRoute from "./Components/ProtectedRoute";
import { NotificationContextProvider } from "./Context/NotificationsContext";
import SharedPlayListPage from "./Components/SharedPlayListPage";
import Login from './Pages/Login';
import SignUp from './Pages/SignUp';


function App() {


  return (
    <div
      style={{
        backgroundColor: "rgb(33,33,33)",
        minHeight: "100vh",
        height: "auto",
      }}
    >
      <div>
        <BrowserRouter>

          <Routes>
            <Route path="/login" element={<Login/>} />
            <Route path="/" element={
              <NotificationContextProvider><Home /></NotificationContextProvider>
              } />
            <Route path="/signup" element={<SignUp/>} />
            <Route path="/artists" element={<Artists />} />


            <Route
              path="/userhome"
              element={
                <ProtectedRoute>
                  <NotificationContextProvider>
                  <UserHome />
                  </NotificationContextProvider>

                </ProtectedRoute>
              }
            />
            <Route path="/admin" element={<Admin />} />
         <Route path="/sharedPlaylist/:playListId" element={<SharedPlayListPage/>}></Route>
      
          </Routes>
        </BrowserRouter>
      </div>
    </div>
  );
}

export default App;
