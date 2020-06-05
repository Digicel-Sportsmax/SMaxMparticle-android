package com.sportsmax.mparticle_android

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.applicaster.analytics.BaseAnalyticsAgent
import com.applicaster.app.CustomApplication
import com.mparticle.*
import com.mparticle.BuildConfig
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject
import java.util.*

class MParticleAnalyticsAgent : BaseAnalyticsAgent() {

    private val TAG: String = MParticleAnalyticsAgent::class.java.simpleName

    private val MAX_SCREEN_NAME_LONG = 35
    private val MAX_PARAM_NAME_LONG = 40
    private val MAX_PARAM_VALUE_LONG = 100

    // custom events
    private val PLAY_EVENT = "Play_video"
    private val PAUSE_EVENT = "Pause_video"
    private val STOP_EVENT = "Stop_video"

    /**
     * Initialization of your Analytics provider.
     * @param context
     */
    /**
     * Initialization of your Analytics provider.
     * @param context
     */
    override fun initializeAnalyticsAgent(context: Context?) {
        super.initializeAnalyticsAgent(context)
        init(context!!)
    }

    private fun init(context: Context) {
        val customApp = context as? CustomApplication
        Branch.getAutoInstance(context)
        initializeMParticle(context)
    }


    private fun initializeMParticle(context: Context) {
        val packageName = context.packageName
        val options = MParticleOptions.builder(context)

        /*val isDevelopment =
            packageName.contains("local")
                    || packageName.contains("staging")
                    || BuildConfig.DEBUG

        if (isDevelopment) {
            options.credentials(context.getString(com.sportsmax.branch_io.R.string.branch_test_key),
                    context.getString(com.sportsmax.branch_io.R.string.branch_test_secret_key))
            options.environment(MParticle.Environment.Development)
        } else {
            options.credentials(context.getString(com.sportsmax.branch_io.R.string.branch_live_key),
                    context.getString(com.sportsmax.branch_io.R.string.branch_live_secret_key))
            options.environment(MParticle.Environment.Production)
        }*/

        options.credentials(PluginConfigurationHelper.getConfigurationValue(MPARTICLE_API_KEY),
            PluginConfigurationHelper.getConfigurationValue(MPARTICLE_API_SECRET_KEY))
        options.environment(MParticle.Environment.Development)
        options.attributionListener(object: AttributionListener{
            override fun onResult(p0: AttributionResult) {
                TODO("Not yet implemented")
            }

            override fun onError(p0: AttributionError) {
                TODO("Not yet implemented")
            }
        })
        MParticle.start(options.build())
    }

    override fun setParams(params: Map<*, *>) {
        super.setParams(params)
        val stringMap = params as Map<String, String>
        PluginConfigurationHelper.setConfigurationMap(stringMap)
    }

    /**
     * Get the value of the key present in plugin_configurations.json
     * @param params parameters
     * @param key key of the parameter
     * @return correspondent value of the parameter with key `key`
     */
    /**
     * Get the value of the key present in plugin_configurations.json
     * @param params parameters
     * @param key key of the parameter
     * @return correspondent value of the parameter with key `key`
     */
    private fun getValue(
        params: Map<*, *>,
        key: String
    ): String? {
        var returnVal = ""
        if (params[key] != null) {
            returnVal = params[key].toString()
        }
        return returnVal
    }

    /**
     * It is a good practice to make the parameters of the plugin available with this method
     * @return a hash map of the configuration of the plugin
     */
    /**
     * It is a good practice to make the parameters of the plugin available with this method
     * @return a hash map of the configuration of the plugin
     */
    override fun getConfiguration(): Map<String, String>? {
        return super.getConfiguration()
    }

    override fun startTrackingSession(context: Context?) {
        super.startTrackingSession(context)
        Log.wtf("** startTrackingSession name", "is startTrackingSession")
    }

    override fun stopTrackingSession(context: Context?) {
        super.stopTrackingSession(context)
    }

    override fun analyticsSwitch(enabled: Boolean) {
        super.analyticsSwitch(enabled)
    }

