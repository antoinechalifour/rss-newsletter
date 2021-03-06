package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NewsletterScheduler(
    val newsletterConfigurationPort: NewsletterConfigurationPort,
    val sendNewsletter: SendNewsletter
) {

    @Scheduled(cron = "0 30 12 * * *", zone = "Europe/Paris")
    fun sendNewsletterTask() {
        newsletterConfigurationPort.all().parallelStream().forEach {
            sendNewsletter(it.id.toString())
        }
    }
}
