import { useState, useEffect } from 'react';
import axiosInstance from '../AxiosInstance';
import { toast } from 'react-toastify';
import ClipLoader from 'react-spinners/ClipLoader';
import Chart from 'react-apexcharts';

const UserAnalytics = ({ userId }) => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const { VITE_FETCH_USER_ANALYTICS } = import.meta.env;

    useEffect(() => {
        const fetchAnalytics = async () => {
            try {
                const response = await axiosInstance.get(`${VITE_FETCH_USER_ANALYTICS}/${userId}`);
                setData(response.data.body);

            } catch (error) {
                console.error(error);
                toast.error("Failed to fetch analytics data.");
            } finally {
                setLoading(false);
            }
        };
        fetchAnalytics();
    }, [userId]);


    const charts = {
        playCount: {
            options: {
                chart: { type: 'bar' },
                title: { text: 'Most Played Songs', align: 'center' },
                xaxis: { categories: Object.keys(data || {}) },
                plotOptions: { bar: { horizontal: true } },
                colors: ['#1DB954']
            },
            series: [{ data: Object.values(data || {}) }]
        }
    };

    return (
        <>
            <div>
                {loading ? (
                    <div style={{ textAlign: "center" }}>
                        <ClipLoader color="white" size={50} />
                    </div>
                ) : (
                    <div style={{ margin: '20px', padding: '20px', backgroundColor: '#121212', height: "auto", color: 'white' }}>
                        <h2 style={{ marginBottom: '10px', textAlign: 'center' }}>
                            Melody Metrics 📊
                        </h2>
                        <h6 style={{ marginBottom: '30px', textAlign: 'center' }}><i >Discover your most played songs and explore the rhythms that define your unique music journey</i>💫</h6>

                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px', justifyContent: 'center' }}>
                            {Object.entries(charts).map(([key, chart]) => (
                                <div key={key} style={{ backgroundColor: '#181818', borderRadius: '10px', padding: '20px', width: '550px', minHeight: '350px' }} className="chart-container">
                                    <Chart options={chart.options} series={chart.series} type={chart.options.chart.type} height="350" />
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </>
    );
};

export default UserAnalytics;
