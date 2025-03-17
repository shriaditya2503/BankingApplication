package com.project.BankingApplication.dto;

public class ApiResponseDto {
    private String message;
    private int status;

    public ApiResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}

