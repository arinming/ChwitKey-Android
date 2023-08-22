package com.chwitkey.cherry_pick_android.presentation.ui.mypage.dialog

import android.content.Intent

interface CameraDialogInterface {

    fun onAlbumClick(intent : Intent)
    fun onBasicClick(intent: Intent)
    fun onCancelClick(intent: Intent)
}