package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.model.ProjectDetails;
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

    @Autowired
    private KnowledgeDao knowledgeDao;

    @Autowired
    private VerificationDao verificationDao;

    @Test
    void listProjects() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false, 1, 5);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    // they are order by startDate
    void listProjectsOrder() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-04", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-02", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-03", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project4);
        projectList.add(project3);
        projectList.add(project2);

        Block<Project> expected = new Block<>(projectList, false, 1, 5);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    // they are order by startDate
    void listProjectsExistMoreTrue() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);

        Block<Project> expected = new Block<>(projectList, true, 1, 3);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void listProjectsFullButExistMoreFalse() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false, 1, 4);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void listProjectsKeyWordsFilter() {
        Project project1 = projectDao.save(new Project("name1Key", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3key", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project3);

        Block<Project> expected = new Block<>(projectList, false, 1, 5);

        Block<Project> result = projectService.listProjects(expected.getPage(), "KeY", expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void listProjectsEmpty() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();

        Block<Project> expected = new Block<>(projectList, false, 1, 5);

        Block<Project> result = projectService.listProjects(expected.getPage(), "key", expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void listProjectsPage2() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project3);
        projectList.add(project4);

        Block<Project> expected = new Block<>(projectList, false, 2, 2);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void listProjectsPage3() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));
        Project project3 = projectDao.save(new Project("name3", "description3", "doing", "2024-08-01", 3));
        Project project4 = projectDao.save(new Project("name4", "description4", "doing", "2024-08-01", 4));
        Project project5 = projectDao.save(new Project("name5", "description5", "doing", "2024-08-01", 4));

        List<Project> projectList = new ArrayList<>();
        projectList.add(project5);

        Block<Project> expected = new Block<>(projectList, false, 3, 2);

        Block<Project> result = projectService.listProjects(expected.getPage(), null, expected.getSize());

        assertEquals(result, expected);
    }

    @Test
    void projectDetails() {
        Project project1 = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project2 = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 2));

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
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Frontend", null, true));
        Uses uses1 = usesDao.save(new Uses(project1, technology1));
        Uses uses2 = usesDao.save(new Uses(project1, technology3));

        User user1 = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        userDao.save(new User("Juan3", "example3@example.com", "Developer", null));
        Participation participation1 = participationDao.save(new Participation(project1, user1, "2024-08-22", null));
        Participation participation2 = participationDao.save(new Participation(project1, user2, "2024-08-21", null));
        List<Participation> participationList = new ArrayList<>();
        participationList.add(participation2);
        participationList.add(participation1);

        ProjectDetails expected = new ProjectDetails(project1, null, participationList);

        try{
            ProjectDetails result = projectService.projectDetails(project1.getId());
            assertEquals(result.getProject(), expected.getProject());
            assertEquals(result.getUsesTreeList().get(0).getParent().getUsesId(), uses1.getId());
            assertNull(result.getUsesTreeList().get(0).getChildren().get(0).getParent().getUsesId());
            assertEquals(result.getUsesTreeList().get(1).getParent().getUsesId(), uses2.getId());
            assertEquals(result.getUsesTreeList().size(), 2);
            assertEquals(result.getParticipationList(), participationList);
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
        try{
            projectService.addProject("name", "description", "doing", "2024-08-01", 1, new ArrayList<>());
            Project result = projectDao.findAll().iterator().next();
            Project expected = new Project("name", "description", "doing", "2024-08-01", 1);

            assertEquals(expected.getName(), result.getName());
            assertEquals(expected.getDescription(), result.getDescription());
            assertEquals(expected.getStatus(), result.getStatus());
            assertEquals(expected.getStartDate(), result.getStartDate());
            assertEquals(expected.getSize(), result.getSize());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addProjectWithTechnologies() {
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        List<Long> technologiesId = new ArrayList<>();
        technologiesId.add(technology1.getId());
        technologiesId.add(technology2.getId());
        technologiesId.add(technology3.getId());

        try{
            projectService.addProject("name", "description", "doing", "2024-08-01", 1, technologiesId);
            Project project = projectDao.findAll().iterator().next();
            List<Uses> result = usesDao.findAllByProject(project);

            assertEquals(technology1, result.get(0).getTechnology());
            assertEquals(technology2, result.get(1).getTechnology());
            assertEquals(technology3, result.get(2).getTechnology());
            assertEquals(project, result.get(0).getProject());
            assertEquals(project, result.get(1).getProject());
            assertEquals(project, result.get(2).getProject());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addProjectInstanceNotFound() {
        List<Long> technologiesId = new ArrayList<>();
        technologiesId.add(NON_EXISTENT_ID);
        try{
            projectService.addProject("name", "description", "doing", "2024-08-01", 1, technologiesId);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    // two projects cant have the same name
    void addProjectDuplicateInstance() {
        try{
            projectService.addProject("name", "description1", "doing", "2024-08-01", 1, new ArrayList<>());
            projectService.addProject("name", "description2", "doing", "2024-08-02", 2, new ArrayList<>());
            assert false;
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (DuplicateInstanceException e) {

            assert true;

        }
    }

    @Test
    void updateProject() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        usesDao.save(new Uses(project, technology1));
        usesDao.save(new Uses(project, technology2));
        usesDao.save(new Uses(project, technology3));

        Technology technology4 = technologyDao.save(new Technology("Languages", null, true));
        Technology technology5 = technologyDao.save(new Technology("Database", null, true));
        Technology technology6 = technologyDao.save(new Technology("Java", technology4.getId(), true));
        List<Long> technologiesId = new ArrayList<>();
        technologiesId.add(technology1.getId());
        technologiesId.add(technology4.getId());
        technologiesId.add(technology5.getId());
        technologiesId.add(technology6.getId());
        Project expected = new Project("newName", "newDescription", "done", "2024-05-01", 3);

        try {
            projectService.updateProject(project.getId(), expected.getName(), expected.getDescription(), expected.getStatus(), expected.getStartDate(), expected.getSize(), true, technologiesId);
            Project result = projectDao.findById(project.getId()).orElse(new Project());

            List<Uses> usesResult = usesDao.findAllByProject(project);

            assertEquals(expected.getName(), result.getName());
            assertEquals(expected.getDescription(), result.getDescription());
            assertEquals(expected.getStatus(), result.getStatus());
            assertEquals(expected.getStartDate(), result.getStartDate());
            assertEquals(expected.getSize(), result.getSize());

            assertEquals(technology1, usesResult.get(0).getTechnology());
            assertEquals(technology4, usesResult.get(1).getTechnology());
            assertEquals(technology5, usesResult.get(2).getTechnology());
            assertEquals(technology6, usesResult.get(3).getTechnology());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    // Use a negative number in size for no changes
    void updateProjectNoChanges() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        usesDao.save(new Uses(project, technology1));
        try {
            projectService.updateProject(project.getId(), null, null, null, null, -1, false, new ArrayList<>());
            Project result = projectDao.findAll().iterator().next();
            assertEquals(project, result);
        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateProjectInstanceNotFound() {
        projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        try {
            projectService.updateProject(NON_EXISTENT_ID, null, null, null, null, -1, false, new ArrayList<>());
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    // Cant update a project to have the same name to another
    void updateProjectDuplicateInstance() {
        projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        Project project = projectDao.save(new Project("name2", "description2", "doing", "2024-08-01", 1));

        try {
            projectService.updateProject(project.getId(), "name1", null, null, null, -1, false, new ArrayList<>());
            assert false;
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (DuplicateInstanceException e) {

            assert true;

        }
    }

    @Test
    void deleteProject() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        try {
            projectService.deleteProject(project.getId());

            assertFalse(projectDao.findAll().iterator().hasNext());

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteProjectInstanceNotFound() {
        projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        try {
            projectService.deleteProject(NON_EXISTENT_ID);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        }
    }

    @Test
    void participate() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Participation expected = new Participation(project, user, "2024-08-02", null);
        try {
            projectService.participate(user.getId(), project.getId(), expected.getStartDate(), expected.getEndDate());
            Participation result = participationDao.findAll().iterator().next();

            assertEquals(expected.getProject(), result.getProject());
            assertEquals(expected.getUser(), result.getUser());
            assertEquals(expected.getStartDate(), result.getStartDate());
            assertEquals(expected.getEndDate(), result.getEndDate());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void participateWithEndDate() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Participation expected = new Participation(project, user, "2024-08-02", "2024-08-06");
        try {
            projectService.participate(user.getId(), project.getId(), expected.getStartDate(), expected.getEndDate());
            Participation result = participationDao.findAll().iterator().next();

            assertEquals(expected.getProject(), result.getProject());
            assertEquals(expected.getUser(), result.getUser());
            assertEquals(expected.getStartDate(), result.getStartDate());
            assertEquals(expected.getEndDate(), result.getEndDate());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void participateInstanceNotFoundProject() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        try {
            projectService.participate(user.getId(), NON_EXISTENT_ID, "2024-08-02", "2024-08-06");
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void participateInstanceNotFoundUser() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        try {
            projectService.participate(NON_EXISTENT_ID, project.getId(), "2024-08-02", "2024-08-06");
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void participateDuplicateInstance() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        participationDao.save(new Participation(project, user, "2024-08-21", null));
        try {
            projectService.participate(user.getId(), project.getId(), "2024-08-02", "2024-08-06");
            assert false;
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (DuplicateInstanceException e) {

            assert true;

        }
    }

    @Test
    void updateParticipate() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Participation participation = participationDao.save(new Participation(project, user, "2024-08-21", null));
        Participation expected = new Participation(project, user, "2024-08-22", "2024-08-24");
        try {
            projectService.updateParticipate(participation.getId(), expected.getStartDate(), expected.getEndDate());
            Participation result = participationDao.findAll().iterator().next();

            assertEquals(expected.getStartDate(), result.getStartDate());
            assertEquals(expected.getEndDate(), result.getEndDate());

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void updateParticipateInstanceNotFound() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Participation participation = participationDao.save(new Participation(project, user, "2024-08-21", null));
        Participation expected = new Participation(project, user, "2024-08-22", "2024-08-24");
        try {
            projectService.updateParticipate(NON_EXISTENT_ID, expected.getStartDate(), expected.getEndDate());
            assert false;
        } catch (InstanceNotFoundException e) {
            assert true;
        }
    }

    @Test
    void addVerification() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Uses uses1 = usesDao.save(new Uses(project, technology1));
        Uses uses2 = usesDao.save(new Uses(project, technology2));
        usesDao.save(new Uses(project, technology3));
        usesDao.save(new Uses(project, technology4));
        usesDao.save(new Uses(project, technology5));

        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        participationDao.save(new Participation(project, user, "2024-08-21", null));

        try {
            projectService.addVerification(user.getId(), uses2.getId());

            List<Verification> verificationList = verificationDao.findAllByKnowledgeUser(user);

            assertEquals(uses1, verificationList.get(0).getUses());
            assertEquals(uses2, verificationList.get(1).getUses());
            assertEquals(technology1, verificationList.get(0).getKnowledge().getTechnology());
            assertEquals(technology2, verificationList.get(1).getKnowledge().getTechnology());
            assertEquals(2, verificationList.size());

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteVerification() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Uses uses1 = usesDao.save(new Uses(project, technology1));
        Uses uses2 = usesDao.save(new Uses(project, technology2));
        Uses uses3 = usesDao.save(new Uses(project, technology3));
        Uses uses4 = usesDao.save(new Uses(project, technology4));
        Uses uses5 = usesDao.save(new Uses(project, technology5));

        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        participationDao.save(new Participation(project, user, "2024-08-21", null));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, false));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, false, false));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));
        Knowledge knowledge5 = knowledgeDao.save(new Knowledge(user, technology5, false, false));
        Verification verification1 = verificationDao.save(new Verification(knowledge1, uses1));
        Verification verification2 = verificationDao.save(new Verification(knowledge2, uses2));
        Verification verification3 = verificationDao.save(new Verification(knowledge3, uses3));
        Verification verification4 = verificationDao.save(new Verification(knowledge4, uses4));
        Verification verification5 = verificationDao.save(new Verification(knowledge5, uses5));

        try {
            projectService.deleteVerification(user.getId(), uses2.getId(), false);

            List<Verification> verificationList = verificationDao.findAllByKnowledgeUser(user);
            List<Knowledge> knowledgeList = knowledgeDao.findAllByUser(user);

            assertEquals(verification1, verificationList.get(0));
            assertEquals(verification3, verificationList.get(1));
            assertEquals(verification5, verificationList.get(2));
            assertEquals(3, verificationList.size());

            assertEquals(5, knowledgeList.size());

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteVerificationDeleteKnowledgeTrue() {
        Project project = projectDao.save(new Project("name1", "description1", "doing", "2024-08-01", 1));

        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Uses uses1 = usesDao.save(new Uses(project, technology1));
        Uses uses2 = usesDao.save(new Uses(project, technology2));
        Uses uses3 = usesDao.save(new Uses(project, technology3));
        Uses uses4 = usesDao.save(new Uses(project, technology4));
        Uses uses5 = usesDao.save(new Uses(project, technology5));

        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        participationDao.save(new Participation(project, user, "2024-08-21", null));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, false));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, false, false));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));
        Knowledge knowledge5 = knowledgeDao.save(new Knowledge(user, technology5, false, false));
        Verification verification1 = verificationDao.save(new Verification(knowledge1, uses1));
        Verification verification2 = verificationDao.save(new Verification(knowledge2, uses2));
        Verification verification3 = verificationDao.save(new Verification(knowledge3, uses3));
        Verification verification4 = verificationDao.save(new Verification(knowledge4, uses4));
        Verification verification5 = verificationDao.save(new Verification(knowledge5, uses5));

        try {
            projectService.deleteVerification(user.getId(), uses2.getId(), true);

            List<Verification> verificationList = verificationDao.findAllByKnowledgeUser(user);
            List<Knowledge> knowledgeList = knowledgeDao.findAllByUser(user);

            assertEquals(verification1, verificationList.get(0));
            assertEquals(verification3, verificationList.get(1));
            assertEquals(verification5, verificationList.get(2));
            assertEquals(3, verificationList.size());

            assertEquals(knowledge1, knowledgeList.get(0));
            assertEquals(knowledge3, knowledgeList.get(1));
            assertEquals(knowledge5, knowledgeList.get(2));
            assertEquals(3, knowledgeList.size());

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }
}