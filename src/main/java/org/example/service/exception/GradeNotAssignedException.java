package org.example.service.exception;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Grade;

@NoArgsConstructor
public class GradeNotAssignedException extends Exception {

    private static final Logger logger = LogManager.getLogger(GradeNotAssignedException.class);

    public GradeNotAssignedException(Grade grade, long student) {
        super(String.format("Grade (%s) couldn't be assigned to student (%d) in service", grade, student));
        logger.error(this);
    }

}
