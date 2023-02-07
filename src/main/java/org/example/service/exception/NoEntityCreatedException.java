package org.example.service.exception;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.EntityType;

@NoArgsConstructor
public class NoEntityCreatedException extends Exception {

    private static final Logger logger = LogManager.getLogger(NoEntityCreatedException.class);

    public NoEntityCreatedException(EntityType entityType) {
        super(String.format("No %s was added to the service as some of fields have either incorrect value or are null",
                entityType.getDisplayName()));
        logger.error(this);
    }

    public NoEntityCreatedException(EntityType entityType, Object entity) {
        super(String.format("No %s (%s) was added to the service as some of fields have either incorrect value or are null",
                entityType.getDisplayName(), entity));
        logger.error(this);
    }

}