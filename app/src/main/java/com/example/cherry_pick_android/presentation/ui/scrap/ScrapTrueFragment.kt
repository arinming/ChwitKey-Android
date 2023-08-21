package com.example.cherry_pick_android.presentation.ui.scrap

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.data.remote.service.article.ArticleScrapService
import com.example.cherry_pick_android.data.remote.service.article.ArticleUnlikeService
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
                val scrapData = response.data!!.content?.toMutableList()

                binding.rvScrapNewsList.adapter = ScrapAdapter(this@ScrapTrueFragment, scrapData)
                binding.tvScrapCount.text = scrapData?.size.toString()

                // 스크랩한 기사가 없을 경우 나타내는 화면
                if(scrapData?.isEmpty() == true){
                    binding.llNoneScrap.visibility = View.VISIBLE
                }
                // 스크랩한 기사가 있을 경우 다시 전환
                else{
                    binding.llNoneScrap.visibility = View.GONE
                }
                binding.lottieDotLoading.visibility = View.GONE
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