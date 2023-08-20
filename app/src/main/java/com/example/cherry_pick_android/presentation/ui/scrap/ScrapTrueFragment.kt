package com.example.cherry_pick_android.presentation.ui.scrap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.data.data.Article
import com.example.cherry_pick_android.data.remote.response.article.ScrapData
import com.example.cherry_pick_android.data.remote.service.article.ArticleScrapService
import com.example.cherry_pick_android.data.remote.service.article.ArticleUnlikeService
import com.example.cherry_pick_android.data.remote.service.user.DeleteUserService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.databinding.FragmentScrapTrueBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.adapter.ScrapAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScrapTrueFragment : Fragment(), DeleteScrapListener {
    private var _binding: FragmentScrapTrueBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userInfoService: UserInfoService
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var articleScrapService: ArticleScrapService
    @Inject
    lateinit var articleUnlikeService: ArticleUnlikeService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrapTrueBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            binding.tvScrapName.text = userDataRepository.getUserData().name
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScrapList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initScrapList() {
        lifecycleScope.launch {
            val response = articleScrapService.getScrap().body()
            val statusCode = response?.statusCode

            if(statusCode == 200){
                val scrapData = response.data!!.toMutableList()

                binding.rvScrapNewsList.adapter = ScrapAdapter(this@ScrapTrueFragment, scrapData)
                binding.tvScrapCount.text = scrapData.size.toString()
            }
        }
    }

    override fun onResume() {
        initScrapList()
        super.onResume()
    }

    override fun onDelete(id: Int) {
        lifecycleScope.launch {
            val response = articleUnlikeService.deleteArticleUnlike(id, "scrap").body()
            val statusCode = response?.statusCode

            if(statusCode != 200){
                Toast.makeText(requireContext(), "통신 오류:$statusCode", Toast.LENGTH_SHORT).show()
            }
            initScrapList()
        }
    }
}