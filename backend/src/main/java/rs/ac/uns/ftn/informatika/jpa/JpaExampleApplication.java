package rs.ac.uns.ftn.informatika.jpa;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class JpaExampleApplication {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public CacheManager cacheManager() throws URISyntaxException {
		CachingProvider cachingProvider = Caching.getCachingProvider();
		javax.cache.CacheManager jCacheManager =
				cachingProvider.getCacheManager(
						getClass().getResource("/ehcache.xml").toURI(),
						getClass().getClassLoader()
				);
		return new JCacheCacheManager(jCacheManager);
	}


	public static void main(String[] args) {
		SpringApplication.run(JpaExampleApplication.class, args);
	}

}
