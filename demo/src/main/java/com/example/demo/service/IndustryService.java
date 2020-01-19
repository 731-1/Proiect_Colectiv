package com.example.demo.service;


import com.example.demo.model.Industry;
import com.example.demo.repository.IndustryRepository;
import com.example.demo.service.dto.DtoMapping;
import com.example.demo.service.dto.IndustryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class IndustryService {

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private DtoMapping dtoMapping;

    public List<IndustryDto> findAll()
    {
        return industryRepository.findAll().stream().map(dtoMapping::industryToDto).collect(Collectors.toList());
    }

    public IndustryDto findById(Integer id){
        if(industryRepository.findById(id).isPresent()){
            return dtoMapping.industryToDto(industryRepository.findById(id).get());
        }
        return null;
    }

    //todo admin only
    public IndustryDto save(IndustryDto industryDto) {
        Industry industry = dtoMapping.dtoToIndustry(industryDto);
        industry = industryRepository.save(industry);
        return dtoMapping.industryToDto(industry);
    }

    //todo admin only
    public void deactivateIndustry(IndustryDto industryDto) {
        industryRepository.findById(industryDto.getId()).ifPresent(industry -> {
            industry.setStatus(false);
            industryRepository.save(industry);
        });
    }

}
