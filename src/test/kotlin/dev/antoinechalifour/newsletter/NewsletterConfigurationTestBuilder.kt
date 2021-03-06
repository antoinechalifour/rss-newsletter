package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID

class NewsletterConfigurationTestBuilder {

    companion object {
        fun aNewsletterConfiguration() = NewsletterConfigurationTestBuilder()
    }

    private var recipientId = UUID.randomUUID()
    private var id = UUID.randomUUID()
    private var sources = mutableListOf<Source>()

    fun withSources(vararg theSources: Source) = apply {
        sources = theSources.toMutableList()
    }

    fun withSources(vararg theSources: SourceTestBuilder) = apply {
        sources = theSources.map { it.build() }.toMutableList()
    }

    fun withRecipientId(theRecipientId: UUID) = apply { recipientId = theRecipientId }
    fun build() = NewsletterConfiguration(id, recipientId, sources)
}
