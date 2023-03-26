package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.DefectiveProductReportRequest;
import ru.sevavto.stock.crm.model.dto.DefectiveProductRemoteStatementResponse;
import ru.sevavto.stock.crm.service.DefectiveProductService;

import java.util.List;

@RestController
@RequestMapping("defective_product_report")
public class DefectiveProductController {

    private final DefectiveProductService defectiveProductService;

    @Autowired
    public DefectiveProductController(DefectiveProductService defectiveProductService) {
        this.defectiveProductService = defectiveProductService;
    }

    @GetMapping("/{id}")
    public DefectiveProductRemoteStatementResponse getDefectiveProductRemoteStatementById(@PathVariable long id) {
        return defectiveProductService.getDefectiveProductRemoteStatementById(id);
    }

    @GetMapping
    public List<DefectiveProductRemoteStatementResponse> getAllDefectiveProductRemoteStatements() {
        return defectiveProductService.getAllDefectiveProductRemoteStatements();
    }

    @GetMapping(params = "status")
    public List<DefectiveProductRemoteStatementResponse> getDefectiveProductRemoteStatementsByStatus(
            @RequestParam(value = "status") String status) {
        return defectiveProductService.getDefectiveProductRemoteStatementsByStatus(status);
    }

    @PutMapping
    public DefectiveProductRemoteStatementResponse addReportForStatement(DefectiveProductReportRequest report) {
        return defectiveProductService.addReportForStatement(report);
    }
}
