package co.com.viveres.susy.microservicesupplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;

@Repository
public interface ISupplierRepository extends JpaRepository<SupplierEntity, Long> {

	Optional<SupplierEntity> findByIdentificationNumber(String identificationNumber);

}
