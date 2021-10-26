package crypto.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Currency {

    private @Id @GeneratedValue Long id;
    private String ticker;
    private String name;
    private long numberOfCoins;
    private BigDecimal marketCap;

    public Currency() {
    }

    public Currency( String ticker, String name, long numberOfCoins, BigDecimal marketCap ) {
        this.ticker = ticker;
        this.name = name;
        this.numberOfCoins = numberOfCoins;
        this.marketCap = marketCap;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker( String ticker ) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public long getNumberOfCoins() {
        return numberOfCoins;
    }

    public void setNumberOfCoins( long numberOfCoins ) {
        this.numberOfCoins = numberOfCoins;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap( BigDecimal marketCap ) {
        this.marketCap = marketCap;
    }

    @Override
    public String toString() {
        return String.format( "Currency[id=%d, ticker='%s', name='%s', number of coins=%,d, market cap=$ %,.0f]", id,
                ticker, name, numberOfCoins, marketCap );
    }
}
