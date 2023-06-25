package ru.practicum.shareit.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ModelMapperUtil extends ModelMapper {

    public ModelMapperUtil() {
        this.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }
}
