import { useState, useEffect } from 'react';

const MILLISECONDS_IN_SECOND = 1000;
const SECONDS_IN_MINUTE = 60;
const MINUTES_IN_HOUR = 60;
const HOURS_IN_DAY = 24;

export const useCountdown = (endTime) => {
    const [timeLeft, setTimeLeft] = useState("");

    useEffect(() => {
        const intervalId = setInterval(() => {
            const now = new Date();
            const end = new Date(endTime);

            if (isNaN(end)) {
                setTimeLeft("Invalid end time");
                clearInterval(intervalId);
                return;
            }

            const timeDifference = end - now;

            if (timeDifference > 0) {
                const days = Math.floor(timeDifference / (MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY));
                const hours = Math.floor((timeDifference / (MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR)) % 24);
                const minutes = Math.floor((timeDifference / (MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE)) % 60);
                const seconds = Math.floor((timeDifference / MILLISECONDS_IN_SECOND) % 60);

                const timeLeftString = `${days > 0 ? `${days} ${days > 1 ? "days" : "day"} ` : ""}${hours}h ${minutes}m ${seconds}s`;
                setTimeLeft(timeLeftString);
            } else {
                // Format to day and time
                const formattedDate = end.toLocaleDateString();
                const formattedTime = end.toLocaleTimeString();
                setTimeLeft(`Ended on ${formattedDate} at ${formattedTime}`);
                clearInterval(intervalId);
            }
        }, MILLISECONDS_IN_SECOND);

        return () => clearInterval(intervalId); // cleanup on component unmount
    }, [endTime]);

    return timeLeft;
}   