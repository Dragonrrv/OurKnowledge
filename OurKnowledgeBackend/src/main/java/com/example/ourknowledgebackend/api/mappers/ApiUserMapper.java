package com.example.ourknowledgebackend.api.mappers;

import com.example.ourKnowledge.api.model.AddTechnologyResponseDTO;
import com.example.ourKnowledge.api.model.DeleteTechnologyResponseDTO;
import com.example.ourKnowledge.api.model.ProfileResponseDTO;
import com.example.ourknowledgebackend.model.UserProfile;
import com.example.ourknowledgebackend.model.entities.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApiUserMapper {

    ApiUserMapper INSTANCE = Mappers.getMapper(ApiUserMapper.class);

    //ProfileResponseDTO mapProfileResponse(UserProfile profile);


}
