package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Schema getSchema(String pathToXSD) {
        Schema schema = null;
        try {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            schema = factory.newSchema(new File(pathToXSD));
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return schema;
    }

    public static Document parseXml(String pathToXML) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(pathToXML));
    }

    public static boolean validateXml(Schema schema, Document doc) {
        try {
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(doc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<StudentGroup> getStudentGroupsFromDocument(Document doc) {
        List<StudentGroup> studentGroups = new ArrayList<>();
        NodeList studentGroupList = doc.getElementsByTagName("studentGroup");
        for (int i = 0; i < studentGroupList.getLength(); i++) {
            StudentGroup tempGroup = new StudentGroup();
            Element gr = (Element) studentGroupList.item(i);

            tempGroup.setId(Integer.parseInt(gr.getAttribute("id")));
            tempGroup.setName(gr.getAttribute("name"));

            NodeList studentList = gr.getElementsByTagName("student");
            tempGroup.setStudents(getStudentsFromXML(studentList));
            studentGroups.add(tempGroup);
        }
        System.out.println("Student groups read from XML file:");
        studentGroups.forEach(System.out::println);
        return studentGroups;
    }

    private static List<Student> getStudentsFromXML(NodeList nodeList) {
        List<Student> tempStudents = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Student tempStudent = new Student();
            Element studentEl = (Element) nodeList.item(i);
            tempStudent.setId(Integer.parseInt(studentEl.getAttribute("id")));
            tempStudent.setFirstName(studentEl.getElementsByTagName("firstName").item(0).getTextContent());
            tempStudent.setLastName(studentEl.getElementsByTagName("lastName").item(0).getTextContent());
            tempStudent.setDateOfBirth(LocalDate.parse(studentEl.getElementsByTagName("dateOfBirth").item(0).getTextContent(), formatter));
            Element portalAccountEl = (Element) studentEl.getElementsByTagName("portalAccount").item(0);
            tempStudent.setPortalAccount(getPortalAccountFromXML(portalAccountEl));
            NodeList gradesNodeList = studentEl.getElementsByTagName("grade");
            tempStudent.setGrades(getGradesFromXML(gradesNodeList));
            tempStudents.add(tempStudent);
        }
        return tempStudents;
    }

    private static PortalAccount getPortalAccountFromXML(Element el) {
        if (el != null) {
            PortalAccount tempAccount = new PortalAccount();
            tempAccount.setId(Integer.parseInt(el.getAttribute("id")));
            tempAccount.setLogin(el.getElementsByTagName("login").item(0).getTextContent());
            tempAccount.setPassword(el.getElementsByTagName("password").item(0).getTextContent());
            tempAccount.setIssueDate(LocalDate.parse(el.getElementsByTagName("issueDate").item(0).getTextContent(), formatter));
            tempAccount.setExpiryDate(LocalDate.parse(el.getElementsByTagName("expiryDate").item(0).getTextContent(), formatter));
            return tempAccount;
        } else {
            return null;
        }
    }

    private static List<Grade> getGradesFromXML(NodeList nodeList) {
        List<Grade> tempGrades = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Grade tempGrade = new Grade();
            Element gradeEl = (Element) nodeList.item(i);
            tempGrade.setId(Integer.parseInt(gradeEl.getAttribute("id")));
            tempGrade.setValue(Integer.parseInt(gradeEl.getElementsByTagName("value").item(0).getTextContent()));
            tempGrade.setSubject(getSubjectFromXML(gradeEl));
            tempGrades.add(tempGrade);
        }
        return tempGrades;
    }

    private static Subject getSubjectFromXML(Element el) {
        if (el != null) {
            Subject tempSubject = new Subject();
            tempSubject.setId(Integer.parseInt(el.getAttribute("id")));
            tempSubject.setName(el.getElementsByTagName("name").item(0).getTextContent());
            return tempSubject;
        } else {
            return null;
        }
    }

}
