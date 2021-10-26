package crypto.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyNotFoundException extends RuntimeException {
    
    private final Logger log = LoggerFactory.getLogger( CurrencyNotFoundException.class );

    public CurrencyNotFoundException( Long id ) {
        super( getMessage( id ) );
        log.error( getMessage() );
    }
    
    private static String getMessage( Long id ) {
        return "Could not find currency with id [" + id + "]";
    }
}
