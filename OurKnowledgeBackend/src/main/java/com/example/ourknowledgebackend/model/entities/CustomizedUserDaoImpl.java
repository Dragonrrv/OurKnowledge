package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.UserResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CustomizedUserDaoImpl implements CustomizedUserDao {
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
    public Slice<UserResult> find(int page, int size, String keywords, List<Long> mandatoryList, List<Long> recommendedList) {

        String[] tokens = getTokens(keywords);
        String queryString = "SELECT DISTINCT new com.example.ourknowledgebackend.model.UserResult(u.id, u.name, u.email, u.role, u.startDate," +
                " (SELECT COUNT(DISTINCT k2.technology.id) FROM Knowledge k2 WHERE k2.user = u AND k2.technology.id IN :recommendedList) AS recommendedCount)" +
                " FROM User u left join Knowledge k on u = k.user";

        queryString += " WHERE u.role = 'Developer'";

        if (tokens.length > 0 || !mandatoryList.isEmpty()) {
            queryString += " AND ";
        }

        if (tokens.length != 0) {

            for (int i = 0; i<tokens.length-1; i++) {
                queryString += " LOWER(u.name) LIKE LOWER(:token" + i + ") AND";
            }
            queryString += " LOWER(u.name) LIKE LOWER(:token" + (tokens.length-1) + ")";

        }

        if (tokens.length > 0 && !mandatoryList.isEmpty()) {
            queryString += " AND ";
        }

        if(!mandatoryList.isEmpty()){
            queryString += "technology.id in (:mandatoryList)";
        }

        queryString += " GROUP BY u.id ";

        if(!mandatoryList.isEmpty()){
            queryString += "HAVING COUNT(DISTINCT k.technology.id) = :mandatoryCount ";
        }

        queryString += " ORDER BY recommendedCount DESC, u.name";

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

        List<UserResult> users = query.getResultList();
        boolean hasNext = users.size() == (size+1);

        if (hasNext) {
            users.remove(users.size()-1);
        }

        return new SliceImpl<>(users, PageRequest.of(page, size), hasNext);

    }
}
