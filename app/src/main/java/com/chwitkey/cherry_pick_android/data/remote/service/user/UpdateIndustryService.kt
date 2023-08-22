package com.chwitkey.cherry_pick_android.data.remote.service.user

import com.chwitkey.cherry_pick_android.data.remote.request.user.updateIndustryRequest
import com.chwitkey.cherry_pick_android.data.remote.response.user.UpdateIndustryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdateIndustryService {
    @POST("/api/members/updateIndustry")
    suspend fun putUpdateUserIndustry(
        @Body updateIndustryRequest: updateIndustryRequest
    ): Response<UpdateIndustryResponse>
}