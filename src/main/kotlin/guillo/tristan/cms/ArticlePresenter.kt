package guillo.tristan.cms

import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment

interface ArticlePresenter {
    fun start(id: Int)

    fun postComment(id: Int, text: String?)

    interface View {
        fun displayArticle(article: Article, comments: List<Comment>)

        fun displayNotFound()
    }
}