package com.procurement.access.infrastructure.dto.fe.check

import com.procurement.access.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class CheckFEDataRequestTest : AbstractDTOTestBase<CheckFEDataRequest>(CheckFEDataRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/fe/check/request/request_check_fe_data_full.json")
    }

    @Test
    fun required_1() {
        testBindingAndMapping("json/dto/fe/check/request/request_check_fe_data_required_1.json")
    }

    @Test
    fun required_2() {
        testBindingAndMapping("json/dto/fe/check/request/request_check_fe_data_required_2.json")
    }
}
