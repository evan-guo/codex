package com.codex.admin;

import com.codex.api.scan.CodexScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@CodexScan
@SpringBootApplication
public class CodexAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodexAdminApplication.class, args);
	}
}
