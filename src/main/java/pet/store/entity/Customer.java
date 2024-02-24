package pet.store.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data

public class Customer {

	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long customerId;

	
	  private String customerFirstName;
	 
	
	  private String customerLastName;
	  
	  @Column(unique = true)
	  private String customerEmail;
	  
	  @ToString.Exclude
	  @EqualsAndHashCode.Exclude
	  
	  @ManyToMany(mappedBy = "customers",  cascade = CascadeType.PERSIST)
	  private Set<PetStore> petStores = new HashSet <>();
	  


}
