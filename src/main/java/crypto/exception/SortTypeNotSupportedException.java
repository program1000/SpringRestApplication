package crypto.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortTypeNotSupportedException extends RuntimeException {
        
        private final Logger log = LoggerFactory.getLogger( SortTypeNotSupportedException.class );

        public SortTypeNotSupportedException( String type ) {
            super( getMessage( type ) );
            log.error( getMessage() );
        }
        
        private static String getMessage( String type ) {
            return "Sort type [" + type + "] is not supported";
        }
}
