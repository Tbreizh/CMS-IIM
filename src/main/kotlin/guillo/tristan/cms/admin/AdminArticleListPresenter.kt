package guillo.tristan.cms.admin

import guillo.tristan.cms.model.Article

interface AdminArticleListPresenter {
    fun start()

    interface View {
        fun displayArticleList(list: List<Article>)
    }
}