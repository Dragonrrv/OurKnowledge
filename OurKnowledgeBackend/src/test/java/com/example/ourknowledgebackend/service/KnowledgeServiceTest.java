package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class KnowledgeServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private KnowledgeDao knowledgeDao;

    @Autowired
    private TechnologyDao technologyDao;

    @Autowired
    private UserDao userDao;

    @Test
    void addKnowledge() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), technology.getId(), null, null);
            Optional<Knowledge> result = knowledgeDao.findById(knowledges.get(0).getId());

            assertEquals(knowledges.get(0), result.get());

        } catch (InstanceNotFoundException | DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addTechnologyAndKnowledge() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Backend", null, true));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), null, "Spring", technology.getId());
            Optional<Knowledge> result = knowledgeDao.findById(knowledges.get(1).getId());

            assertEquals(knowledges.get(1), result.get());

        } catch (InstanceNotFoundException | DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addTechnologyWhitParentAndKnowledge() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), null, "Java", null);
            Optional<Knowledge> result = knowledgeDao.findById(knowledges.get(0).getId());

            assertEquals(knowledges.get(0), result.get());

        } catch (InstanceNotFoundException | DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addKnowledgeList() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology2.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology1.getId(), true));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), technology2.getId(), null, null);
            Optional<Knowledge> result1 = knowledgeDao.findById(knowledges.get(0).getId());
            Optional<Knowledge> result2 = knowledgeDao.findById(knowledges.get(1).getId());

            assertEquals(knowledges.get(0), result1.get());
            assertEquals(knowledges.get(1), result2.get());
            assertEquals(knowledges.size(), 2);

        } catch (InstanceNotFoundException | DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addKnowledgeUserNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(NON_EXISTENT_ID, technology.getId(), null, null);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addKnowledgeTechnologyNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), NON_EXISTENT_ID, null, null);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addKnowledgeDuplicateException() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        knowledgeDao.save(new Knowledge(user, technology, false, false));
        try {
            List<Knowledge> knowledges = knowledgeService.addKnowledge(user.getId(), technology.getId(), null, null);
            assert false;
        } catch (DuplicateInstanceException e) {

            assert true;

        } catch (InstanceNotFoundException | InvalidAttributesException  e) {
            assert false;
        }
    }

    @Test
    void addKnowledgeInvalidAttributesException() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        try {
            knowledgeService.addKnowledge(user.getId(), null, null, technology.getId());
            assert false;
        } catch (InvalidAttributesException e) {

            assert true;

        } catch (InstanceNotFoundException | DuplicateInstanceException  e) {
            assert false;
        }
    }


    @Test
    void deleteKnowledge() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user, technology, false, false));
        try {
            knowledgeService.deleteKnowledge(user.getId(), knowledge.getId());
            Optional<Knowledge> result = knowledgeDao.findById(knowledge.getId());

            assertFalse(result.isPresent());

        } catch (Exception e) {
            assert false;
        }
    }


    @Test
    void deleteKnowledgeTree() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology2.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology1.getId(), true));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, false));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, false, false));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));
        try {
            knowledgeService.deleteKnowledge(user.getId(), knowledge1.getId());
            Optional<Knowledge> result1 = knowledgeDao.findById(knowledge1.getId());
            Optional<Knowledge> result2 = knowledgeDao.findById(knowledge2.getId());
            Optional<Knowledge> result3 = knowledgeDao.findById(knowledge3.getId());
            Optional<Knowledge> result4 = knowledgeDao.findById(knowledge4.getId());

            assertFalse(result1.isPresent());
            assertFalse(result2.isPresent());
            assertFalse(result3.isPresent());
            assertFalse(result4.isPresent());

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    void updateKnowledge() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user, technology, false, false));
        try {
            knowledgeService.updateKnowledge(user.getId(), knowledge.getId(), true, true);
            knowledge.setMainSkill(true);
            knowledge.setLikeIt(true);
            Optional<Knowledge> result = knowledgeDao.findById(knowledge.getId());

            assertEquals(result.get(), knowledge);

        } catch (InstanceNotFoundException | PermissionException e) {
            assert false;
        }
    }

    @Test
    void updateKnowledgeToFalse() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user, technology, true, true));
        try {
            knowledgeService.updateKnowledge(user.getId(), knowledge.getId(), false, false);
            knowledge.setMainSkill(false);
            knowledge.setLikeIt(false);
            Optional<Knowledge> result = knowledgeDao.findById(knowledge.getId());

            assertEquals(result.get(), knowledge);

        } catch (InstanceNotFoundException | PermissionException e) {
            assert false;
        }
    }

    @Test
    void updateKnowledgeUserNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user, technology, false, false));
        try {
            knowledgeService.updateKnowledge(NON_EXISTENT_ID, knowledge.getId(), true, true);
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (PermissionException e) {
            assert false;
        }
    }

    @Test
    void updateKnowledgeNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user, technology, false, false));
        try {
            knowledgeService.updateKnowledge(user.getId(), NON_EXISTENT_ID, true, true);
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (PermissionException e) {
            assert false;
        }
    }

    @Test
    void updateKnowledgePermissionException() {
        User user1 = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example@example.com", "Developer", null));
        Technology technology = technologyDao.save(new Technology("Java", null, true));
        Knowledge knowledge = knowledgeDao.save(new Knowledge(user1, technology, false, false));
        try {
            knowledgeService.updateKnowledge(user2.getId(), knowledge.getId(), true, true);
        } catch (PermissionException e) {

            assert true;

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

}