package com.tarea.tarea1.demo.logic.entity.rol;

import org.springframework.data.repository.CrudRepository;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
