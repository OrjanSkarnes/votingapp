import { useState, useEffect } from 'react';

export const useCountdown = (endTime) => {
    const [timeLeft, setTimeLeft] = useState("");

    useEffect(() => {
        const intervalId = setInterval(() => {
            const now = new Date();
            const end = new Date(endTime);
            const diff = end - now;

            if (diff > 0) {
                const days = Math.floor(diff / (1000 * 60 * 60 * 24));
                const hours = Math.floor(diff / (1000 * 60 * 60));
                const minutes = Math.floor((diff / (1000 * 60)) % 60);
                const seconds = Math.floor((diff / 1000) % 60);

                let timeLeftString = "";
                if (days !== 0) {
                    timeLeftString += `${days} ${days > 1 ? "days" : "day"} `;
                }
                timeLeftString += `${hours}h ${minutes}m ${seconds}s`;
                setTimeLeft(timeLeftString);
            } else {
                setTimeLeft("Time ended");
                clearInterval(intervalId);
            }
        }, 1000);

        return () => clearInterval(intervalId); // cleanup on component unmount
    }, [endTime]);

    return timeLeft;
}