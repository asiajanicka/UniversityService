package org.example.parserTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.StudentGroup;
import org.example.service.exception.EntityNotFoundException;
import org.example.service.jdbc.StudentService;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxbTests {

    private static final Logger logger = LogManager.getLogger(JaxbTests.class);

    @Test
    public void usecaseTest() throws EntityNotFoundException, JAXBException {

        File file = new File (TestUtils.getNameForXMLFile("studentGroupJAXB"));
        StudentService studentService = new StudentService();
        StudentGroup groupFromDB = studentService.getStudentGroupById(1);
        logger.info("Retrieved student group from DB");
        JAXBContext context = JAXBContext.newInstance(StudentGroup.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(groupFromDB, file);
        logger.info(String.format("Marshalled student group to file '%s'", file));

        Unmarshaller unmarshaller = context.createUnmarshaller();
        StudentGroup groupFromXML = (StudentGroup) unmarshaller.unmarshal(file);
        logger.info(String.format("Unmarshalled student group from file '%s' to object", file));
        assertThat(groupFromXML).isEqualTo(groupFromDB);
    }

}