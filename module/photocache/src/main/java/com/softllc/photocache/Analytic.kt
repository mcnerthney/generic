package com.softllc.photocache

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Analytic {
     enum class Event{
          ADD_PHOTO,
          SCALE_PHOTO,
          ROTATE_PHOTO,
          DELETE_PHOTO,
          DELETE_ALL,
          IMPORT_PHOTO,
          NEXT_PHOTO,
          PREV_PHOTO
     }
    fun Context.LogAnalyticEvent(event: Analytic.Event, eventContext: String? = null) {
        var bundle : Bundle? = null
        if ( eventContext != null ) {
            bundle = Bundle()
            bundle.putString("app_context", eventContext)
        }
        FirebaseAnalytics.getInstance(this).logEvent(event.name.toLowerCase(),bundle)
    }
}

