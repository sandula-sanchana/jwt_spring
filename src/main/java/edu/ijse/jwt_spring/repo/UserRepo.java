package edu.ijse.jwt_spring.repo;

import edu.ijse.jwt_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
