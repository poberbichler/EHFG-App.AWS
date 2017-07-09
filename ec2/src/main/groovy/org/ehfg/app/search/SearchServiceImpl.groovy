package org.ehfg.app.search

import groovy.util.logging.Slf4j
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.*
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.concurrent.locks.StampedLock

/**
 * @author patrick
 * @since 07.2017
 */
@Slf4j
@Service
class SearchServiceImpl implements SearchService {
    private Directory index

    private final RestTemplate restTemplate

    private final String sessionEndpointUrl
    private final String speakerEndpointUrl

    private final StampedLock lock = new StampedLock()

    @Autowired
    SearchServiceImpl(RestTemplateBuilder restTemplateBuilder,
                      @Value('${endpoint.url.session}') String sessionEndpointUrl,
                      @Value('${endpoint.url.speaker}') String speakerEndpointUrl) {
        this.restTemplate = restTemplateBuilder.build()
        this.sessionEndpointUrl = sessionEndpointUrl
        this.speakerEndpointUrl = speakerEndpointUrl
    }

    @Override
    SearchResult search(String searchTerm) {
        log.info("searching for input [{}]", searchTerm)

        def timestamp = this.lock.readLock()
        try {

            DirectoryReader directoryReader = StandardDirectoryReader.open(index)
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader)

            TopDocs search = indexSearcher.search(new TermQuery(new Term("content", searchTerm)), 50)
            return search.scoreDocs.inject(new SearchResult()) { result, scoreDoc ->
                def doc = indexSearcher.doc(scoreDoc.doc)
                result << new SearchResultPosition(
                        id: doc.get("id"),
                        type: SearchResultType.valueOf(doc.get("type")),
                        description: doc.get("name"))
            }
        }

        finally {
            this.lock.unlockRead(timestamp)
        }
    }

    @Override
    @Scheduled(fixedRate = 3_600_000L)
    void updateIndex() {
        log.info("updating search index...")
        def timestamp = this.lock.writeLock()

        try {
            index?.close()
            index = new RAMDirectory()

            new IndexWriter(index, new IndexWriterConfig(new StandardAnalyzer())).withCloseable { writer ->
                log.info("building index for sessions...")

                def sessions = this.restTemplate.getForObject(this.sessionEndpointUrl, Map.class)
                        .collectMany { it.value.sessions }
                def speakers = this.restTemplate.getForObject(this.speakerEndpointUrl, List.class)

                log.info("found [{}] sessions and [{}] speakers", sessions.size(), speakers.size())
                writer.addDocuments(buildIndexForSessions(sessions))
                writer.addDocuments(buildIndexForSpeakers(speakers))
            }
        } finally {
            this.lock.unlockWrite(timestamp)

        }
    }

    private static List<Document> buildIndexForSpeakers(List<Map<String, String>> speakers) {
        return speakers.collect {
            def document = new Document()
            document.add(new TextField("content", new StringReader(it.description)))
            document.add(new TextField("name", it.fullName, Field.Store.YES))
            document.add(new StringField("type", SearchResultType.SPEAKER.toString(), Field.Store.YES))
            document.add(new StringField("id", it.id, Field.Store.YES))
            return document
        }
    }

    private static List<Document> buildIndexForSessions(List<Map<String, String>> sessions) {
        return sessions.collect {
            def document = new Document()
            document.add(new TextField("content", new StringReader(it.description)))
            document.add(new TextField("name", it.name, Field.Store.YES))
            document.add(new StringField("type", SearchResultType.SESSION.toString(), Field.Store.YES))
            document.add(new StringField("id", it.id, Field.Store.YES))
            return document
        }
    }
}
