package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.HaveChildrenException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.model.TechnologyTree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TechnologyServiceTest {


    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private TechnologyDao technologyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private KnowledgeDao knowledgeDao;

    @Test
    void listRelevantTechnologies() {
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology1.getId(), true));

        List<TechnologyTree> result = technologyService.listRelevantTechnologies();

        // no se puede porque los array vacíos tienen distinto hash
        /*
        TechnologiesTreeList spring = new TechnologiesTreeList(technology3, new ArrayList<>());
        TechnologiesTreeList maven = new TechnologiesTreeList(technology4, new ArrayList<>());
        ArrayList<TechnologiesTreeList> backendChildren = new ArrayList<>();
        backendChildren.add(spring);
        backendChildren.add(maven);
        TechnologiesTreeList backend = new TechnologiesTreeList(technology1, backendChildren);
        TechnologiesTreeList frontend = new TechnologiesTreeList(technology2, new ArrayList<>());
        ArrayList<TechnologiesTreeList> rootChildren = new ArrayList<>();
        rootChildren.add(backend);
        rootChildren.add(frontend);
        TechnologiesTreeList expected = new TechnologiesTreeList(null, rootChildren);

        assertEquals(expected, result);
         */

        assertEquals(result.get(0).getParentTechnology(), technology1);
        assertEquals(result.get(1).getParentTechnology(), technology2);
        assertEquals(result.get(0).getChildrenTechnology().get(0).getParentTechnology(), technology3);
        assertEquals(result.get(0).getChildrenTechnology().get(1).getParentTechnology(), technology4);
    }

    @Test
    void listJustRelevantTechnologies() {
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        technologyDao.save(new Technology("Java", null, false));
        technologyDao.save(new Technology("SpringBoot", technology3.getId(), false));

        List<TechnologyTree> result = technologyService.listRelevantTechnologies();

        assertEquals(result.get(0).getParentTechnology(), technology1);
        assertEquals(result.get(1).getParentTechnology(), technology2);
        assertEquals(result.get(0).getChildrenTechnology().get(0).getParentTechnology(), technology3);
        assertEquals(result.get(0).getChildrenTechnology().get(1).getParentTechnology(), technology4);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getChildrenTechnology().size(), 2);
    }

    @Test
    void listRelevantTechnologiesEmpty() {
        Technology technology1 = technologyDao.save(new Technology("Backend", null, false));
        Technology technology2 = technologyDao.save(new Technology("Frontend", null, false));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology1.getId(), false));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology1.getId(), false));
        technologyDao.save(new Technology("Java", null, false));
        technologyDao.save(new Technology("SpringBoot", technology3.getId(), false));

        List<TechnologyTree> result = technologyService.listRelevantTechnologies();

        assertEquals(result.size(), 0);
    }

    @Test
    void addTechnology() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        try {
            String name = "Java";
            Long parentId = null;
            technologyService.addTechnology(name, parentId, user.getId());
            Optional<Technology> result = technologyDao.findByNameAndParentId(name, parentId);

            assert(result.isPresent());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyAdmin() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        try {
            String name = "Java";
            Long parentId = null;
            technologyService.addTechnology(name, parentId, user.getId());
            Optional<Technology> result = technologyDao.findByNameAndParentId(name, parentId);

            assert(result.isPresent());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyDeveloper() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Developer", null));
        String name = "Java";
        Long parentId = null;
        try {
            technologyService.addTechnology(name, parentId, user.getId());
            Technology technology = technologyDao.findByNameAndParentId(name, parentId).get();

            Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);

            assertFalse(technology.isRelevant());
            assert  knowledge.isPresent();

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyDeveloperTree() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        String name = "SpringBoot";
        try {
            technologyService.addTechnology(name, technology2.getId(), user.getId());
            Technology technology = technologyDao.findByNameAndParentId(name, technology2.getId()).get();
            Optional<Knowledge> knowledge1 = knowledgeDao.findByUserAndTechnology(user, technology1);
            Optional<Knowledge> knowledge2 = knowledgeDao.findByUserAndTechnology(user, technology2);
            Optional<Knowledge> knowledge3 = knowledgeDao.findByUserAndTechnology(user, technology3);

            assertFalse(technology.isRelevant());
            assert  knowledge1.isPresent();
            assert  knowledge2.isPresent();
            assertFalse(knowledge3.isPresent());

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addExistedTechnologyDeveloper() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Developer", null));
        String name = "Java";
        Long parentId = null;
        technologyDao.save(new Technology(name, parentId, false));
        try {
            technologyService.addTechnology(name, parentId, user.getId());
            Technology technology = technologyDao.findByNameAndParentId(name, parentId).get();
            Optional<Knowledge> knowledge = knowledgeDao.findByUserAndTechnology(user, technology);

            assertFalse(technology.isRelevant());
            assert  knowledge.isPresent();

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addExistedTechnologyAdmin() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        String name = "Java";
        Long parentId = null;
        technologyDao.save(new Technology(name, parentId, false));
        try {
            technologyService.addTechnology(name, parentId, user.getId());
            Technology technology = technologyDao.findByNameAndParentId(name, parentId).get();

            assert technology.isRelevant();

        } catch (InstanceNotFoundException | DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyUserNotFound() {
        try {
            technologyService.addTechnology("Java", null, NON_EXISTENT_ID);
        } catch (InstanceNotFoundException e) {
            assert true;
        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyParentTechnologyNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        try {
            technologyService.addTechnology("Java", NON_EXISTENT_ID, user.getId());
        } catch (InstanceNotFoundException e) {
            assert true;
        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void addTechnologyDuplicate() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        technologyDao.save(new Technology("Java", null, true));
        try {
            technologyService.addTechnology("Java", null, user.getId());
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (DuplicateInstanceException e) {
            assert true;
        }
    }

    @Test
    void deleteTechnology() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        try {
            technologyService.deleteTechnology(user.getId(), technology.getId(), false);
            Optional<Technology> result = technologyDao.findById(technology.getId());

            assertFalse(result.isPresent());

        } catch (HaveChildrenException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteChildTechnologies() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        try {
            technologyService.deleteTechnology(user.getId(), technology1.getId(), true);
            Optional<Technology> result1 = technologyDao.findById(technology1.getId());
            Optional<Technology> result2 = technologyDao.findById(technology2.getId());
            Optional<Technology> result3 = technologyDao.findById(technology3.getId());
            Optional<Technology> result4 = technologyDao.findById(technology4.getId());

            assertFalse(result1.isPresent());
            assertFalse(result2.isPresent());
            assertFalse(result3.isPresent());
            assertFalse(result4.isPresent());

        } catch (HaveChildrenException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteKnownTechnology() {
        User user1 = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "pass", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        knowledgeDao.save(new Knowledge(user2, technology, false, false));
        try {
            technologyService.deleteTechnology(user1.getId(), technology.getId(), false);
            Optional<Technology> result = technologyDao.findById(technology.getId());

            assertFalse(result.get().isRelevant());

        } catch (HaveChildrenException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteKnownTechnologyTree() {
        User user1 = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "pass", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        knowledgeDao.save(new Knowledge(user2, technology1, false, false));
        knowledgeDao.save(new Knowledge(user2, technology2, false, false));
        try {
            technologyService.deleteTechnology(user1.getId(), technology1.getId(), true);
            Optional<Technology> result1 = technologyDao.findById(technology1.getId());
            Optional<Technology> result2 = technologyDao.findById(technology2.getId());
            Optional<Technology> result3 = technologyDao.findById(technology3.getId());
            Optional<Technology> result4 = technologyDao.findById(technology4.getId());

            assertFalse(result1.get().isRelevant());
            assertFalse(result2.get().isRelevant());
            assertFalse(result3.isPresent());
            assertFalse(result4.isPresent());

        } catch (HaveChildrenException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteTechnologiesHaveChildrenException() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        try {
            technologyService.deleteTechnology(user.getId(), technology1.getId(), false);
            assert false;
        } catch (HaveChildrenException e) {

            assert true;

        } catch (PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteTechnologiesPermissionException() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        try {
            technologyService.deleteTechnology(user.getId(), technology1.getId(), false);
            assert false;
        } catch (PermissionException e) {

            assert true;

        } catch (HaveChildrenException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteTechnologiesInstanceNotFoundException() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Developer", null));
        try {
            technologyService.deleteTechnology(user.getId(), NON_EXISTENT_ID, false);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (HaveChildrenException | PermissionException e) {
            assert false;
        }
    }
}