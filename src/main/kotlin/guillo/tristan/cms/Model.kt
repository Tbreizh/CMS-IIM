package guillo.tristan.cms

import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment
import guillo.tristan.cms.model.User

interface Model {

    fun getArticles(): List<Article>

    fun getArticle(id: Int): Article?

    fun deleteArticle(id: Int)

    fun deleteCommentsByArticle(id: Int)

    fun deleteComment(id: Int)

    fun getArticleComments(id: Int): List<Comment>

    fun addArticle(title: String, text: String)

    fun submitArticleComment(id: Int, text: String)

    fun getUserByUsername(username: String): User?
}