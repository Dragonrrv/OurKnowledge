package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        User user2 = userDao.save(new User("Juan2", "example@example.com", "pass", "Admin", null));
        User result = userService.login("Juan", "pass");

        assertEquals(user, result);
    }

    @Test
    void showProfile() {
        User user = userDao.save(new User("Juan", "example@example.com", "pass", "Admin", null));
        Technology technology1 = technologyDao.save(new Technology("Java", null, true));
        Technology technology2 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology3 = technologyDao.save(new Technology("Spring", technology2.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("Maven", technology2.getId(), true));
        technologyDao.save(new Technology("Other", technology2.getId(), true));
        Knowledge knowledge1 = knowledgeDao.save(new Knowledge(user, technology1, false, false));
        Knowledge knowledge2 = knowledgeDao.save(new Knowledge(user, technology2, false, true));
        Knowledge knowledge3 = knowledgeDao.save(new Knowledge(user, technology3, true, true));
        Knowledge knowledge4 = knowledgeDao.save(new Knowledge(user, technology4, false, false));

        UserProfile userProfile = userService.showProfile(user.getId(), user.getId());

        assertEquals(userProfile.getUser(), user);
        assertNull(userProfile.getKnowledgeTreeList().getParentKnowledge());
        assertEquals(userProfile.getKnowledgeTreeList().getChildKnowledges().get(0).getParentKnowledge(), knowledge1);
        assertEquals(userProfile.getKnowledgeTreeList().getChildKnowledges().get(1).getParentKnowledge(), knowledge2);
        assertEquals(userProfile.getKnowledgeTreeList().getChildKnowledges().get(1).getChildKnowledges().get(0).getParentKnowledge(), knowledge3);
        assertEquals(userProfile.getKnowledgeTreeList().getChildKnowledges().get(1).getChildKnowledges().get(1).getParentKnowledge(), knowledge4);
        assertEquals(userProfile.getKnowledgeTreeList().getChildKnowledges().get(1).getChildKnowledges().size(), 2);
    }
}