import React, { useEffect } from 'react';
import SpeechRecognition from 'react-speech-recognition';
import { NotificationsContext } from '../Context/NotificationsContext';

const VoiceCommand = () => {
    const { transcript, resetTranscript } = React.useContext(NotificationsContext);

    if (!SpeechRecognition.browserSupportsSpeechRecognition()) {
        return <div>Your browser does not support speech recognition.</div>;
    }

    useEffect(() => {
        
        const timeout = setTimeout(() => {
            resetTranscript(); 
        }, 10000); 

        return () => clearTimeout(timeout);
    }, [resetTranscript]);

    return (
        <div style={{ color: 'white' }}>
            <p>{transcript}</p> 
        </div>
    );
};

export default VoiceCommand;
