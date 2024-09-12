package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private KnowledgeDao knowledgeDao;

    @Autowired
    private TechnologyDao technologyDao;

    @Autowired
    private UserDao userDao;

    @Test
    void login() {
        User result = userService.login("Juan", "example@example.com", "Admin");

        User user = userDao.findByEmail("example@example.com");

        assertEquals(user, result);
    }

    @Test
    void loginDeveloper() {
        User result = userService.login("Juan", "example@example.com", "Developer");

        User user = userDao.findByEmail("example@example.com");

        assertEquals(user, result);
    }

    @Test
    void loginExists() {
        User user1 = userDao.save(new User("Juan1", "example@example.com", "Admin", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Admin", null));
        User result = userService.login("Juan", "example@example.com", "Admin");

        assertEquals(user1, result);
    }

    @Test
    void listUsers() {
        User user1 = userDao.save(new User("Juan1", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        User user3 = userDao.save(new User("Juan3", "example2@example.com", "Developer", null));
        User user4 = userDao.save(new User("Juan4", "example2@example.com", "Developer", null));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        Block<User> expected = new Block<>(userList, false, 1, 5);

        Block<User> result = userService.listUsers(expected.getPage(), null, expected.getSize());

        assertEquals(expected, result);
    }

    @Test
    void listUsersOnlyDeveloper() {
        User user1 = userDao.save(new User("Juan1", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        User user3 = userDao.save(new User("Juan3", "example2@example.com", "Admin", null));
        User user4 = userDao.save(new User("Juan4", "example2@example.com", "Developer", null));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user4);

        Block<User> expected = new Block<>(userList, false, 1, 5);

        Block<User> result = userService.listUsers(expected.getPage(), null, expected.getSize());

        assertEquals(expected, result);
    }

    @Test
    void listUsersOrder() {
        User user1 = userDao.save(new User("AJuan1", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("CJuan2", "example2@example.com", "Developer", null));
        User user3 = userDao.save(new User("BJuan3", "example2@example.com", "Developer", null));
        User user4 = userDao.save(new User("JJuan4", "example2@example.com", "Developer", null));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user3);
        userList.add(user2);
        userList.add(user4);

        Block<User> expected = new Block<>(userList, false, 1, 5);

        Block<User> result = userService.listUsers(expected.getPage(), null, expected.getSize());

        assertEquals(expected, result);
    }

    @Test
    void listUsersKeyWordsFilter() {
        User user1 = userDao.save(new User("Juan1", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Manuel1", "example2@example.com", "Developer", null));
        User user3 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        User user4 = userDao.save(new User("Manuel2", "example2@example.com", "Developer", null));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user3);

        Block<User> expected = new Block<>(userList, false, 1, 5);

        Block<User> result = userService.listUsers(expected.getPage(), "juA", expected.getSize());

        assertEquals(expected, result);
    }

    @Test
    void showProfile() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Java", null, true));
        Technology technology2 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology2.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Other", technology2.getId(), true));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, true));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, true, true));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));

        UserProfile userProfile = userService.showProfile(user.getId(), user.getId());

        assertEquals(userProfile.getUser(), user);
        // todos los datos correctos
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getId(), technology1.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getName(), technology1.getName());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().isRelevant(), technology1.isRelevant());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getKnowledgeId(), knowledge1.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getMainSkill(), knowledge1.isMainSkill());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getLikeIt(), knowledge1.isLikeIt());
        // Todas las tecnologías conocidas
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getParent().getKnowledgeId(), knowledge2.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(0).getParent().getKnowledgeId(), knowledge3.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(1).getParent().getKnowledgeId(), knowledge4.getId());
        // No conocida
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(2).getParent().getId(), technology5.getId());
        assertNull(userProfile.getKnowledgeTreeList().get(1).getChildren().get(2).getParent().getKnowledgeId());
    }

    @Test
    void showProfileMultiUsers() {
        User user = userDao.save(new User("Juan", "example@example.com", "Developer", null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", "Developer", null));
        Technology technology1 = technologyDao.save(new Technology("Java", null, true));
        Technology technology2 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology2.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Other", technology2.getId(), true));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, true));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, true, true));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));
        Knowledge knowledge5 = knowledgeDao.save(new Knowledge(user2, technology1, false, false));

        UserProfile userProfile = userService.showProfile(user.getId(), user.getId());

        assertEquals(userProfile.getUser(), user);
        // todos los datos correctos
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getId(), technology1.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getName(), technology1.getName());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().isRelevant(), technology1.isRelevant());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getKnowledgeId(), knowledge1.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getMainSkill(), knowledge1.isMainSkill());
        assertEquals(userProfile.getKnowledgeTreeList().get(0).getParent().getLikeIt(), knowledge1.isLikeIt());
        // Todas las tecnologías conocidas
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getParent().getKnowledgeId(), knowledge2.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(0).getParent().getKnowledgeId(), knowledge3.getId());
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(1).getParent().getKnowledgeId(), knowledge4.getId());
        // No conocida
        assertEquals(userProfile.getKnowledgeTreeList().get(1).getChildren().get(2).getParent().getId(), technology5.getId());
        assertNull(userProfile.getKnowledgeTreeList().get(1).getChildren().get(2).getParent().getKnowledgeId());
        // Tamaños correctos
        assert userProfile.getKnowledgeTreeList().size() == 2;
    }
}