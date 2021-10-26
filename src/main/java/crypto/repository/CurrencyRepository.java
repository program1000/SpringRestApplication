package crypto.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import crypto.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    
    public Page<Currency> findAll(Pageable pageable);

}
