package com.example.post.model.users;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor	// 모든 필드를 파라미터로 받는 생성자
@NoArgsConstructor	// 기본 생성자
@Entity
@Data
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;		// 회원 정보를 구분하는 ID
	
	private String username;// 로그인 아이디
	private String password;// 로그인 패스워드
	private String name;	// 사용자 이름
	
	@Enumerated(EnumType.STRING)
	private GenderType gender;
	
	private LocalDate birthDate;
	private String email;
}
