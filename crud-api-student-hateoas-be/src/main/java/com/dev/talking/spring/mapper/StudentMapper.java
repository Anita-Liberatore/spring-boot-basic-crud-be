package com.dev.talking.spring.mapper;

import com.dev.talking.spring.entity.Student;
import com.dev.talking.spring.model.StudentDto;
import com.dev.talking.spring.model.request.CreateStudentRequest;
import com.dev.talking.spring.model.request.UpdateStudentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Student toEntity(CreateStudentRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateStudentRequest request, @MappingTarget Student student);
}
