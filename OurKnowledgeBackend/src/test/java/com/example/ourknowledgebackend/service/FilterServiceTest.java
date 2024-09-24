package com.example.ourknowledgebackend.service;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.model.CompleteFilter;
import com.example.ourknowledgebackend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FilterServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Value("${app.constants.admin_role}")
    private String adminRole;

    @Value("${app.constants.default_filter_name}")
    private String defaultFilter;

    @Autowired
    private FilterService filterService;

    @Autowired
    private FilterDao filterDao;

    @Autowired
    private FilterParamDao filterParamDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TechnologyDao technologyDao;


    Filter setBaseFilter() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        FilterParam filterParam1 = filterParamDao.save(new FilterParam(filter, technology1, true, false));
        FilterParam filterParam2 = filterParamDao.save(new FilterParam(filter, technology2, true, false));
        FilterParam filterParam3 = filterParamDao.save(new FilterParam(filter, technology4, true, false));
        return filter;
    }



    @Test
    // Listar Filtros no debe retornar el filtro default
    void listFilter() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter1 = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));
        Filter filter3 = filterDao.save(new Filter(user, "filter3"));
        List<Filter> expected = new ArrayList<>();
        expected.add(filter2);
        expected.add(filter3);
        try {
            List<Filter> result = filterService.listFilter(user.getId());
            assertEquals(expected, result);
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void listFilterMoreUsers() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        User user2 = userDao.save(new User("Juan2", "example2@example.com", adminRole, null));
        Filter filter1 = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));
        Filter filter3 = filterDao.save(new Filter(user, "filter3"));
        Filter filter4 = filterDao.save(new Filter(user2, defaultFilter));
        Filter filter5 = filterDao.save(new Filter(user2, "filter5"));
        List<Filter> expected = new ArrayList<>();
        expected.add(filter2);
        expected.add(filter3);
        try {
            List<Filter> result = filterService.listFilter(user.getId());
            assertEquals(expected, result);
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void listFilterInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter1 = filterDao.save(new Filter(user, defaultFilter));
        try {
            List<Filter> result = filterService.listFilter(NON_EXISTENT_ID);
            assert false;
        } catch (InstanceNotFoundException e) {
            assert true;
        }
    }

    @Test
    void getFilter() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));
        FilterParam filterParam1 = filterParamDao.save(new FilterParam(filter, technology1, true, false));
        FilterParam filterParam2 = filterParamDao.save(new FilterParam(filter, technology2, true, false));
        FilterParam filterParam3 = filterParamDao.save(new FilterParam(filter, technology3, true, false));
        FilterParam filterParam4 = filterParamDao.save(new FilterParam(filter2, technology1, true, false));

        try {
            CompleteFilter result = filterService.getFilter(filter.getId());

            assertEquals(filter, result.getFilter());
            assertEquals(filterParam1.getId(), result.getFilterParamTechnologyTreeList().get(0).getParent().getFilterParamId());
            assertNull(result.getFilterParamTechnologyTreeList().get(1).getParent().getFilterParamId());
            assertEquals(2, result.getFilterParamTechnologyTreeList().size());
            assertEquals(filterParam2.getId(), result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getParent().getFilterParamId());
            assertEquals(filterParam3.getId(), result.getFilterParamTechnologyTreeList().get(0).getChildren().get(1).getParent().getFilterParamId());
            assertEquals(2, result.getFilterParamTechnologyTreeList().get(0).getChildren().size());
            assertNull(result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getChildren().get(0).getParent().getFilterParamId());
            assertEquals(1, result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getChildren().size());
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void getFilterUnnecessary() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));
        FilterParam filterParam1 = filterParamDao.save(new FilterParam(filter, technology1, true, false));
        FilterParam filterParam2 = filterParamDao.save(new FilterParam(filter, technology2, true, false));
        FilterParam filterParam3 = filterParamDao.save(new FilterParam(filter, technology3, true, false));
        FilterParam filterParam4 = filterParamDao.save(new FilterParam(filter2, technology1, true, false));

        try {
            CompleteFilter result = filterService.getFilter(filter.getId());

            assertTrue(result.getFilterParamTechnologyTreeList().get(0).getParent().getUnnecessary());
            assertFalse(result.getFilterParamTechnologyTreeList().get(1).getParent().getUnnecessary());
            assertFalse(result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getParent().getUnnecessary());
            assertFalse(result.getFilterParamTechnologyTreeList().get(0).getChildren().get(1).getParent().getUnnecessary());
            assertFalse(result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getChildren().get(0).getParent().getUnnecessary());
        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void getFilterUnnecessary2() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));

        try {
            filterService.updateFilterParam(user.getId(), null, filter.getId(), technology4.getId(), true, false);
            CompleteFilter result = filterService.getFilter(filter.getId());

            assertTrue(result.getFilterParamTechnologyTreeList().get(0).getParent().getUnnecessary());
            assertTrue(result.getFilterParamTechnologyTreeList().get(0).getChildren().get(0).getParent().getUnnecessary());
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (PermissionException | InvalidAttributesException | DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getFilterInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));

        try {
            CompleteFilter result = filterService.getFilter(NON_EXISTENT_ID);
            assert false;
        } catch (InstanceNotFoundException e) {
            assert true;
        }
    }

    @Test
    void saveFilter() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        String expectedName = "newName";

        try {
            filterService.saveFilter(user.getId(), expectedName);
            Filter savedFilter = filterDao.findById(filter.getId()).orElse(null);

            assert savedFilter != null;
            assertEquals(expectedName, savedFilter.getName());

        } catch (DuplicateInstanceException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void saveFilterCreatesANewDefault() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        String expectedName = "newName";

        try {
            filterService.saveFilter(user.getId(), expectedName);
            Filter newFilter = filterDao.findByName(defaultFilter).orElse(null);

            assertNotNull(newFilter);

        } catch (DuplicateInstanceException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void saveFilterInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));

        String expectedName = "newName";

        try {
            filterService.saveFilter(NON_EXISTENT_ID, expectedName);
            assert false;
        } catch (InstanceNotFoundException e) {
            assert true;
        } catch (DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void saveFilterDuplicateInstance() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));

        String sameName = "Name";
        Filter filter2 = filterDao.save(new Filter(user, sameName));

        try {
            filterService.saveFilter(user.getId(), sameName);
            assert false;
        } catch (InstanceNotFoundException e) {
            assert false;
        } catch (DuplicateInstanceException e) {
            assert true;
        }
    }

    @Test
    void deleteFilter() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        try {
            filterService.deleteFilter(user.getId(), filter2.getId());
            Optional<Filter> result = filterDao.findByName(filter2.getName());

            assert(!result.isPresent());

        } catch (PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteFilterCantDeleteDefault() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        try {
            filterService.deleteFilter(user.getId(), filter.getId());
            Optional<Filter> result = filterDao.findByName(filter.getName());

            assert(result.isPresent());

        } catch (PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteFilterDefaultDeleteFilterParams() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        filterParamDao.save(new FilterParam(filter, technology1, true, false));
        filterParamDao.save(new FilterParam(filter, technology2, true, false));
        filterParamDao.save(new FilterParam(filter, technology3, true, false));


        try {
            filterService.deleteFilter(user.getId(), filter.getId());
            List<FilterParam> filterParamList = filterParamDao.findAllByFilter(filter);

            assert(filterParamList.isEmpty());

        } catch (PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteFilterPermission() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        try {
            filterService.deleteFilter(NON_EXISTENT_ID, filter2.getId());
            assert false;
        } catch (PermissionException e) {

            assert true;

        } catch (InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void deleteFilterInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));

        try {
            filterService.deleteFilter(user.getId(), NON_EXISTENT_ID);
            assert false;
        } catch (PermissionException e) {
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        }
    }

    @Test
    void updateFilterParamAdd() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));

        FilterParam expected = new FilterParam(filter, technology1, true, false);

        try {
            filterService.updateFilterParam(user.getId(), null, filter.getId(), technology1.getId(), expected.isMandatory(), expected.isRecommended());
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assertEquals(expected.getFilter(), result.get(0).getFilter());
            assertEquals(expected.getTechnology(), result.get(0).getTechnology());
            assertEquals(expected.isMandatory(), result.get(0).isMandatory());
            assertEquals(expected.isRecommended(), result.get(0).isRecommended());

        } catch (PermissionException | DuplicateInstanceException | InstanceNotFoundException |
                 InvalidAttributesException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamAdd2() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Filter filter2 = filterDao.save(new Filter(user, "filter2"));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Backend2", null, true));
        Technology technology3 = technologyDao.save(new Technology("Backend1", null, true));

        filterParamDao.save(new FilterParam(filter, technology2, true, false));
        filterParamDao.save(new FilterParam(filter2, technology1, true, false));

        try {
            filterService.updateFilterParam(user.getId(), null, filter.getId(), technology1.getId(), false, true);
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assert (filterParamDao.existsByFilterAndTechnology(filter, technology1));
            assertEquals(2, result.size());

        } catch (PermissionException | DuplicateInstanceException | InstanceNotFoundException |
                 InvalidAttributesException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamAddPermission() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));

        try {
            filterService.updateFilterParam(NON_EXISTENT_ID, null, filter.getId(), technology1.getId(), true, false);
            assert false;
        } catch (PermissionException e) {

            assert true;

        } catch (DuplicateInstanceException | InstanceNotFoundException | InvalidAttributesException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamAddDuplicateInstance() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = new FilterParam(filter, technology1, true, false);
        filterParamDao.save(filterParam);

        try {
            filterService.updateFilterParam(user.getId(), null, filterParam.getFilter().getId(), filterParam.getTechnology().getId(), false, true);
            assert false;
        } catch (DuplicateInstanceException e) {

            assert true;

        } catch (PermissionException | InstanceNotFoundException | InvalidAttributesException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamAddInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));

        try {
            filterService.updateFilterParam(user.getId(), null, NON_EXISTENT_ID, technology1.getId(), true, false);
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (DuplicateInstanceException | PermissionException | InvalidAttributesException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamAddInvalidAttributesTrues() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));

        try {
            filterService.updateFilterParam(user.getId(), null, filter.getId(), technology1.getId(), true, true);
            assert false;
        } catch (InvalidAttributesException e) {

            assert true;

        } catch (DuplicateInstanceException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamInvalidAttributesFalses() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));

        try {
            filterService.updateFilterParam(user.getId(), null, filter.getId(), technology1.getId(), false, false);
            assert false;
        } catch (InvalidAttributesException e) {

            assert true;

        } catch (DuplicateInstanceException | PermissionException | InstanceNotFoundException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamDelete() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        try {
            filterService.updateFilterParam(null, filterParam.getId(), null, null, false, false);
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assert (result.isEmpty());

        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamDeleteAllParents() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        Technology technology2 = technologyDao.save(new Technology("Spring", technology1.getId(), true));
        Technology technology3 = technologyDao.save(new Technology("Maven", technology1.getId(), true));
        Technology technology4 = technologyDao.save(new Technology("SpringBoot", technology2.getId(), true));
        Technology technology5 = technologyDao.save(new Technology("Frontend", null, true));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        FilterParam filterParam1 = filterParamDao.save(new FilterParam(filter, technology1, true, false));
        FilterParam filterParam2 = filterParamDao.save(new FilterParam(filter, technology2, true, false));
        FilterParam filterParam3 = filterParamDao.save(new FilterParam(filter, technology4, true, false));

        try {
            filterService.updateFilterParam(null, filterParam3.getId(), null, null, false, false);
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assert (result.isEmpty());

        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamDeleteInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        try {
            filterService.updateFilterParam(null, NON_EXISTENT_ID, null, null, false, false);
            assert false;
        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {

            assert true;

        }
    }

    @Test
    void updateFilterParam() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        FilterParam expected = new FilterParam(filter, technology1, false, true);

        try {
            filterService.updateFilterParam(null, filterParam.getId(), null, null, expected.isMandatory(), expected.isRecommended());
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assertEquals(expected.isMandatory(), result.get(0).isMandatory());
            assertEquals(expected.isRecommended(), result.get(0).isRecommended());

        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParam2() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, false, true));

        FilterParam expected = new FilterParam(filter, technology1, true, false);

        try {
            filterService.updateFilterParam(null, filterParam.getId(), null, null,expected.isMandatory(), expected.isRecommended());
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assertEquals(expected.isMandatory(), result.get(0).isMandatory());
            assertEquals(expected.isRecommended(), result.get(0).isRecommended());

        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParam3() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        FilterParam expected = new FilterParam(filter, technology1, true, false);

        try {
            filterService.updateFilterParam(null, filterParam.getId(), null, null,expected.isMandatory(), expected.isRecommended());
            List<FilterParam> result = filterParamDao.findAllByFilter(filter);

            assertEquals(expected.isMandatory(), result.get(0).isMandatory());
            assertEquals(expected.isRecommended(), result.get(0).isRecommended());

        } catch (InstanceNotFoundException | InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterParamInstanceNotFound() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        FilterParam expected = new FilterParam(filter, technology1, true, false);

        try {
            filterService.updateFilterParam(null, NON_EXISTENT_ID, null, null, expected.isMandatory(), expected.isRecommended());
            assert false;
        } catch (InstanceNotFoundException e) {

            assert true;

        } catch (InvalidAttributesException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        }
    }

    @Test
    void updateFilterInvalidAttributes() {
        User user = userDao.save(new User("Juan", "example@example.com", adminRole, null));
        Filter filter = filterDao.save(new Filter(user, defaultFilter));
        Technology technology1 = technologyDao.save(new Technology("Backend", null, true));
        FilterParam filterParam = filterParamDao.save(new FilterParam(filter, technology1, true, false));

        FilterParam expected = new FilterParam(filter, technology1, true, true);

        try {
            filterService.updateFilterParam(null, filterParam.getId(), null, null,expected.isMandatory(), expected.isRecommended());
            assert false;
        } catch (InstanceNotFoundException | PermissionException |
                 DuplicateInstanceException e) {
            assert false;
        } catch (InvalidAttributesException e) {

            assert true;

        }
    }
}