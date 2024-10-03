package com.example.ourknowledgebackend.model.entities;

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
    public Slice<Project> find(int page, int size, String keywords,  List<Long> mandatoryList,  List<Long> recommendedList) {

        String[] tokens = getTokens(keywords);
        String queryString = "SELECT DISTINCT p FROM Project p";

        if (!mandatoryList.isEmpty()) {
            queryString += " join Uses u on p = u.project";
        }

        if (tokens.length > 0 || !mandatoryList.isEmpty()) {
            queryString += " WHERE ";
        }

        if (tokens.length != 0) {

            for (int i = 0; i<tokens.length-1; i++) {
                queryString += "LOWER(p.name) LIKE LOWER(:token" + i + ") AND ";
            }

            queryString += "LOWER(p.name) LIKE LOWER(:token" + (tokens.length-1) + ")";

        }

        if (tokens.length > 0 && !mandatoryList.isEmpty()) {
            queryString += " AND ";
        }

        if(!mandatoryList.isEmpty()){

            queryString += "technology.id in (:techIdList)" +
                    "GROUP BY p.id " +
                    "HAVING COUNT(DISTINCT u.technology.id) = :techCount ";


        }

        queryString += " ORDER BY p.startDate DESC";

        Query query = entityManager.createQuery(queryString).setFirstResult((page-1)*size).setMaxResults(size+1);

        if (tokens.length != 0) {
            for (int i = 0; i<tokens.length; i++) {
                query.setParameter("token" + i, '%' + tokens[i] + '%');
            }
        }

        if (!mandatoryList.isEmpty()) {
            query.setParameter("techIdList" , mandatoryList );
            query.setParameter("techCount" , mandatoryList.size() );
        }

        List<Project> products = query.getResultList();
        boolean hasNext = products.size() == (size+1);

        if (hasNext) {
            products.remove(products.size()-1);
        }

        return new SliceImpl<>(products, PageRequest.of(page, size), hasNext);

    }
}
