package guillo.tristan.cms.pres.admin

import guillo.tristan.cms.Model
import guillo.tristan.cms.admin.AdminCallsPresenter

class AdminCallsPresenterImpl(private val model: Model, private val view: AdminCallsPresenter.View) : AdminCallsPresenter {
    override fun addArticle(title: String, text: String) {
        model.addArticle(title, text)
        view.redirect()
    }

    override fun deleteArticle(id: Int) {
        model.deleteCommentsByArticle(id)
        model.deleteArticle(id)
        view.redirect()
    }

    override fun deleteComment(id: Int) {
        model.deleteComment(id)
        view.redirect()
    }
}