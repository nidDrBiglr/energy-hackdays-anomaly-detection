package ch.opendata.energy.domain.meter;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyRepository extends ReactiveSortingRepository<AnomalyDocument, String> {
}
