package io.github.trashemail.repositories;

import io.github.trashemail.models.EmailCounter;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface EmailCounterRepository
        extends CrudRepository<EmailCounter, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE EmailCounter ec set ec.count = ec.count + 1 where " +
            "ec.id = 1")
    public void updateCount();

    @Query(value = "select ec.count from EmailCounter ec where ec.id=1")
    public long count();
}
