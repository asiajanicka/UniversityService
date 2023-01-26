package org.example.parserTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.StudentGroup;
import org.example.service.StudentService;
import org.example.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;

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

        logger.info("Start of Jaxb Tests - test case 1");
        StudentService studentService = new StudentService();
        StudentGroup groupFromDB = studentService.getStudentGroupById(1);
        JAXBContext context = JAXBContext.newInstance(StudentGroup.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(groupFromDB, new File("src/test/resources/studentGroupsJAXB.xml"));

        Unmarshaller unmarshaller = context.createUnmarshaller();
        StudentGroup groupFromXML = (StudentGroup) unmarshaller.unmarshal(new File("src/test/resources/studentGroupsJAXB.xml"));
        assertThat(groupFromXML).isEqualTo(groupFromDB);
        logger.info("End of Jaxb Tests - test case 1");
    }

}