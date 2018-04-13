package com.procurement.access.controller;

import com.procurement.access.model.dto.bpe.ResponseDto;
import com.procurement.access.model.dto.tender.TenderProcessDto;
import com.procurement.access.service.TenderProcessService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/pin")
public class PinController {

    private final TenderProcessService tenderProcessService;

    public PinController(final TenderProcessService tenderProcessService) {
        this.tenderProcessService = tenderProcessService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createPin(@RequestParam("stage") final String stage,
                                                 @RequestParam("country") final String country,
                                                 @RequestParam("pmd") final String pmd,
                                                 @RequestParam("owner") final String owner,
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                 @RequestParam("date") final LocalDateTime dateTime,
                                                 @Valid @RequestBody final TenderProcessDto data) {
        return new ResponseEntity<>(
                tenderProcessService.createPin(stage, country, owner, dateTime, data),
                HttpStatus.CREATED);
    }
}
