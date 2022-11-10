package co.com.viveres.susy.microservicesupplier.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "\"PRODUCTS_SUPPLIERS\"")
public class ProductSupplierEntity{
    
	@Id
	@SequenceGenerator(name = "\"SEQ_PRODUCT_SUPPLIER_ID\"", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "\"SEQ_PRODUCT_SUPPLIER_ID\"")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "ID_PRODUCT_FK", unique = false, nullable = false)
    private Long productId;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "ID_SUPPLIER_FK", unique = false, nullable = false)
    private SupplierEntity supplier;

}
