package io.github.trashemail.repositories;

import io.github.trashemail.models.EmailAllocation;
import org.springframework.data.repository.CrudRepository;

public interface EmailAllocationRepository
        extends CrudRepository<EmailAllocation, Long> {
    EmailAllocation findByEmailIdAndIsActiveTrue(String emailId);
}
