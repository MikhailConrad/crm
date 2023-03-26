package ru.sevavto.stock.crm.util.mapper;

import ru.sevavto.stock.crm.model.dto.DefectiveProductPhotoLinkResponse;
import ru.sevavto.stock.crm.model.entity.DefectiveProductPhotoLink;

public class DefectiveProductPhotoLinkToDtoMapper {
    public static DefectiveProductPhotoLinkResponse mapToDto(DefectiveProductPhotoLink photoLink) {
        return DefectiveProductPhotoLinkResponse.builder()
                .id(photoLink.getId())
                .path(photoLink.getPath())
                .build();
    }
}
