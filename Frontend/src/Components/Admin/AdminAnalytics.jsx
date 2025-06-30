import React from 'react';
import Chart from 'react-apexcharts';
import axiosInstance from '../AxiosInstance';
import { toast } from 'react-toastify';
import "/src/CSS/Analytics.css";
import html2canvas from 'html2canvas';
import JSZip from 'jszip';
import FileSaver from 'file-saver';
import { ClipLoader } from 'react-spinners';

const AdminAnalytics = ({admin}) => {
  const [analytics, setAnalytics] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const {VITE_ADMIN_ANALYTICS}=import.meta.env;
  React.useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const response = await axiosInstance.get(VITE_ADMIN_ANALYTICS);
        setAnalytics(response.data.body);
        console.log(response.data);
        setLoading(false);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch analytics data.");
        setLoading(true)
      }
    };
    fetchAnalytics();
  }, [admin]);



  const downloadChartsAsZip = async () => {
    const zip = new JSZip();
    const chartElements = document.querySelectorAll('.chart-container');

    for (let i = 0; i < chartElements.length; i++) {
      const chartElement = chartElements[i];
      const canvas = await html2canvas(chartElement);
      canvas.toBlob((blob) => {
        zip.file(`chart${i + 1}.png`, blob);
      });
    }

    zip.generateAsync({ type: 'blob' }).then((content) => {
      FileSaver.saveAs(content, 'charts.zip');
    });
  };

  const charts = {
    playCount: {
      options: {
        chart: { type: 'bar' },
        title: { text: 'Most Played Songs', align: 'center' },
        xaxis: { categories: Object.keys(analytics.playCounts || {}) },
        plotOptions: { bar: { horizontal: true } },
        colors: ['#1DB954']
      },
      series: [{ data: Object.values(analytics.playCounts || {}) }]
    },

    listeningHistory: {
      options: {
        chart: { type: 'bar' },
        title: { text: 'Song Listened Duration (Minutes)', align: 'center' },
        xaxis: { categories: Object.keys(analytics.listenedDurations || {}) },
        plotOptions: { bar: { horizontal: true } },
        colors: ['#FF9F43']
      },
      series: [{ data: Object.values(analytics.listenedDurations || {}) }]
    },

    genrePopularity: {
      options: {
        chart: { type: 'bar' },
        title: { text: 'Popular Genres', align: 'center' },
        xaxis: { categories: Object.keys(analytics.genrePopularity || {}) },
        plotOptions: { bar: { horizontal: true } },
        colors: ['#FF6B6B']
      },
      series: [{ data: Object.values(analytics.genrePopularity || {}) }]
    },

    userGrowth: {
      options: {
        chart: { type: 'line' },
        title: { text: 'User Signups', align: 'center' },
        xaxis: { categories: Object.keys(analytics.userGrowth || {}) },
        stroke: { curve: 'smooth' },
        colors: ['#4ECDC4']
      },
      series: [{ name: 'Signups', data: Object.values(analytics.userGrowth || {}) }]
    },

    songVerification: {
      options: {
        chart: { type: 'donut' },
        title: { text: 'Song Verification Status', align: 'center' },
        labels: Object.keys(analytics.songVerification || {}),
        colors: ['#45B7D1', '#FF9F43']
      },
      series: Object.values(analytics.songVerification || {})
    }
  };


  return (
    <div style={{ marginTop: "70px", height: "auto" ,minHeight:"100vh"}}>
       {
    (loading) ?
      (
        <div className="spinner-container-admin">
          <ClipLoader color="white" size={50}/>
        </div>
      )
      : (<div style={{ marginTop: '70px', padding: '20px', backgroundColor: '#121212', height: "auto", color: 'white' }}>
        <h2 style={{ marginBottom: '40px', textAlign: 'center' }}>Music Analytics Dashboard</h2>

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px', justifyContent: 'center' }}>
          {Object.entries(charts).map(([key, chart]) => (
            <div key={key} style={{ backgroundColor: '#181818', borderRadius: '10px', padding: '20px', width: '550px', minHeight: '350px' }} className="chart-container">
              <Chart options={chart.options} series={chart.series} type={chart.options.chart.type} height="350" />
            </div>
          ))}
        </div>

        <button
          onClick={downloadChartsAsZip}
          className='download'
          style={{ marginTop: '20px', padding: '10px 20px', fontSize: '16px', backgroundColor: '#4ECDC4', border: 'none', borderRadius: '5px', color: 'white', cursor: 'pointer' }}
        >
          Download All Charts as Zip
        </button>
      </div>)
  }
    </div>);
}

export default AdminAnalytics;