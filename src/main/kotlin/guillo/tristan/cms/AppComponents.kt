package guillo.tristan.cms

import guillo.tristan.cms.admin.AdminArticleListPresenter
import guillo.tristan.cms.admin.AdminCallsPresenter
import guillo.tristan.cms.pres.ArticlePresenterImpl
import guillo.tristan.cms.pres.ArticleListPresenterImpl
import guillo.tristan.cms.pres.admin.AdminArticleListPresenterImpl
import guillo.tristan.cms.pres.admin.AdminCallsPresenterImpl
import guillo.tristan.cms.services.LoginService

class AppComponents(mysqlUrl: String, mysqlUser: String, mysqlPassword: String) {

    private val pool = ConnectionPool(mysqlUrl, mysqlUser, mysqlPassword)

    private val model = MysqlModel(getPool())

    private fun getPool(): ConnectionPool = pool

    private fun getModel(): Model = model

    fun getArticleListPresenter(view: ArticleListPresenter.View): ArticleListPresenter =
        ArticleListPresenterImpl(getModel(), view)

    fun getArticlePresenter(view: ArticlePresenter.View): ArticlePresenter =
        ArticlePresenterImpl(getModel(), view)

    fun getAdminArticleListPresenter(view: AdminArticleListPresenter.View): AdminArticleListPresenter = AdminArticleListPresenterImpl(getModel(), view)

    fun getAdminCallsPresenter(view: AdminCallsPresenter.View): AdminCallsPresenter = AdminCallsPresenterImpl(getModel(), view)

    val authService = LoginService(getModel())
}