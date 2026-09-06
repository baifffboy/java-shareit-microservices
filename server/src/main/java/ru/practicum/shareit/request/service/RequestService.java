package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateRequest;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {

    RequestDto create(Long userId, CreateRequest createRequest);

    Collection<RequestDto> getUserRequests(Long userId);

    Collection<RequestDto> getOtherRequests(Long userId);

    RequestDto getRequestById(Long userId, Long requestId);
}
