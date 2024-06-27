package com.example.ourknowledgebackend.api.mappers;

import com.example.ourKnowledge.api.model.AddTechnologyResponseDTO;
import com.example.ourKnowledge.api.model.DeleteTechnologyResponseDTO;
import com.example.ourknowledgebackend.model.entities.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApiTechnologyMapper {

    ApiTechnologyMapper INSTANCE = Mappers.getMapper(ApiTechnologyMapper.class);

    AddTechnologyResponseDTO mapAddResponse(Technology technology);

    DeleteTechnologyResponseDTO mapDeleteResponse(Technology technology);

}
