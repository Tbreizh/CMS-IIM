package guillo.tristan.cms

import com.nhaarman.mockitokotlin2.*
import guillo.tristan.cms.model.Article
import guillo.tristan.cms.pres.ArticleListPresenterImpl
import guillo.tristan.cms.pres.ArticlePresenterImpl
import org.junit.jupiter.api.Test

class PresenterTests {

    @Test
    fun testArticleListPresenter() {

        val list = listOf(Article(1, "Un", null), Article(2, "Deux", null))
        val model = mock<Model>(){
            on { getArticleList() } doReturn list
        }

        val view = mock<ArticleListPresenter.View>()

        val presenter = ArticleListPresenterImpl(model, view)
        presenter.start()

        verify(model).getArticleList()
        verify(view).displayArticleList(list)
        verifyNoMoreInteractions(model, view)
    }

    @Test
    fun testArticlePresenter(){
        val id = 1
        val article  = Article(id, "Un", null)

        val model = mock<Model>(){
            on { getArticle(id) } doReturn article
        }

        val view = mock<ArticlePresenter.View>()
        val presenter = ArticlePresenterImpl(model, view)

        presenter.start(id)

        verify(model).getArticle(id)
        verify(view).displayArticle(article)
        verifyNoMoreInteractions(model, view)

    }

    @Test
    fun testInvalidArticlePresenter(){
        val model = mock<Model>(){
            on { getArticle(any()) } doReturn null
        }

        val view = mock<ArticlePresenter.View>()
        val presenter = ArticlePresenterImpl(model, view)

        presenter.start(42)

        verify(model).getArticle(42)
        verify(view).displayNotFound()
        verifyNoMoreInteractions(model, view)

    }


}
