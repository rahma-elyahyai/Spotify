import React from "react";
import Footer from "../Components/Footer";
import "../CSS/Home.css"
import HomePage from "../Components/HomePage";

const Home = () => {
  React.useEffect(() => {
    localStorage.clear();
  })
  return (
    <div className="home-container">
      <HomePage />
      <div className="footer-container">
        <Footer />
      </div>
    </div>
  );
};

export default Home;
