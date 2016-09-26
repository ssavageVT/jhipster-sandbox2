package tech.ipponusa.service.impl;

import tech.ipponusa.service.PtoPeriodService;
import tech.ipponusa.domain.Employee;
import tech.ipponusa.domain.PtoPeriod;
import tech.ipponusa.domain.User;
import tech.ipponusa.repository.EmployeeRepository;
import tech.ipponusa.repository.PtoPeriodRepository;
import tech.ipponusa.repository.UserRepository;
import tech.ipponusa.security.SecurityUtils;
import tech.ipponusa.service.dto.PtoPeriodDTO;
import tech.ipponusa.service.mapper.PtoPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PtoPeriod.
 */
@Service
@Transactional
public class PtoPeriodServiceImpl implements PtoPeriodService{

    private final Logger log = LoggerFactory.getLogger(PtoPeriodServiceImpl.class);
    
    @Inject
    private PtoPeriodRepository ptoPeriodRepository;

    @Inject
    private PtoPeriodMapper ptoPeriodMapper;
    
    @Inject 
    private EmployeeRepository employeeRepository;
   
    
    @Inject
    private UserRepository userRepository;

    /**
     * Save a ptoPeriod.
     *
     * @param ptoPeriodDTO the entity to save
     * @return the persisted entity
     */
    public PtoPeriodDTO save(PtoPeriodDTO ptoPeriodDTO) {
    	 log.debug("Request to save PtoPeriod : {}", ptoPeriodDTO);
         PtoPeriod ptoPeriod = ptoPeriodMapper.ptoPeriodDTOToPtoPeriod(ptoPeriodDTO);
         
         
         
         
         ptoPeriod = ptoPeriodRepository.save(ptoPeriod);
         PtoPeriodDTO result = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(ptoPeriod);
         return result;
    }

    /**
     *  Get all the ptoPeriods.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PtoPeriodDTO> findAll() {
        log.debug("Request to get all PtoPeriods");
        List<PtoPeriodDTO> result = ptoPeriodRepository.findAll().stream()
            .map(ptoPeriodMapper::ptoPeriodToPtoPeriodDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        if(result.isEmpty()){
        	PtoPeriodDTO init = new PtoPeriodDTO();
        	init.setEndDate(calculatePtoPeriodEndDate());
        	result.add(init);
        }
        
        return result;
    }
    
    private ZonedDateTime calculatePtoPeriodEndDate(){
    	
    	//Modify the PTO period based on who is logged in.
        String login = SecurityUtils.getCurrentUserLogin();
        log.info("PTO Period Info");
        log.info("User Requesting:"  + login);
        
        //Get the email address from the user table
        //Use email address to lookup the employee ID
        //From there we can determine the PTO period and check balances
        User user = userRepository.findOneByLogin(login).get();
        log.info("User Found: " + user.getFirstName() + " " + user.getLastName());
        log.info("User Email Address: " + user.getEmail());       
        
        Employee employee = employeeRepository.findEmployeeByEmail(user.getEmail());
    	
        log.info("Employee Found: " + employee.getFirstName() + " " + employee.getLastName());
        
    	return employee.getHireDate().plusYears(1);
    }

    /**
     *  Get one ptoPeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PtoPeriodDTO findOne(Long id) {
        log.debug("Request to get PtoPeriod : {}", id);
        PtoPeriod ptoPeriod = ptoPeriodRepository.findOne(id);
        PtoPeriodDTO ptoPeriodDTO = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(ptoPeriod);
        return ptoPeriodDTO;
    }

    /**
     *  Delete the  ptoPeriod by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PtoPeriod : {}", id);
        ptoPeriodRepository.delete(id);
    }
}
