package guillo.tristan.cms

import freemarker.cache.ClassTemplateLoader
import guillo.tristan.cms.admin.AdminArticleListPresenter
import guillo.tristan.cms.admin.AdminCallsPresenter
import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment
import guillo.tristan.cms.model.User
import guillo.tristan.cms.tpl.DetailsContext
import guillo.tristan.cms.tpl.IndexContext
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import kotlinx.coroutines.launch

class App

data class AuthSession(val user: String)

fun main() {
    val component = AppComponents("jdbc:mysql://localhost/CMS?serverTimezone=UTC", "root", "")

    embeddedServer(Netty, 8080){

        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(App::class.java.classLoader, "templates")
        }

        install(Sessions) {
            cookie<AuthSession>("AUTH_SESSION", SessionStorageMemory())
        }

        install(Authentication) {
            form("check-auth") {
                userParamName = "username"
                passwordParamName = "password"
                challenge = FormAuthChallenge.Redirect { "/login" }

                validate { credentials ->
                    val authService = component.authService
                    val user: User? = authService.getUserByUsername(credentials.name)

                    if (user == null) {
                        null
                    } else if ((credentials.name == user.username) && (credentials.password == user.password)) {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
                skipWhen { call -> call.sessions.get<AuthSession>() != null }
            }
        }

        routing {
            static("static") {
                resources("static")
            }

            get("/article/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                val controller: ArticlePresenter = component.getArticlePresenter(object : ArticlePresenter.View {
                    override fun displayArticle(article: Article, comments: List<Comment>) {
                        val context = DetailsContext(article, comments)
                        launch {
                            call.respond(FreeMarkerContent("details.ftl", context, null))
                        }
                    }

                    override fun displayNotFound() {
                        launch {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }

                })

                if (id  == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    controller.start(id)
                }
            }

            post("/article/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val postParameters: Parameters = call.receiveParameters()

                val text: String? = postParameters["text"]

                val controller: ArticlePresenter = component.getArticlePresenter(object : ArticlePresenter.View {
                    override fun displayArticle(article: Article, comments: List<Comment>) {
                        val context = DetailsContext(article, comments)
                        launch {
                            call.respond(FreeMarkerContent("details.ftl", context, null))
                        }
                    }

                    override fun displayNotFound() {
                        launch {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }

                })

                if (id == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    controller.postComment(id, text)
                }
            }

            get ("/") {
                val controller: ArticleListPresenter = component.getArticleListPresenter(object : ArticleListPresenter.View {
                    override fun displayArticleList(list: List<Article>) {
                        val context = IndexContext(list)
                        launch {
                            call.respond(FreeMarkerContent("index.ftl", context, null))
                        }
                    }
                })

                controller.start()
            }

            // ADMIN
            get("/login") {
                call.respond(FreeMarkerContent("auth/login.ftl", null, null))
            }

            authenticate("check-auth") {
                post("/login") {
                    val principal = call.authentication.principal<UserIdPrincipal>()
                    call.sessions.set(AuthSession(principal!!.name))
                    call.respondRedirect("/admin/")
                }

                route("admin") {
                    get {
                        val controller =
                            component.getAdminArticleListPresenter(object : AdminArticleListPresenter.View {
                                override fun displayArticleList(list: List<Article>) {
                                    val context = IndexContext(list)
                                    launch {
                                        call.respond(FreeMarkerContent("admin/admin_index.ftl", context, null))
                                    }
                                }
                            })

                        controller.start()
                    }

                    route("article") {
                        get("/{id}") {
                            val id = call.parameters["id"]?.toIntOrNull()

                            val controller: ArticlePresenter =
                                component.getArticlePresenter(object : ArticlePresenter.View {
                                    override fun displayArticle(article: Article, comments: List<Comment>) {
                                        val context = DetailsContext(article, comments)
                                        launch {
                                            call.respond(FreeMarkerContent("admin/admin_details.ftl", context, null))
                                        }
                                    }

                                    override fun displayNotFound() {
                                        launch {
                                            call.respond(HttpStatusCode.NotFound)
                                        }
                                    }

                                })

                            if (id == null) {
                                call.respond(HttpStatusCode.NotFound)
                            } else {
                                controller.start(id)
                            }
                        }

                        get("delete/{id}") {
                            val id = call.parameters["id"]?.toIntOrNull()

                            val controller: AdminCallsPresenter =
                                component.getAdminCallsPresenter(object : AdminCallsPresenter.View {
                                    override fun redirect() {
                                        launch {
                                            call.respondRedirect("/admin/")
                                        }
                                    }

                                    override fun displayNotFound() {
                                        launch {
                                            call.respond(HttpStatusCode.NotFound)
                                        }
                                    }
                                })

                            if (id == null) {
                                call.respond(HttpStatusCode.NotFound)
                            } else {
                                controller.deleteArticle(id)
                            }
                        }

                        post("add") {
                            val postParameters: Parameters = call.receiveParameters()

                            val title: String? = postParameters["title"]
                            val text: String? = postParameters["text"]

                            val controller: AdminCallsPresenter =
                                component.getAdminCallsPresenter(object : AdminCallsPresenter.View {
                                    override fun redirect() {
                                        launch {
                                            call.respondRedirect("/admin/")
                                        }
                                    }

                                    override fun displayNotFound() {
                                        launch {
                                            call.respond(HttpStatusCode.NotFound)
                                        }
                                    }
                                })

                            if (title == null || text == null) {
                                call.respond(HttpStatusCode.NotFound)
                            } else {
                                controller.addArticle(title, text)
                            }
                        }

                        route("comment") {
                            get("delete/{id}") {
                                val id = call.parameters["id"]?.toIntOrNull()

                                val controller: AdminCallsPresenter =
                                    component.getAdminCallsPresenter(object : AdminCallsPresenter.View {
                                        override fun redirect() {
                                            launch {
                                                call.respondRedirect("/admin/")
                                            }
                                        }

                                        override fun displayNotFound() {
                                            launch {
                                                call.respond(HttpStatusCode.NotFound)
                                            }
                                        }
                                    })

                                if (id == null) {
                                    call.respond(HttpStatusCode.NotFound)
                                } else {
                                    controller.deleteComment(id)
                                }
                            }
                        }
                    }
                }

                get("/logout") {
                    call.sessions.clear<AuthSession>()
                    call.respondRedirect("/")
                }
            }

        }



    }.start(wait = true)
}
