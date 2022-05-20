package co.com.viveres.susy.microservicesupplier.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.viveres.susy.microservicesupplier.entity.ProductSupplierEntity;
import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;

@Repository
public interface IProductSupplierRepository extends JpaRepository<ProductSupplierEntity, Long> {
	
	List<ProductSupplierEntity> findBySupplier(SupplierEntity supplier);
	
	Optional<ProductSupplierEntity> findByProductIdAndSupplier(Long productId, SupplierEntity supplier);
	
	void deleteByProductIdAndSupplier(Long productId, SupplierEntity supplier);

}
