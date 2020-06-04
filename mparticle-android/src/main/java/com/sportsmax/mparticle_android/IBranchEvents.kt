package com.sportsmax.mparticle_android

import org.json.JSONObject

interface IBranchEvents {
    fun onBranchInitialised(params: JSONObject?)
}