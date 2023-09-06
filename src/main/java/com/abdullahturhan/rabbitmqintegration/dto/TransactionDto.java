package com.abdullahturhan.rabbitmqintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TransactionDto {
    private String transferTo;
    private Double transferredAmount;
    private Long userId;
}
