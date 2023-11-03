package no.hvl.dat250.voting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);

    public void log(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.error(message);
    }

    public void logDebug(String message) {
        logger.debug(message);
    }

    public void logWarn(String message) {
        logger.warn(message);
    }
}
