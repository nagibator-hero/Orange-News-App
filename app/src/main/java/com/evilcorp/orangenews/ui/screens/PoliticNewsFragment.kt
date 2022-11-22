package com.evilcorp.orangenews.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.evilcorp.orangenews.R
import com.evilcorp.orangenews.data.models.News
import com.evilcorp.orangenews.data.viewmodels.AllNewsViewModel
import com.evilcorp.orangenews.databinding.FragmentAllNewsBinding
import com.evilcorp.orangenews.ui.adapters.PoliticNewsAdapter

class PoliticNewsFragment : Fragment() {

    lateinit var binding: FragmentAllNewsBinding
    lateinit var viewModel: AllNewsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_all_news, container, false)
        binding = DataBindingUtil.bind(v.root)!!
        viewModel = AllNewsViewModel()
        return v.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvNews = binding.rvNews
        val rvAdapter = PoliticNewsAdapter(view.context)
        rvNews.adapter = rvAdapter
        rvNews.layoutManager = LinearLayoutManager(requireContext())
        rvNews.setHasFixedSize(true)
        viewModel.getNewsFromApi()

        var news: MutableList<News> = mutableListOf()
        viewModel.news.observe(viewLifecycleOwner) {
            val newsList = it.articles
            for (article in newsList) {
                news.add(News(article.articleTitle, article.image.imageUrl, article.articleText))
            }
            rvAdapter.setList(news)
            rvAdapter.notifyDataSetChanged()
        }
    }

}