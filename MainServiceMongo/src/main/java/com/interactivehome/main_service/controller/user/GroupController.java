package com.interactivehome.main_service.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interactivehome.main_service.model.user.dto.GroupDto;
import com.interactivehome.main_service.model.user.entity.Group;
import com.interactivehome.main_service.service.user.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class GroupController {
    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/group")
    public ResponseEntity<String> CreateGroup(@RequestBody GroupDto dto) {
        groupService.createGroup(dto);
        String body = "";
        try {
            ObjectMapper map = new ObjectMapper();
            body = map.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            System.out.println("Json Processing Exception: " + e.getMessage());
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(body);
    }

    @GetMapping("/group/{id}")
    public Group GetGroup(@PathVariable Integer id) {
        return groupService.getGroupById(id);
    }

    @PutMapping("/group/{id}")
    public Group ModifyGroup(@PathVariable Integer id,
                           @RequestBody GroupDto dto) {
        return groupService.modifyGroup(id, dto);
    }

    @GetMapping("/group")
    public List<Group> GetAllGroups() {
        return groupService.getAllGroups();
    }

    @DeleteMapping("/group/{id}")
    public Boolean DeleteGroup(@PathVariable Integer id) {
        return groupService.deleteGroup(id);
    }
}
