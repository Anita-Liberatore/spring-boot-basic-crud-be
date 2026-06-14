package com.dev.talking.spring.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.dev.talking.spring.controller.StudentController;
import com.dev.talking.spring.model.StudentDto;
import com.dev.talking.spring.model.StudentStatus;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentDto, EntityModel<StudentDto>> {

	@Override
	public EntityModel<StudentDto> toModel(StudentDto student) {
		EntityModel<StudentDto> model = EntityModel.of(
				student,
				linkTo(methodOn(StudentController.class).getStudentById(student.getId())).withSelfRel(),
				linkTo(methodOn(StudentController.class).getStudents(null)).withRel("students"),
				linkTo(methodOn(StudentController.class).updateStudent(student.getId(), null)).withRel("update")
		);

		if (StudentStatus.ACTIVE.equals(student.getStatus())) {
			model.add(linkTo(methodOn(StudentController.class).deactivateStudent(student.getId())).withRel("deactivate"));
		}

		return model;
	}
}
