package guillo.tristan.cms.pres.admin

import guillo.tristan.cms.Model
import guillo.tristan.cms.admin.AdminArticlePresenter
import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment

class AdminArticlePresenterImpl(private val model: Model, private val view: AdminArticlePresenter.View) :
    AdminArticlePresenter {
    override fun start(id: Int) {
        val article: Article? = model.getArticle(id)
        val comments: List<Comment> = model.getArticleComments(id)

        if (article != null) {
            view.displayArticle(article, comments)
        } else {
            view.displayNotFound()
        }
    }
}