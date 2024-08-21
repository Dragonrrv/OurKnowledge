package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
import com.example.ourknowledgebackend.model.TechnologyTree;
import com.example.ourknowledgebackend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProjectServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TechnologyDao technologyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UsesDao usesDao;

    @Autowired
    private ParticipationDao participationDao;

    @Test
    void listProjects() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(1, null, 5);

        assertEquals(result, expected);
    }

    @Test
    // they are order by startDate
    void listProjectsOrder() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-04", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-02", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project4);
        projectList.add(project3);
        projectList.add(project2);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(1, null, 5);

        assertEquals(result, expected);
    }

    @Test
    // they are order by startDate
    void listProjectsExistMoreTrue() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);

        Block<Project> expected = new Block<>(projectList, true);

        Block<Project> result = projectService.listProjects(1, null, 3);

        assertEquals(result, expected);
    }

    @Test
    void listProjectsFullButExistMoreFalse() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(1, null, 4);

        assertEquals(result, expected);
    }

    @Test
    void listProjectsKeyWordsFilter() {
        Project project1 = projectDao.save(new Project("name1Key", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3key", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project3);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(1, "KeY", 5);

        assertEquals(result, expected);
    }

    @Test
    void listProjectsEmpty() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(1, "key", 5);

        assertEquals(result, expected);
    }

    @Test
    void listProjectsPage2() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(2, null, 2);

        assertEquals(result, expected);
    }

    @Test
    void listProjectsPage3() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-03", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-04", 4));
        Project project5 = projectDao.save(new Project("name5", "description5", "doing", "2024-08-05", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project5);

        Block<Project> expected = new Block<>(projectList, false);

        Block<Project> result = projectService.listProjects(3, null, 2);

        assertEquals(result, expected);
    }

    @Test
    void projectDetails() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-02", 2));

        try{
            ProjectDetails result = projectService.projectDetails(project1.getId());
            assertEquals(result.getProject(), project1);
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void projectDetailsWithTechnologiesAndUsers() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, true));
        usesDao.save(new Uses(project1, technology1));
        usesDao.save(new Uses(project1, technology2));

        User user1 = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        participationDao.save(new Participation(project1, user1, "2024-08-21", null));
        participationDao.save(new Participation(project1, user2, "2024-08-21", null));
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        ProjectDetails expected = new ProjectDetails(project1, null, userList);

        try{
            ProjectDetails result = projectService.projectDetails(project1.getId());
            assertEquals(result.getProject(), expected.getProject());
            assertEquals(result.getTechnologyTreeList().get(0).getParent(), technology1);
            assertEquals(result.getTechnologyTreeList().get(1).getParent(), technology2);
            assertEquals(result.getTechnologyTreeList().size(), 2);
            assertEquals(result.getUserList(), userList);
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void projectDetailsInstanceNotFound() {
        projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        try{
            projectService.projectDetails(NON_EXISTENT_ID);
            assert false;
        } catch (InstanceNotFoundException e) {
            assert true;
        }
    }

    @Test
    void addProject() {
    }

    @Test
    void updateProject() {
    }

    @Test
    void deleteProject() {
    }

    @Test
    void participate() {
    }

    @Test
    void updateParticipate() {
    }

    @Test
    void verificate() {
    }
}