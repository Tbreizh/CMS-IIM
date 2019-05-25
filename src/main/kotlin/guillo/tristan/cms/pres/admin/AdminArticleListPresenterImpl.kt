package guillo.tristan.cms.pres.admin

import guillo.tristan.cms.Model
import guillo.tristan.cms.admin.AdminArticleListPresenter

class AdminArticleListPresenterImpl(private val model: Model, private val view: AdminArticleListPresenter.View) : AdminArticleListPresenter {
    override fun start() {
        val list = model.getArticles()
        view.displayArticleList(list)
    }
}