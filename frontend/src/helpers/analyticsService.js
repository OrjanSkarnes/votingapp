import fetchWrapper from './fetchWrapper';

const trackEvent = async (eventData) => {
    try {
        // Use the fetchWrapper for the API call
        await fetchWrapper('/analytics', 'POST', eventData);

        console.log('Event tracked successfully');
    } catch (error) {
        console.error('Error tracking event:', error);
    }
};

export default {
    trackEvent,
};
