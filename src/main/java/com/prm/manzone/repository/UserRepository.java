package com.prm.manzone.repository;
import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
