package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import com.example.ourknowledgebackend.infrastructure.MapFormatter;
import com.example.ourknowledgebackend.infrastructure.OpenAIService;
import com.example.ourknowledgebackend.model.*;
import com.example.ourknowledgebackend.model.entities.*;
import com.example.ourknowledgebackend.service.Block;
import com.example.ourknowledgebackend.service.KnowledgeService;
import com.example.ourknowledgebackend.service.PermissionChecker;
import com.example.ourknowledgebackend.service.ProjectService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.naming.directory.InvalidAttributesException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ObjectMapper objectMapper;

    private final Common common;

    private final KnowledgeService knowledgeService;

    private final OpenAIService openAIService;

    private final ProjectDao projectDao;

    private final UsesDao usesDao;

    private final ParticipationDao participationDao;

    private final VerificationDao verificationDao;

    private final TechnologyDao technologyDao;

    private final ExtendedTechnologyDao extendedTechnologyDao;

    private final FilterDao filterDao;

    private final UserDao userDao;

    private final KnowledgeDao knowledgeDao;
    private final PermissionChecker permissionChecker;

    @Override
    public Block<ProjectResult> listProjects(int page, int size, String keywords, Long filterId) throws InstanceNotFoundException {
        List<Long> mandatoryList = new ArrayList<>();
        List<Long> recommendedList = new ArrayList<>();
        if(filterId!=null){
            Filter filter = filterDao.findById(filterId).orElseThrow(() -> new InstanceNotFoundException("project.entity.filter", filterId));;
            Map<String, List<Long>> filterParamTechnologiesId = common.getFilterParamTechnologiesId(filter);
            mandatoryList = filterParamTechnologiesId.get("mandatory");
            recommendedList = filterParamTechnologiesId.get("recommended");
        }
        Slice<ProjectResult> slice = projectDao.find(page, size, keywords, mandatoryList, recommendedList);
        return new Block<>(slice.getContent(), slice.hasNext(), page, size);
    }

    @Override
    public ProjectDetails projectDetails(Long id) throws InstanceNotFoundException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        List<UsesTree> usesTreeList = listProjectUses(project);
        List<Participation> participationList = participationDao.findAllByProjectOrderByStartDate(project);
        return new ProjectDetails(project, usesTreeList, participationList);
    }

    public List<UsesTree> listProjectUses(Project project) {
        List<UsesTechnology> usesTechnologyList = extendedTechnologyDao.findTechnologiesWithUses(project.getId());

        for (UsesTechnology uses : usesTechnologyList) {
            List<Verification> verificationList = verificationDao.findAllByUsesId(uses.getUsesId());
            uses.setVerificationList(verificationList.stream()
                    .map(SimpleVerification::new)
                    .collect(Collectors.toList()));
        }

        return common.ListToTreeList(usesTechnologyList, UsesTree::new);
    }

    @Override
    @Transactional
    public Project addProject(String name, String description, String status, String startDate, int size, List<Long> technologiesId) throws DuplicateInstanceException, InstanceNotFoundException {
        if(projectDao.existsByName(name)){
            throw new DuplicateInstanceException("project.entity.project", projectDao.findByName(name).get().getId());
        }
        Project project = new Project(name, description, status, startDate, size);
        projectDao.save(project);
        try{
            updateProjectTechnologies(project, technologiesId, true);
        } catch (InstanceNotFoundException e) {
            projectDao.delete(project);
            throw e;
        }
        return project;
    }

    @Override
    @Transactional
    public void updateProject(Long id, String name, String description, String status, String startDate, int size, Boolean updateTechnologies, List<Long> technologiesId) throws InstanceNotFoundException, DuplicateInstanceException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        if(projectDao.existsByNameAndIdIsNot(name, id)){
            throw new DuplicateInstanceException("project.entity.project", projectDao.findByNameAndIdIsNot(name, id).get().getId());
        }
        if(name != null){
            project.setName(name);
        }
        if(description != null){
            project.setDescription(description);
        }
        if(status != null){
            project.setStatus(status);
        }
        if(startDate != null){
            project.setStartDate(startDate);
        }
        if(size > -1){
            project.setSize(size);
        }
        if(updateTechnologies){
            updateProjectTechnologies(project, technologiesId, true);
        }
        projectDao.save(project);
    }

    @Override
    @Transactional
    public void updateProjectWithFile(Long id, String extension, String fileContent, Boolean useProcessing, Boolean useAI) throws InstanceNotFoundException {
        Project project = projectDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", id));
        List<Long> technologiesId = new ArrayList<>();
        if(useProcessing){
            switch (extension) {
                case "xml" -> technologiesId.addAll(readXmlTechnologies(fileContent).stream()
                        .map(Technology::getId)
                        .toList());
                case "json" -> technologiesId.addAll(readJsonTechnologies(fileContent).stream()
                        .map(Technology::getId)
                        .toList());
                default -> throw new RuntimeException();
            }
        }
        if(useAI){
            List<Long> aiTechnologiesId = getAITechnologiesId(fileContent);
            technologiesId.addAll(aiTechnologiesId);
        }

        updateProjectTechnologies(project, technologiesId, false);

        projectDao.save(project);
    }

    private List<Long> getAITechnologiesId(String fileContent) {
        try {
            Map<String, Object> jsonRequest = objectMapper.readValue(new ClassPathResource("openAIPrompts/FileTechnologies.json").getInputStream(), Map.class);
            List<Technology> technologies = technologyDao.findAllByRelevantTrue();
            Map<String, String> technologiesJson = new HashMap<>();
            technologiesJson.put("technologies", objectMapper.writeValueAsString(technologies));
            jsonRequest = MapFormatter.formatMap(jsonRequest, Map.of("configContent", fileContent, "technologiesContent", technologiesJson.toString()));
            String content = openAIService.askGPT(jsonRequest).get("content").toString();
            JsonNode technologiesNode = objectMapper.readTree(content).get("technologies");
            List<Long> technologiesId = new ArrayList<>();
            technologiesNode.forEach(technology -> technologiesId.add(technology.get("id").longValue()));

            return technologiesId;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<Technology> readXmlTechnologies(String pomContent) {
        try {
            InputSource inputSource = new InputSource(new StringReader(pomContent));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/project/dependencies/dependency";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            ArrayList<String> technologyTokens = new ArrayList<>(getGroupAndArtifactIds(nodeList));

            expression = "/project/build/plugins/plugin";
            nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            technologyTokens.addAll(getGroupAndArtifactIds(nodeList));

            // Eliminar duplicados convirtiendo a un Set y luego de vuelta a una lista
            Set<String> uniqueTokens = new HashSet<>(technologyTokens);
            technologyTokens = new ArrayList<>(uniqueTokens);

            return technologyDao.findAllByRelevantTrueAndNameInIgnoreCase(technologyTokens);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<Technology> readJsonTechnologies(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);

            String projectName = jsonNode.get("name").asText();
            JsonNode dependencies = jsonNode.get("dependencies");

            ArrayList<String> technologyTokens = new ArrayList<>(separateBySymbols(projectName));

            Iterator<String> fieldNames = dependencies.fieldNames();
            while (fieldNames.hasNext()) {
                technologyTokens.addAll(separateBySymbols(fieldNames.next()));
            }
            Set<String> uniqueTokens = new HashSet<>(technologyTokens);
            technologyTokens = new ArrayList<>(uniqueTokens);

            return technologyDao.findAllByRelevantTrueAndNameInIgnoreCase(technologyTokens);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private ArrayList<String> getGroupAndArtifactIds(NodeList nodeList) {
        ArrayList<String> technologyTokens = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element dependency = (Element) nodeList.item(i);
            String groupId = dependency.getElementsByTagName("groupId").item(0).getTextContent();
            String artifactId = dependency.getElementsByTagName("artifactId").item(0).getTextContent();

            technologyTokens.addAll(separateBySymbols(groupId));
            technologyTokens.addAll(separateBySymbols(artifactId));
        }
        return technologyTokens;
    }

    private List<String> separateBySymbols(String text){
        return Arrays.asList(text.toLowerCase().split("[./@-]"));
    }

    private void updateProjectTechnologies(Project project, List<Long> technologiesId, Boolean reset) throws InstanceNotFoundException {

        if(reset){
            usesDao.deleteAllByProject(project);
        }

        for (Long technologyId : technologiesId) {
            try {
                addUses(project.getId(), technologyId);
            } catch (DuplicateInstanceException ignored){}
        }
    }

    @Override
    public void deleteProject(Long projectId) throws InstanceNotFoundException {
        Optional<Project> project =  projectDao.findById(projectId);
        if(project.isEmpty()){
            throw new InstanceNotFoundException("project.entities.technology", projectId);
        }
        projectDao.delete(project.get());
    }

    @Override
    public void addUses(Long projectId, Long technologyId) throws InstanceNotFoundException, DuplicateInstanceException {
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Technology technology = technologyDao.findById(technologyId).orElseThrow(() -> new InstanceNotFoundException("project.entity.technology", technologyId));
        Optional<Uses> uses = usesDao.findByProjectAndTechnology(project, technology);
        if (uses.isPresent()) {
            throw new DuplicateInstanceException("project.entities.uses", uses.get().getId());
        }
        addUsesHierarchy(project, technology);
    }

    private List<Uses> addUsesHierarchy(Project project, Technology technology) {
        List<Uses> usesList = new ArrayList<>();
        if (technology.getParentId() != null) {
            List<Uses> brotherUses = usesDao.findAllByProjectAndTechnologyParentId(project, technology.getParentId());
            if (brotherUses.isEmpty()) {
                Optional<Technology> parentTechnology = technologyDao.findById(technology.getParentId());
                parentTechnology.ifPresent(value -> usesList.addAll(addUsesHierarchy(project, value)));
            }
        }
        if(!usesDao.existsByProjectAndTechnology(project, technology)){
            usesList.add(usesDao.save(new Uses(project, technology)));
        }
        return usesList;
    }

    @Override
    public Uses deleteUses(Long usesId) throws InstanceNotFoundException {
        Uses uses = usesDao.findById(usesId).orElseThrow(() -> new InstanceNotFoundException("project.entity.uses", usesId));
        deleteUsesHierarchy(uses);
        return uses;
    }

    private void deleteUsesHierarchy(Uses uses) {
        List<Uses> childrenUses = usesDao.findAllByProjectAndTechnologyParentId(uses.getProject(), uses.getTechnology().getId());
        usesDao.delete(uses);
        if (!childrenUses.isEmpty()) {
            for(Uses childUses : childrenUses){
                deleteUsesHierarchy(childUses);
            }
        }
    }

    @Override
    public Participation participate(Long userId, Long projectId, String startDate, String endDate) throws InstanceNotFoundException, DuplicateInstanceException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
        Optional<Participation> participation = participationDao.findByUserAndProjectAndEndDateIsNull(user, project);
        if(participation.isPresent()){
            throw new DuplicateInstanceException("project.entity.participation", participation.get().getId());
        }
        return participationDao.save(new Participation(project, user, startDate, endDate));
    }

    @Override
    public Participation updateParticipate(Long userId, Long participationId, String startDate, String endDate) throws InstanceNotFoundException, PermissionException {
        Participation participation = permissionChecker.checkParticipationExistsAndBelongsTo(participationId, userId);
        if(startDate != null) {
            participation.setStartDate(startDate);
        }
        if(endDate != null) {
            participation.setEndDate(endDate);
        }
        return participationDao.save(participation);
    }

    @Override
    public List<Verification> listVerification(Long userId, Long projectId) throws InstanceNotFoundException, InvalidAttributesException {
        if(userId != null) {
            User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
            if(projectId != null) {
                Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
                return verificationDao.findAllByKnowledgeUserAndUsesProject(user, project);
            }
            return verificationDao.findAllByKnowledgeUser(user);
        }
        if(projectId != null) {
            Project project = projectDao.findById(projectId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", projectId));
            return verificationDao.findAllByUsesProject(project);
        }
        throw new InvalidAttributesException();
    }

    @Override
    public Verification addVerification(Long userId, Long usesId) throws InstanceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new InstanceNotFoundException("project.entity.project", userId));
        Uses uses = usesDao.findById(usesId).orElseThrow(() -> new InstanceNotFoundException("project.entity.uses", usesId));
        //crear la fila de verification y de knowledge si es necesario
        Knowledge knowledge = knowledgeDao.findByUserAndTechnology(user, uses.getTechnology()).orElse(null);
        if(knowledge == null) {
            try {
                knowledgeService.addKnowledge(userId, uses.getTechnology().getId(), null, null);
                knowledge = knowledgeDao.findByUserAndTechnology(user, uses.getTechnology()).orElse(null);
            } catch (InvalidAttributesException | DuplicateInstanceException ignored) {}
        }

        assert knowledge != null;
        return addVerificationHierarchy(uses, knowledge);
    }

    private Verification addVerificationHierarchy(Uses uses, Knowledge knowledge) {
        if (knowledge.getTechnology().getParentId() != null) {
            Optional<Verification> parentVerification = verificationDao.findByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyId(uses.getProject(), knowledge.getUser(), knowledge.getTechnology().getParentId());
            if (parentVerification.isEmpty()) {
                addVerificationHierarchy(usesDao.findByProjectAndTechnologyId(uses.getProject(), uses.getTechnology().getParentId()),
                        knowledgeDao.findByUserAndTechnologyId(knowledge.getUser(),knowledge.getTechnology().getParentId()));
            }
        }
        return verificationDao.save(new Verification(knowledge, uses));
    }

    @Override
    public Verification deleteVerification(Long userId, Long verificationId, Boolean deleteKnowledge) throws InstanceNotFoundException, PermissionException {
        Verification verification = permissionChecker.checkVerificationExistsAndBelongTo(verificationId, userId);

        return deleteVerificationHierarchy(verification, deleteKnowledge);
    }

    private Verification deleteVerificationHierarchy(Verification verification, Boolean deleteKnowledge) {
        List<Verification> childrenVerifications = verificationDao.findAllByUsesProjectAndKnowledgeUserAndKnowledgeTechnologyParentId(verification.getUses().getProject(), verification.getKnowledge().getUser(), verification.getKnowledge().getTechnology().getId());
        verificationDao.delete(verification);

        if(deleteKnowledge && !verificationDao.existsByKnowledgeUserAndKnowledgeTechnologyId(verification.getKnowledge().getUser(), verification.getKnowledge().getTechnology().getId())){
            knowledgeDao.delete(verification.getKnowledge());
        }

        if (!childrenVerifications.isEmpty()) {
            for(Verification childVerification : childrenVerifications){
                deleteVerificationHierarchy(childVerification, deleteKnowledge);
            }
        }
        return verification;
    }

}
