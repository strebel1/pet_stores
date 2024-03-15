package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service

public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {

		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));

	}

	private PetStore findOrCreatePetStore(Long petStoreId) {

		if (Objects.isNull(petStoreId)) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);
		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + "does not exist."));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());

	}

	@Transactional(readOnly = false)

	public PetStoreEmployee savePetStoreEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.addEmployee(employee);
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);

	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (employeeId == null) {
			return new Employee();
		} else {
			return findEmployeeById(petStoreId, employeeId);

		}

	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {

		Employee employee = employeeDao.findById(employeeId)

				.orElseThrow(() -> new NoSuchElementException("Employee not found with ID" + petStoreId));

		if (employee.getPetStore().getPetStoreId().equals(petStoreId)) {

			return employee;

		} else {
			throw new IllegalArgumentException(
					"Employee with ID" + employeeId + "does not belong to PetStore with ID" + petStoreId);
		}
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());

	}

	@Transactional
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);

		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);

		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);

	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if (customerId == null) {
			return new Customer();
		} 
			return findCustomerById(petStoreId, customerId);

		

	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {

		Customer customer = customerDao.findById(customerId)

				.orElseThrow(() -> new NoSuchElementException("Customer not found with ID" + petStoreId));

		boolean found = false;
		for (PetStore petStore : customer.getPetStores()) {
			if (petStore.getPetStoreId() == petStoreId) {

				found = true;
				break;

			}

		}
		if (!found) {
			throw new IllegalArgumentException(
					"Customer with ID" + customerId + "does not shop at PetStore with ID" + petStoreId);
		}
		return customer;

	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());

	}

	@Transactional(readOnly=true)
			
			public List<PetStoreData> retrieveAllPetStores() {
			
			List<PetStore> petStores = petStoreDao.findAll();
			List<PetStoreData> result = new LinkedList<>();
			
			for(PetStore petStore: petStores) {
				PetStoreData psd = new PetStoreData(petStore);
				
				psd.getCustomers().clear();
				psd.getEmployees().clear();
				
				result.add(psd);
		
			}
				return result;
	
	}

	@Transactional
	public PetStoreData retrievePetStoryById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);

		petStoreDao.delete(petStore);
	}

}
