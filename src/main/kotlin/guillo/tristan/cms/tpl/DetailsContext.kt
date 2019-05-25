package guillo.tristan.cms.tpl

import guillo.tristan.cms.model.Article
import guillo.tristan.cms.model.Comment

data class DetailsContext(val article: Article, val comments: List<Comment>)
