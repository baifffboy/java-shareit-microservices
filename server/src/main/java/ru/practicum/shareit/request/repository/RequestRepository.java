package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestUserIdOrderByCreatedDesc(Long userId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requestUser.id != :userId ORDER BY r.created DESC")
    List<ItemRequest> findRequestsByOtherUsers(@Param("userId") Long userId);
}
