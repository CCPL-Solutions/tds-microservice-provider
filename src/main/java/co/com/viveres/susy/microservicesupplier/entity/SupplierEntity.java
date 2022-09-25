package co.com.viveres.susy.microservicesupplier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "SUPPLIER")
public class SupplierEntity {

	@Id
	@SequenceGenerator(name = "SEQ_SUPPLIER_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SUPPLIER_ID")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "BUSINESS_NAME", unique = false, nullable = false)
    private String businessName;

    @Column(name = "IDENTIFICATION_NUMBER", unique = false, nullable = false)
    private String identificationNumber;

    @Column(name = "PHONE", unique = false, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "supplier", 
    		   cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
    		   orphanRemoval = true)
    private List<ProductSupplierEntity> productSupplierList;

    public void addProduct(ProductSupplierEntity product) {
        if (productSupplierList == null)
            productSupplierList = new ArrayList<>();
        productSupplierList.add(product);
        product.setSupplier(this);
    }    

}
