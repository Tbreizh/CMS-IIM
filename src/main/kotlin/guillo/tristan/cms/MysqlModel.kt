package guillo.tristan.cms

import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment
import guillo.tristan.cms.model.User

class MysqlModel (val pool: ConnectionPool) : Model {

    override fun getArticles(): List<Article> {
        val list = ArrayList<Article>()

        pool.useConnection { connection ->
            connection.prepareStatement("SELECT id, title, text FROM articles").use{ stmt ->
                stmt.executeQuery().use { result ->
                    while(result.next()){
                        list += Article(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("text")
                        )
                    }
                }
            }
        }
        return list
    }

    override fun getArticle(id: Int): Article?{
        pool.useConnection { connection ->

            connection.prepareStatement("SELECT * FROM articles where id = ?").use {stmt2 ->
                stmt2.setInt(1, id)

                stmt2.executeQuery().use { result2 ->
                    if (result2.next()){
                        return Article(
                            result2.getInt("id"),
                            result2.getString("title"),
                            result2.getString("text")
                        )
                    }
                }
            }
        }
        return null
    }

    override fun submitArticleComment(id: Int, text: String) {
        pool.useConnection { connection ->
            connection.prepareStatement("INSERT INTO commentaires (idarticle, text) VALUES (?, ?)").use {stmt ->
                stmt.setInt(1, id)
                stmt.setString(2, text)
                stmt.executeUpdate()
            }
        }
    }

    override fun addArticle(title: String, text: String) {
        pool.useConnection { connection ->
            connection.prepareStatement("INSERT INTO articles (title, text) VALUES (?, ?)").use {stmt ->
                stmt.setString(1, title)
                stmt.setString(2, text)
                stmt.execute()
            }
        }
    }

    override fun deleteArticle(id: Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("DELETE FROM articles WHERE id = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.execute()
            }
        }
    }

    override fun deleteCommentsByArticle(id: Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("DELETE FROM commentaires WHERE idarticle = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.execute()
            }
        }
    }

    override fun deleteComment(id: Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("DELETE FROM commentaires WHERE id = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.execute()
            }
        }
    }

    override fun getArticleComments(id: Int): List<Comment> {
        val comments = ArrayList<Comment>()

        pool.useConnection { connection ->
            connection.prepareStatement("SELECT * FROM commentaires WHERE idarticle = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use {results ->
                    while (results.next()) {
                        comments += Comment (
                            results.getInt("id"),
                            results.getInt("idarticle"),
                            results.getString("text")
                        )
                    }
                }
            }
        }

        return comments
    }

    override fun getUserByUsername(username: String): User? {
        pool.useConnection { connection ->
            connection.prepareStatement("SELECT * FROM users WHERE username = ? AND isAdmin = 1").use {stmt ->
                stmt.setString(1, username)
                stmt.executeQuery().use {result ->
                    if (result.next()) {
                        return User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("password")
                        )
                    }
                }
            }
        }
        return null
    }

}