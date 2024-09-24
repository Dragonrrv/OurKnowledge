package com.example.ourknowledgebackend.model.entities;

import com.example.ourknowledgebackend.model.FilterParamTechnology;
import com.example.ourknowledgebackend.model.KnownTechnology;
import com.example.ourknowledgebackend.model.UsesTechnology;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtendedTechnologyDao extends CrudRepository<Technology, Long> {

    @Query("SELECT DISTINCT new com.example.ourknowledgebackend.model.KnownTechnology(t.id, t.name, t.parentId, t.relevant, k.id, k.mainSkill, k.likeIt, null) " +
            "FROM Technology t " +
            "LEFT JOIN Knowledge k ON t.id = k.technology.id and k.user.id = :userId " +
            "WHERE t.relevant = true or k.user.id = :userId")
    List<KnownTechnology> findTechnologiesWithKnowledge(@Param("userId") Long userId);

    @Query("SELECT DISTINCT new com.example.ourknowledgebackend.model.UsesTechnology(t.id, t.name, t.parentId, t.relevant, u.id, null) " +
            "FROM Technology t " +
            "LEFT JOIN Uses u ON t.id = u.technology.id and u.project.id = :projectId " +
            "WHERE t.relevant = true or u.project.id = :projectId")
    List<UsesTechnology> findTechnologiesWithUses(@Param("projectId") Long projectId);

    @Query("SELECT DISTINCT new com.example.ourknowledgebackend.model.FilterParamTechnology(t.id, t.name, t.parentId, t.relevant, f.id, f.mandatory, f.recommended, " +
            "CASE WHEN EXISTS (SELECT 1 FROM Technology child " +
            "LEFT JOIN FilterParam childFilter ON child.id = childFilter.technology.id " +
            "WHERE child.parentId = t.id AND childFilter.filter.id = :filterId) THEN true ELSE false END) " +
            "FROM Technology t " +
            "LEFT JOIN FilterParam f ON t.id = f.technology.id and f.filter.id = :filterId " +
            "WHERE t.relevant = true or f.filter.id = :filterId")
    List<FilterParamTechnology> findTechnologiesWithFilter(@Param("filterId") Long filterId);


}
