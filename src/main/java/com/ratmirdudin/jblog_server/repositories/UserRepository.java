package com.ratmirdudin.jblog_server.repositories;

import com.ratmirdudin.jblog_server.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("select u from User u where (u.username = :usernameOrEmail OR u.email = :usernameOrEmail) and u.enabled = true")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    @Query("select u from User u where u.id = :userId and u.enabled = true")
    Optional<User> findUserById(@Param("userId") Long userId);

    @Query(
            "select u from User u where lower(u.username) like concat('%', lower(:search), '%')" +
                    " or lower(u.email) like concat('%', lower(:search), '%')" +
                    " or lower(u.firstname) like concat('%', lower(:search), '%')" +
                    " or lower(u.lastname) like concat('%', lower(:search), '%')"
    )
    Page<User> findAllUsers(@Param("search") String search, Pageable pageable);

    @Modifying
    @Query("update User u set u.enabled = false where u.id = :id and u.immunity = false")
    int banUserById(@Param("id") Long id);

    @Modifying
    @Query("update User u set u.enabled = true where u.id = :id")
    int unbanUserById(@Param("id") Long id);

    @Modifying
    @Query("update User u set u.verified = true, u.verificationCode = null where u.verificationCode = :verificationCode")
    int verifyUserAccount(@Param("verificationCode") String verificationCode);

    @Modifying
    @Query("update User u set u.firstname = :firstName, u.lastname = :lastName where u.id = :userId")
    int updateUserById(@Param("userId") Long userId, @Param("firstName") String firstName, @Param("lastName") String lastName);

}
