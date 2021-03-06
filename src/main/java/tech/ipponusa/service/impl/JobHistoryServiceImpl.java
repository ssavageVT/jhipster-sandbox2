package tech.ipponusa.service.impl;

import tech.ipponusa.service.JobHistoryService;
import tech.ipponusa.domain.JobHistory;
import tech.ipponusa.repository.JobHistoryRepository;
import tech.ipponusa.service.dto.JobHistoryDTO;
import tech.ipponusa.service.mapper.JobHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing JobHistory.
 */
@Service
@Transactional
public class JobHistoryServiceImpl implements JobHistoryService{

    private final Logger log = LoggerFactory.getLogger(JobHistoryServiceImpl.class);
    
    @Inject
    private JobHistoryRepository jobHistoryRepository;

    @Inject
    private JobHistoryMapper jobHistoryMapper;

    /**
     * Save a jobHistory.
     *
     * @param jobHistoryDTO the entity to save
     * @return the persisted entity
     */
    public JobHistoryDTO save(JobHistoryDTO jobHistoryDTO) {
        log.debug("Request to save JobHistory : {}", jobHistoryDTO);
        JobHistory jobHistory = jobHistoryMapper.jobHistoryDTOToJobHistory(jobHistoryDTO);
        jobHistory = jobHistoryRepository.save(jobHistory);
        JobHistoryDTO result = jobHistoryMapper.jobHistoryToJobHistoryDTO(jobHistory);
        return result;
    }

    /**
     *  Get all the jobHistories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<JobHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobHistories");
        Page<JobHistory> result = jobHistoryRepository.findAll(pageable);
        return result.map(jobHistory -> jobHistoryMapper.jobHistoryToJobHistoryDTO(jobHistory));
    }

    /**
     *  Get one jobHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public JobHistoryDTO findOne(Long id) {
        log.debug("Request to get JobHistory : {}", id);
        JobHistory jobHistory = jobHistoryRepository.findOne(id);
        JobHistoryDTO jobHistoryDTO = jobHistoryMapper.jobHistoryToJobHistoryDTO(jobHistory);
        return jobHistoryDTO;
    }

    /**
     *  Delete the  jobHistory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobHistory : {}", id);
        jobHistoryRepository.delete(id);
    }
}
