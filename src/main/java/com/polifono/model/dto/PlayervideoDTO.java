package com.polifono.model.dto;

public record PlayervideoDTO(
        int id,
        String gameName,
        String levelName,
        int phaseOrder,
        int playerId,
        String playerFirstName,
        String playerLastName,
        String url
) {
}
