package ru.sevavto.stock.crm.model.dto.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRefreshRequest {

    private String refreshToken;
}
