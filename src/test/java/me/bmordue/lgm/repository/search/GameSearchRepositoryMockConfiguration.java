package me.bmordue.lgm.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of GameSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GameSearchRepositoryMockConfiguration {

    @MockBean
    private GameSearchRepository mockGameSearchRepository;

}
