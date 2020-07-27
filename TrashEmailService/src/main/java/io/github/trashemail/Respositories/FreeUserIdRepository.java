package io.github.trashemail.Respositories;

import io.github.trashemail.models.FreeUserId;
import org.springframework.data.repository.CrudRepository;

public interface FreeUserIdRepository
        extends CrudRepository<FreeUserId, Integer> {
    public FreeUserId findTopByOrderByIdAsc();
}
