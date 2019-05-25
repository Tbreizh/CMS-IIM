package guillo.tristan.cms

import guillo.tristan.cms.model.Article

interface ArticleListPresenter {
    fun start()

    interface View {
        fun displayArticleList(list: List<Article>)
    }
}