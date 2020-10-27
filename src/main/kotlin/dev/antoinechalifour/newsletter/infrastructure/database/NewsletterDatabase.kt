package dev.antoinechalifour.newsletter.infrastructure.database

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.Recipient
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "newsletter")
@TypeDefs(
    TypeDef(name = "json", typeClass = JsonStringType::class),
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
open class NewsletterDatabase(
    @Id
    open var id: UUID? = null,

    open var newsletterConfigurationId: UUID? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    open var payload: String? = null
) {
    companion object {
        val mapper = jacksonObjectMapper()

        fun of(newsletter: Newsletter) = NewsletterDatabase(
            newsletter.id,
            newsletter.newsletterConfigurationId,
            mapper.writeValueAsString(JsonNewsletter(newsletter))
        )
    }

    data class JsonNewsletter(
        val id: String,
        val newsletterConfigurationId: String,
        val recipient: JsonRecipient,
        val articles: List<JsonArticle>,
    ) {
        constructor(newsletter: Newsletter) : this(
            newsletter.id.toString(),
            newsletter.newsletterConfigurationId.toString(),
            JsonRecipient(newsletter.recipient),
            newsletter.articles.map { JsonArticle(it) }
        )

        fun toNewsletter() = Newsletter(
            UUID.fromString(id),
            UUID.fromString(newsletterConfigurationId),
            recipient.toRecipient(),
            articles.map { it.toArticle() }
        )
    }

    data class JsonArticle(
        val title: String,
        val url: String,
        val pubDate: Long,
    ) {
        constructor(article: Article) : this(
            article.title,
            article.url,
            article.pubDate.toEpochSecond(ZoneOffset.UTC)
        )

        fun toArticle() = Article(
            title,
            url,
            LocalDateTime.ofEpochSecond(pubDate, 0, ZoneOffset.UTC)
        )
    }

    data class JsonRecipient(
        val name: String,
        val email: String
    ) {
        constructor(recipient: Recipient) : this(
            recipient.name,
            recipient.email
        )

        fun toRecipient() = Recipient(
            name,
            email
        )
    }

    fun toNewsletter() = mapper.readValue(payload, JsonNewsletter::class.java).toNewsletter()
}