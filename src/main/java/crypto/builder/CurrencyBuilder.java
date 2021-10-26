package crypto.builder;

import java.math.BigDecimal;

import crypto.model.Currency;

public class CurrencyBuilder {

    public Currency buildBitcoin() {
        return new Currency( "BTC", "Bitcoin", 16770000, new BigDecimal( 189580000000l ) );
    }

    public Currency buildEthereum() {
        return new Currency( "ETH", "Ethereum", 96710000, new BigDecimal( 69280000000l ) );
    }

    public Currency buildRipple() {
        return new Currency( "XRP", "Ripple", 38590000000l, new BigDecimal( 64750000000l ) );
    }

    public Currency buildBitcoinCash() {
        return new Currency( "BCH", "BitcoinCash", 16670000, new BigDecimal( 69020000000l ) );
    }

}
