package guillo.tristan.cms.pres

import guillo.tristan.cms.ArticleListPresenter
import guillo.tristan.cms.Model

class ArticleListPresenterImpl(val model : Model, val view : ArticleListPresenter.View) : ArticleListPresenter {

    override fun start() {
        val list = model.getArticles()
        view.displayArticleList(list)
    }
}