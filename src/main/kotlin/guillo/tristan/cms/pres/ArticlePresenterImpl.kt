package guillo.tristan.cms.pres

import guillo.tristan.cms.ArticlePresenter
import guillo.tristan.cms.Model
import guillo.tristan.cms.model.Comment

class ArticlePresenterImpl(val model : Model, val view : ArticlePresenter.View) :  ArticlePresenter{
    override fun start(id :Int) {
        val article = model.getArticle(id)
        val comments: List<Comment> = model.getArticleComments(id)

        if (article != null) {
            view.displayArticle(article, comments)
        }
        else{
            view.displayNotFound()
        }
    }

    override fun postComment(id: Int, text: String?) {
        if (text != null) {
            model.submitArticleComment(id, text)
            start(id)
        }
    }
}