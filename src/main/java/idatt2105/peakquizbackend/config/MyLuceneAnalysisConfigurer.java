package idatt2105.peakquizbackend.config;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

/**
 * Configures Lucene analysis for Hibernate Search backend.
 */
public class MyLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

    /**
     * Configures the Lucene analysis process.
     *
     * @param context
     *            The configuration context for Lucene analysis.
     */
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("english").custom().tokenizer("standard").tokenFilter("lowercase")
                .tokenFilter("snowballPorter").param("language", "English").tokenFilter("asciiFolding");
    }
}