package ru.raysmith.tgbot.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.raysmith.htmldiff.HtmlDiff
import ru.raysmith.htmldiff.downloadResourcesAndReplacePaths
import ru.raysmith.htmldiff.removeComments
import java.io.File
import java.net.URL

open class HtmlDiffTask : DefaultTask() {

    private val destFolder = project.rootDir.resolve("docsDiff").apply {
        mkdirs()
    }

    @TaskAction
    fun execute() {
        listOf(
            "api" to "https://core.telegram.org/bots/api",
            "features" to "https://core.telegram.org/bots/features",
            "webapps" to "https://core.telegram.org/bots/webapps",
        ).forEach { (name, url) ->
            process(name, url)
        }
    }

    private fun redownload(folder: File, doc: Document) {
        folder.listFiles()?.forEach {
            it.deleteRecursively()
        }
        doc.downloadResourcesAndReplacePaths(folder, removeComments = true)
    }

    private fun process(name: String, url: String) {
        val folder = destFolder.resolve(name)
        folder.mkdirs()

        val lastRevision = folder.listFiles()?.find { it.name == "index.html" }
        val origDoc = Jsoup.connect(url).get()
        val preparedDoc = origDoc.clone().apply {
            val elementsWithSrc = select("[src]")
            val elementsWithHref = select("link[href]")
            val resourceElements = elementsWithSrc + elementsWithHref

            for (element in resourceElements) {
                val attributeKey = if (element.hasAttr("src")) "src" else "href"
                val resourceUrl = element.absUrl(attributeKey)
                if (resourceUrl.isEmpty()) continue

                val resourceFile = File(folder, URL(resourceUrl).path)
                val relativePath = resourceFile.relativeTo(folder).path
                element.attr(attributeKey, relativePath)
            }
            removeComments()
        }

        if (lastRevision == null) {
            redownload(folder, origDoc)
            println("'$name' page skipped: the last revision not found, current state was downloaded")
            return
        }

        val diff = HtmlDiff.execute(lastRevision.readText(), preparedDoc.outerHtml())
        val lastDiff = folder.listFiles()?.find { it.name == "diff.html" }

        if (diff != null && lastDiff?.readText() == diff) {
            println("'$name' page skipped: the current state is the same as the last revision")
            return
        }

        redownload(folder, origDoc)

        if (diff != null) {
            folder.resolve("diff.html").writeText(diff)
        }
    }
}