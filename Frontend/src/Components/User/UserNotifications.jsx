import React from 'react';
import axiosInstance from '../AxiosInstance';
import "../../CSS/UserNotifications.css";
import { toast } from 'react-toastify';
import { NotificationsContext } from '../../Context/NotificationsContext';

const UserNotifications = ({ user }) => {
  const { VITE_USER_NOTIFICATIONS_GET, VITE_USER_NOTIFICATIONS_CLEAR } = import.meta.env;
  const [notifications, setNotifications] = React.useState([]);
  const [clear, setClear] = React.useState(false);
  const {setNotificationNumber} = React.useContext(NotificationsContext);
 

  React.useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await axiosInstance.get(`${VITE_USER_NOTIFICATIONS_GET}/${user.id}`);
        console.log(response.data);
        setNotifications(response.data);
        setNotificationNumber(response.data.length);
      } catch (error) {
        console.log(error);
        toast.error("Failed to fetch notifications. Please try again.");
      }
    };
    fetchNotifications();
  },[user, clear]);

  const handleClear = async () => {
    try {
      const response = await axiosInstance.delete(`${VITE_USER_NOTIFICATIONS_CLEAR}/${user.id}`);
      console.log(response.data);
      setClear(true);
      setNotificationNumber(0);
      toast.warning("Cleared notifications");
    } catch (error) {
      toast.error("Error deleting resource:", error);
    }
  };

  return (
    <div>
      {notifications.length > 0 ? (
        notifications.map((notification) => (
          <div key={notification.id} className="notification-item">
            <h5>{notification.message}</h5>
          </div>
        ))
      ) : (
        <div><i>No notifications</i></div>
      )}
      {notifications.length > 0 && (
        <button onClick={handleClear}>Clear</button>
      )}
    </div>
  );
};

export default UserNotifications;
