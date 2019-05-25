package guillo.tristan.cms.admin

import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment

interface AdminArticlePresenter {
    fun start(id: Int)

    interface View {
        fun displayArticle(article: Article, comments: List<Comment>)

        fun displayNotFound()
    }
}