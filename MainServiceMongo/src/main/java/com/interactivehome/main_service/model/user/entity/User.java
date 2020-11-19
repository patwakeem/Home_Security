package com.interactivehome.main_service.model.user.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.user.dto.UserDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "user")
public class User {
    @Id
    @Field("_id")
    private Integer _id;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    private String email;

    private String username;

    private String password;

    @Field("registration_utc")
    private Date registrationUtc;

    @Field("updated_utc")
    private Date updatedUtc;

    public void updateUserFromDto(UserDto dto) {
        firstName = dto.firstName;
        lastName = dto.lastName;
        email = dto.email;
        username = dto.username;
        password = dto.password;
        updatedUtc = new Date(System.currentTimeMillis());
    }

    public void createUserFromDto(Integer id, UserDto dto) {
        _id = id;
        firstName = dto.firstName;
        lastName = dto.lastName;
        email = dto.email;
        username = dto.username;
        password = dto.password;
        registrationUtc = new Date(System.currentTimeMillis());
    }
}
