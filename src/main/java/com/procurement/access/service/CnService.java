package com.procurement.access.service;

import com.procurement.access.model.dto.bpe.ResponseDto;
import com.procurement.access.model.dto.cn.CnDto;
import org.springframework.stereotype.Service;

@Service
public interface CnService {

    ResponseDto createCn(CnDto cnDto);
}
