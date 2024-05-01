package tech.task.clearsolutions.service;

import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.Role;

@Service
public interface RoleService {

    Role getByName(String name);

}