    override fun logEvent(eventName: String?) {
        super.logEvent(eventName)
        eventName?.let { it ->
            val event = MPEvent.Builder(it.alphaNumericOnly().cutToMaxLength(MAX_PARAM_NAME_LONG), MParticle.EventType.Other)
                .build()
            MParticle.getInstance()?.logEvent(event)
        }
    }

    /**
     * Log event with extra data
     * @param eventName name of the event logged
     * @param params extra data
     */
    /**
     * Log event with extra data
     * @param eventName name of the event logged
     * @param params extra data
     */
    override fun logEvent(eventName: String?, params: TreeMap<String, String>?) {
        super.logEvent(eventName, params)
        eventName?.let { it ->

            params?.let { params ->
                val newTree = TreeMap<String, String>()
                for ((key, value) in params.entries) {
                    newTree[key.alphaNumericOnly().cutToMaxLength(MAX_PARAM_NAME_LONG)] = value.alphaNumericOnly().cutToMaxLength(MAX_PARAM_VALUE_LONG)
                }
                val event = MPEvent.Builder(it.alphaNumericOnly().cutToMaxLength(MAX_PARAM_NAME_LONG), MParticle.EventType.Other)
                    .customAttributes(newTree)
                    .build()
                MParticle.getInstance()?.logEvent(event)
            }
        }
    }

    override fun startTimedEvent(eventName: String?) {
        super.startTimedEvent(eventName)
        logEvent(eventName)
    }

    override fun startTimedEvent(eventName: String?, params: TreeMap<String, String>) {
        super.startTimedEvent(eventName, params)
        logEvent(eventName, params)
    }

    override fun endTimedEvent(eventName: String?) {
        super.endTimedEvent(eventName)
        logEvent(eventName)
    }

    override fun endTimedEvent(eventName: String?, params: TreeMap<String, String>) {
        super.endTimedEvent(eventName, params)
        logEvent(eventName, params)
    }

    override fun logPlayEvent(currentPosition: Long) {
        super.logPlayEvent(currentPosition)
        logEvent(PLAY_EVENT)
    }

    /**
     * Set the User Id (UUID) on the Analytics Agent
     *
     * @param userId
     */
    /**
     * Set the User Id (UUID) on the Analytics Agent
     *
     * @param userId
     */
    override fun sendUserID(userId: String?) {
        super.sendUserID(userId)
    }

    override fun logVideoEvent(eventName: String?, params: TreeMap<String, String>) {
        super.logVideoEvent(eventName, params)
        logEvent(eventName, params)
    }

    /**
     * Track a when player paused.
     *
     * @param currentPosition
     */
    /**
     * Track a when player paused.
     *
     * @param currentPosition
     */
    override fun logPauseEvent(currentPosition: Long) {
        super.logPauseEvent(currentPosition)
        logEvent(PAUSE_EVENT)
    }

    /**
     * Track when player stop.
     *
     * @param currentPosition
     */
    /**
     * Track when player stop.
     *
     * @param currentPosition
     */
    override fun logStopEvent(currentPosition: Long) {
        super.logStopEvent(currentPosition)
        logEvent(STOP_EVENT)
    }

    override fun setScreenView(activity: Activity?, screenView: String) {
        super.setScreenView(activity, screenView)
        val screenName = if (screenView.contains("ATOM Article", ignoreCase = true)){
            val title = screenView.replace("ATOM Article", "Article").trim()
            title
        }else{
            screenView
        }

        val map = TreeMap<String, String>()
        map["Screen_name"] = screenName.cutToMaxLength(MAX_SCREEN_NAME_LONG)
        MParticle.getInstance()?.logScreen(screenName.cutToMaxLength(MAX_SCREEN_NAME_LONG), null)
        MParticle.getInstance()?.logScreen("screen_visit", map)
    }
}

fun String.cutToMaxLength(maxLength: Int): String{
    return if (this.length > maxLength){
        this.substring(0, maxLength)
    }else{
        this
    }
}

fun String.alphaNumericOnly(): String{
    val regex = Regex("[^A-Za-z0-9 ]")
    return regex.replace(this, "").replace(" ","_")
}