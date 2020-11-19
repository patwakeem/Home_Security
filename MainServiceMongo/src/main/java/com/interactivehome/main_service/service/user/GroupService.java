package com.interactivehome.main_service.service.user;

import com.interactivehome.main_service.model.user.dto.GroupDto;
import com.interactivehome.main_service.model.user.entity.Group;

import java.util.List;

public interface GroupService {
    void createGroup(GroupDto dto);
    Group modifyGroup(Integer id, GroupDto dto);
    Group getGroupById(Integer id);
    List<Group> getAllGroups();
    Boolean deleteGroup(Integer id);
}
