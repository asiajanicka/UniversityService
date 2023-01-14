package org.example.service.exception;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.EntityType;

@NoArgsConstructor
public class EntityNotFoundException extends Exception {

    private static final Logger logger = LogManager.getLogger(EntityNotFoundException.class);

    public EntityNotFoundException(EntityType entityType, long id) {
        super(String.format("No %s with id %d found in the service", entityType.getDisplayName(), id));
        logger.error(this);
    }

    public EntityNotFoundException(EntityType entityType) {
        super(String.format("No %s found in the service", entityType.getDisplayName()));
        logger.error(this);
    }

}
