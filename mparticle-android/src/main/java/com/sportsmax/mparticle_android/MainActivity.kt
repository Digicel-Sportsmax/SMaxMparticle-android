package com.sportsmax.mparticle_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mparticle.AttributionError
import com.mparticle.AttributionListener
import com.mparticle.AttributionResult
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject

class MainActivity: AppCompatActivity(), AttributionListener {

    object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.i("BRANCH SDK", referringParams.toString())
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.e("BRANCH SDK", error.message)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        // Branch reinit (in case Activity is already in foreground when Branch link is clicked)
        Branch.sessionBuilder(this).withCallback(branchListener).reInit()
    }

    override fun onResult(p0: AttributionResult) {
        branchListener.onInitFinished(p0.parameters, null)
    }

    override fun onError(p0: AttributionError) {
        branchListener.onInitFinished(null,null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}