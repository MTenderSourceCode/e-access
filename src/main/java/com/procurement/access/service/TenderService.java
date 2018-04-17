package com.procurement.access.service;

import com.procurement.access.model.dto.bpe.ResponseDto;
import com.procurement.access.model.dto.ocds.TenderStatus;
import com.procurement.access.model.dto.ocds.TenderStatusDetails;
import org.springframework.stereotype.Service;

@Service
public interface TenderService {

    ResponseDto updateStatus(String cpId, String stage, TenderStatus status);

    ResponseDto updateStatusDetails(String cpId, String stage, TenderStatusDetails statusDetails);

    ResponseDto setSuspended(String cpId, String stage, Boolean suspended);
}
