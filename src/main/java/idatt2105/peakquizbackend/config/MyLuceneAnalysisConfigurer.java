package idatt2105.peakquizbackend.config;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class MyLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer( "english" ).custom() 
                .tokenizer( "standard" ) 
                .tokenFilter( "lowercase" ) 
                .tokenFilter( "snowballPorter" ) 
                        .param( "language", "English" ) 
                .tokenFilter( "asciiFolding" );
    }
}