package org.example.parserTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.StudentGroup;
import org.example.utils.XmlUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DOMTests {

    private static final Logger logger = LogManager.getLogger(DOMTests.class);

    @Test
    public void validateXMLAgainstSchema() throws ParserConfigurationException, IOException, SAXException {

        logger.info("Start of DOM Parser Tests - test case 1");
        Schema schema = XmlUtils.getSchema("src/test/resources/studentGroups.xsd");
        Document document = XmlUtils.parseXml("src/test/resources/studentGroups.xml");
        assertThat(XmlUtils.validateXml(schema, document)).isTrue();

        List<StudentGroup> studentGroupsFromXML = XmlUtils.getStudentGroupsFromDocument(document);

        assertThat(studentGroupsFromXML.size()).isEqualTo(2);

        assertThat(studentGroupsFromXML.get(0).getStudents().size()).isEqualTo(2);
        assertThat(studentGroupsFromXML.get(0).getStudents()
                .stream()
                .filter(p -> (p.getPortalAccount() != null))
                .count())
                .isEqualTo(2);
        assertThat(studentGroupsFromXML.get(0).getStudents().get(0).getGrades().size()).isEqualTo(3);

        assertThat(studentGroupsFromXML.get(1).getStudents().size()).isEqualTo(3);
        assertThat(studentGroupsFromXML.get(1).getStudents()
                .stream()
                .filter(p -> (p.getPortalAccount() != null))
                .count())
                .isEqualTo(2);
        assertThat(studentGroupsFromXML.get(1).getStudents().get(2).getGrades()).isEmpty();
        logger.info("End of DOM Parser Tests - test case 1");
    }

}
