package com.jokes.repository;

import com.jokes.domain.JokesUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JokesUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JokesUserRepository extends JpaRepository<JokesUser, Long> {
    @Query("select jokesUser from JokesUser jokesUser where jokesUser.internalUser.login = ?#{principal.username}")
    List<JokesUser> findByInternalUserIsCurrentUser();
}
