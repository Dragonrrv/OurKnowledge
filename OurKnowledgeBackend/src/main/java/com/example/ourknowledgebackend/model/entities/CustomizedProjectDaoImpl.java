package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.ProjectResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CustomizedProjectDaoImpl implements CustomizedProjectDao {
    @PersistenceContext
    private EntityManager entityManager;

    private String[] getTokens(String keywords) {

        if (keywords == null || keywords.length() == 0) {
            return new String[0];
        } else {
            return keywords.split("\\s");
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Slice<ProjectResult> find(int page, int size, String keywords, List<Long> mandatoryList, List<Long> recommendedList) {

        String[] tokens = getTokens(keywords);
        String queryString = "SELECT DISTINCT new com.example.ourknowledgebackend.model.ProjectResult(p.id, p.name, p.description, p.status, p.startDate, p.size," +
                " (SELECT COUNT(DISTINCT u2.technology.id) FROM Uses u2 WHERE u2.project = p AND u2.technology.id IN :recommendedList) AS recommendedCount)" +
                " FROM Project p left join Uses u on p = u.project";

        if (tokens.length > 0 || !mandatoryList.isEmpty()) {
            queryString += " WHERE ";
        }

        if (tokens.length > 0) {

            for (int i = 0; i<tokens.length-1; i++) {
                queryString += "LOWER(p.name) LIKE LOWER(:token" + i + ") AND ";
            }
            queryString += "LOWER(p.name) LIKE LOWER(:token" + (tokens.length-1) + ")";
        }

        if (tokens.length > 0 && !mandatoryList.isEmpty()) {
            queryString += " AND ";
        }

        if(!mandatoryList.isEmpty()){
            queryString += "technology.id in (:mandatoryList)";
        }

        queryString += " GROUP BY p.id ";

        if(!mandatoryList.isEmpty()){
            queryString += "HAVING COUNT(DISTINCT u.technology.id) = :mandatoryCount ";
        }

        queryString += " ORDER BY recommendedCount DESC, p.startDate DESC";

        Query query = entityManager.createQuery(queryString).setFirstResult((page-1)*size).setMaxResults(size+1);

        query.setParameter("recommendedList" , recommendedList );

        if (tokens.length != 0) {
            for (int i = 0; i<tokens.length; i++) {
                query.setParameter("token" + i, '%' + tokens[i] + '%');
            }
        }

        if (!mandatoryList.isEmpty()) {
            query.setParameter("mandatoryList" , mandatoryList );
            query.setParameter("mandatoryCount" , mandatoryList.size() );
        }

        List<ProjectResult> products = query.getResultList();
        boolean hasNext = products.size() == (size+1);

        if (hasNext) {
            products.remove(products.size()-1);
        }

        return new SliceImpl<>(products, PageRequest.of(page, size), hasNext);

    }
}
