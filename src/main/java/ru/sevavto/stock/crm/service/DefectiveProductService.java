package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.exception.StatusChangingException;
import ru.sevavto.stock.crm.model.dto.DefectiveProductRemoteStatementResponse;
import ru.sevavto.stock.crm.model.dto.DefectiveProductReportRequest;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatement;
import ru.sevavto.stock.crm.model.entity.DefectiveProductRemoteStatementStatus;
import ru.sevavto.stock.crm.repository.DefectiveProductRemoteStatementRepository;
import ru.sevavto.stock.crm.util.mapper.DefectiveProductRemoteStatementToDtoMapper;

import java.time.LocalDate;
import java.util.List;

@Service
public class DefectiveProductService {

    private final DefectiveProductRemoteStatementRepository defectiveProductRemoteStatementRepository;

    @Autowired
    public DefectiveProductService(DefectiveProductRemoteStatementRepository defectiveProductRemoteStatementRepository){
        this.defectiveProductRemoteStatementRepository = defectiveProductRemoteStatementRepository;
    }

    public DefectiveProductRemoteStatementResponse getDefectiveProductRemoteStatementById(long id) {
        return DefectiveProductRemoteStatementToDtoMapper.mapToDto(
                        defectiveProductRemoteStatementRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Не найдена заявка на отбраковку с таким id"))
        );
    }

    public List<DefectiveProductRemoteStatementResponse> getAllDefectiveProductRemoteStatements() {
        return defectiveProductRemoteStatementRepository.findAll()
                .stream()
                .map(DefectiveProductRemoteStatementToDtoMapper::mapToDto)
                .toList();
    }

    public List<DefectiveProductRemoteStatementResponse> getDefectiveProductRemoteStatementsByStatus(String status) {
        return defectiveProductRemoteStatementRepository
                .findAllByStatus(DefectiveProductRemoteStatementStatus.valueOf(status))
                        .stream()
                        .map(DefectiveProductRemoteStatementToDtoMapper::mapToDto)
                        .toList();
    }

    public DefectiveProductRemoteStatementResponse addReportForStatement(DefectiveProductReportRequest report) {
        DefectiveProductRemoteStatement statement =
                defectiveProductRemoteStatementRepository.findById(report.getStatementId())
                        .orElseThrow(() -> new NotFoundException("Не найдена заявка на отбраковку с таким id"));
        if(!statement.getStatus().equals(DefectiveProductRemoteStatementStatus.NEW)) {
            throw new StatusChangingException("Нельзя обработать заявку. Заявка уже имеет заключение.");
        }
        statement.setDateOfReport(LocalDate.now());
        statement.setConclusion(report.getConclusion());
        statement.setStatus(report.getStatus());
        return DefectiveProductRemoteStatementToDtoMapper.mapToDto(defectiveProductRemoteStatementRepository.save(statement));
    }
}
