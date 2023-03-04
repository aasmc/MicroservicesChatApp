package ru.aasmc.chatapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.chatapp.dto.Email;

import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
    List<Email> findAllByStatus(String status, Pageable pageable);
}
