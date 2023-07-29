package com.example.cherry_pick_android.presentation.ui.firstKeyword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cherry_pick_android.databinding.FragmentFirstKeywordBinding

class FirstKeywordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFirstKeywordBinding by lazy {
            FragmentFirstKeywordBinding.inflate(layoutInflater)
        }
        return binding.root
    }
}