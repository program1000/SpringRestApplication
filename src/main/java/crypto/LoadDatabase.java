package crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import crypto.builder.CurrencyBuilder;
import crypto.repository.CurrencyRepository;

@Configuration
public class LoadDatabase {

    private final Logger log = LoggerFactory.getLogger( LoadDatabase.class );

    @Bean
    CommandLineRunner initDatabase( CurrencyRepository repository ) {
        CurrencyBuilder currencyBuilder = new CurrencyBuilder();
        return args -> {
            log.info( "Preloading {}", repository.save( currencyBuilder.buildBitcoin() ) );
            log.info( "Preloading {}", repository.save( currencyBuilder.buildEthereum() ) );
            log.info( "Preloading {}", repository.save( currencyBuilder.buildRipple() ) );
            log.info( "Preloading {}", repository.save( currencyBuilder.buildBitcoinCash() ) );
        };
    }
}
