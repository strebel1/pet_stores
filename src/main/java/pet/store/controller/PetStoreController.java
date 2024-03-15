package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j

public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;

	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStoreData(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store data {}", petStoreData);

		return petStoreService.savePetStore(petStoreData);

	}

	@PostMapping("/petStore/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating petStoreId{}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)

	public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Adding employee{} to pet store with ID={}", petStoreEmployee, petStoreId);
		
		return petStoreService.savePetStoreEmployee(petStoreId, petStoreEmployee);

	}
	
	@PostMapping ("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	
	public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId, 
			@RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Adding customer {} to pet store with ID={}", petStoreCustomer, petStoreId);
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}
 @GetMapping("/{petStoreId}")
 public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
	 log.info("Retrieving pet store with ID={}", petStoreId);
	 return petStoreService.retrievePetStoryById(petStoreId);
	 
 }
 
 @GetMapping 

 public List<PetStoreData> retrieveAllPetStores() {
 		return petStoreService.retrieveAllPetStores();
 	}
	 
 @DeleteMapping ("/{petStoreId}")
 public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId){
	 log.info("Deleting pet store with ID={}", petStoreId);
	 petStoreService.deletePetStoreById(petStoreId);
	return Map.of("message","Pet store with ID" + petStoreId + "has been deleted");

	

	 
 }
	 
	 
 }

