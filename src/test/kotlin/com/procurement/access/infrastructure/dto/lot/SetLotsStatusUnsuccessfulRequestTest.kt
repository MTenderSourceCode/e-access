package com.procurement.access.infrastructure.dto.lot

import com.procurement.access.infrastructure.AbstractDTOTestBase
import com.procurement.access.infrastructure.handler.v1.model.request.SetLotsStatusUnsuccessfulRequest
import org.junit.jupiter.api.Test

class SetLotsStatusUnsuccessfulRequestTest :
    AbstractDTOTestBase<SetLotsStatusUnsuccessfulRequest>(SetLotsStatusUnsuccessfulRequest::class.java) {
    @Test
    fun test() {
        testBindingAndMapping("json/dto/lot/request/request_lots_set_status_unsuccessful_full.json")
    }
}
