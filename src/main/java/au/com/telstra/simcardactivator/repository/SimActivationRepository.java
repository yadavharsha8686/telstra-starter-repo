package au.com.telstra.simcardactivator.repository;

import au.com.telstra.simcardactivator.model.SimActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimActivationRepository extends JpaRepository<SimActivation, Long> {
}
