package pl.bykowski.rectangleapp.services;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pl.bykowski.rectangleapp.model.Role;
import pl.bykowski.rectangleapp.repositories.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = Objects.requireNonNull(roleRepository, "roleRepository must be not null");
    }

    public Role findByName(String name){
        Optional<Role> roleOpt = roleRepository.findByName(name);
        return roleOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("Unable to get Role name : [%s]", name)));
    }
}
