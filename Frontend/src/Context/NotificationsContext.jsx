import React from 'react'
import { useSpeechRecognition } from 'react-speech-recognition';


 const NotificationsContext = React.createContext();

const NotificationContextProvider =({ children }) => {
    
  const [notification,setNotificationNumber] = React.useState(0);
  const [selectedFilters, setSelectedFilters] = React.useState("");
    const [songs,setsongs]=React.useState(false);
  const [filter,setFilter] = React.useState(false);
  const [addedToPlyList,setAddedToPlyList] = React.useState(false);
  const [songAddedToPlayList,setSongAddedToPlayList] = React.useState(null);
  const { transcript, resetTranscript } = useSpeechRecognition();
 
    return (
      <NotificationsContext.Provider value={{notification,setNotificationNumber,selectedFilters, setSelectedFilters,filter,setFilter,songs,setsongs,addedToPlyList,setAddedToPlyList,songAddedToPlayList,setSongAddedToPlayList,transcript,resetTranscript}}> 
       {children}
      </NotificationsContext.Provider>
    );
  };

export {NotificationContextProvider, NotificationsContext};