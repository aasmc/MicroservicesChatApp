package ru.aasmc.chatapp.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.chatapp.dto.Email;

import java.time.Instant;
import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
    /**
     * This query prevents different instances of the app from acquiring the same
     * rows of emails from DB. FOR UPDATE ensures it. SKIP LOCKED allows rows that
     * are not locked to be grabbed by other instances.
     */
    @Query("UPDATE emails SET lock_until = :lockUntil WHERE id in (SELECT e.id FROM emails e " +
            "WHERE e.status = 'NEW' and (e.lock_until is null or e.lock_until < now()) " +
            "LIMIT :limit FOR UPDATE SKIP LOCKED) RETURNING *")
    List<Email> findAllByStatus(String status, Instant lockUntil, int limit);
}
