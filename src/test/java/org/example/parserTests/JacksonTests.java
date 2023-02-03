package org.example.parserTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.model.StudentGroup;
import org.example.service.StudentService;
import org.example.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTests {

    private static final Logger logger = LogManager.getLogger(JacksonTests.class);

    @Test
    public void usecaseTest() throws EntityNotFoundException, IOException {

        File file = new File(TestUtils.getNameForJsonFile("studentGroupsJackson"));
        StudentService studentService = new StudentService();
        List<StudentGroup> groupsFromDB = new ArrayList<>();
        groupsFromDB.add(studentService.getStudentGroupById(1));
        groupsFromDB.add(studentService.getStudentGroupById(2));
        logger.info("Created a list of student groups (1 & 2)");

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(file, groupsFromDB);
        logger.info(String.format("Wrote list of groups to json '%s'", file));

        List<StudentGroup> groupsFromJSON = objectMapper.readValue(file, new TypeReference<>(){});
        logger.info(String.format("Read list of groups to from json '%s' to object", file));
        assertThat(groupsFromJSON).isEqualTo(groupsFromDB);
    }

}