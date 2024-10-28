package com.example.wherenextbackend.services;

import com.example.wherenextbackend.customException.EmptyOptionalException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversionService {

    public <T> T getEntityFromOptional(Optional<T> optional) throws EmptyOptionalException {
        if (optional.isEmpty()) {
            throw new EmptyOptionalException("Unexpected empty Optional");
        }
        return optional.get();
    }


}