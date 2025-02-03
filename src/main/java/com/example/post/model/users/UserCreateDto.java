package com.example.post.model.users;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
//회원 가입을 받기 위한 전용 클래스
public class UserCreateDto {
	@NotBlank
	@Size(min = 4, max = 20)
	private String username;
	private String password;
	private String name;
	private GenderType gender;
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private String email;
}
