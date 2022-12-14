package com.evilcorp.orangenews.ui.screens

import android.content.Context
import android.content.SharedPreferences
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
import com.evilcorp.orangenews.data.utils.NewsDecoder
import com.evilcorp.orangenews.data.viewmodels.AllNewsViewModel
import com.evilcorp.orangenews.databinding.FragmentAllNews2Binding
import com.evilcorp.orangenews.ui.adapters.NewsAdapter

class NewsFragment(val category: String) : Fragment() {

    private lateinit var binding: FragmentAllNews2Binding
    private lateinit var viewModel: AllNewsViewModel
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_all_news2, container, false)
        binding = DataBindingUtil.bind(v.root)!!
        viewModel = AllNewsViewModel()
        prefs = requireActivity().getSharedPreferences("NEWS_MAIN", Context.MODE_PRIVATE)
        return v.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRv = binding.allNewsRv
        val rvAdapter = NewsAdapter(view.context)
        newsRv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        setupSwipeRefreshView(rvAdapter)

        val newsList: List<News> = NewsDecoder.formatNews(prefs, category)
        rvAdapter.setList(newsList)
        rvAdapter.notifyDataSetChanged()

    }

    private fun setupSwipeRefreshView(adapter: NewsAdapter) {

        binding.allnewsSwipeLayout.setOnRefreshListener {
            viewModel.getNewsFromApi()
            viewModel.news.observe(viewLifecycleOwner) {
                NewsDecoder.saveNews(it, prefs)
                val news: List<News> = NewsDecoder.formatNews(prefs, category)
                adapter.setList(news)
                adapter.notifyDataSetChanged()
                binding.allnewsSwipeLayout.isRefreshing = false
            }
        }
    }

}