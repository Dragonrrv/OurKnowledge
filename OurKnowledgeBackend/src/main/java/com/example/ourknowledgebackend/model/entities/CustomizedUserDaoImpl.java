package com.example.ourknowledgebackend.model.entities;

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
    public Slice<User> find(int page, String keywords, int size) {

        String[] tokens = getTokens(keywords);
        String queryString = "SELECT u FROM User u";

        queryString += " WHERE u.role = 'Developer'";

        if (tokens.length != 0) {

            queryString += " AND";

            for (int i = 0; i<tokens.length-1; i++) {
                queryString += " LOWER(u.name) LIKE LOWER(:token" + i + ") AND";
            }

            queryString += " LOWER(u.name) LIKE LOWER(:token" + (tokens.length-1) + ")";

        }

        queryString += " ORDER BY u.name";

        Query query = entityManager.createQuery(queryString).setFirstResult((page-1)*size).setMaxResults(size+1);

        if (tokens.length != 0) {
            for (int i = 0; i<tokens.length; i++) {
                query.setParameter("token" + i, '%' + tokens[i] + '%');
            }

        }

        List<User> users = query.getResultList();
        boolean hasNext = users.size() == (size+1);

        if (hasNext) {
            users.remove(users.size()-1);
        }

        return new SliceImpl<>(users, PageRequest.of(page, size), hasNext);

    }
}
