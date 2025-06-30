import Slider from "react-slick";
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css"; 
import video1 from '../assets/6274273-uhd_3840_2160_30fps.mp4'; 
import video2 from "../assets/8513599-uhd_3840_2160_25fps.mp4";
import video3 from "../assets/8514058-uhd_3840_2160_25fps.mp4";

const VideoCarousel = ({login,signup}) => {
  const settings = {
    dots: true,
    infinite: true,
    speed: 300, 
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    arrows: false,
    fade: true, 
  };

  const videos = [video2, video3, video1];

  return (

    
        (login || signup)?(""):(
            <div className="video-carousel" style={{marginTop:"20px"}}>
              <Slider {...settings}>
                {videos.map((video, index) => (
                  <div key={index}>
                    <video controls autoPlay loop muted>
                      <source src={video} type="video/mp4" />
                      Your browser does not support the video tag.
                    </video>
                    <div className="video-text">
                      <h1>Where your music </h1>
                      <h1>is everything</h1>
                      <br/>
                      <b>
                      <h5>Develop a fanbase,build your</h5>
                      <h5>business, and create</h5>
                      <h5>the world around your music.</h5>
                      </b>
                      <br/>
                      <br/>
                      <div style={{display:"flex",flexDirection:"row",gap:"100px"}}>   
                      <h2>Amplify your music</h2>
                      <h2>Connect with fans</h2>
                      <h2>Grow your business</h2>
                      <h2>Understand your audience</h2>
                      </div>
                    </div>
                  
                  </div>
                ))}
              </Slider>
            </div>)
    
  );
};

export default VideoCarousel;
