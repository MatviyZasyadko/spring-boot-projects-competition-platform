package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.competitions.business_layer.Competition;
import com.ukma.competition.platform.competitions.business_layer.CompetitionService;
import com.ukma.competition.platform.competitions.business_layer.CompetitionServiceImpl;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/competitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionController {
    CompetitionService competitionService;

    @PostMapping("/upload")
    public ResponseEntity<CompetitionDto> createNewCompetition(@Valid @RequestBody CompetitionDto newCompetitionDto) {
        Competition newCompetition = convertDtoToCompetition(newCompetitionDto);
        Competition savedCompetition = competitionService.save(newCompetition);
        CompetitionDto savedCompetitionDto = convertCompetitionToDto(savedCompetition);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompetitionDto);
    }

    @GetMapping
    public ResponseEntity<List<CompetitionDto>> findAll() {
        competitionService.logger.info("Received request to retrieve all competitions");
        // !!!!!!!!! Прибрати
        ThreadContext.put("methodName", "findAll");
        ThreadContext.put("id", "1");
        List<Competition> allCompetitions = competitionService.findAll();
        List<CompetitionDto> allCompetitionDtos = allCompetitions.stream()
                .map(this::convertCompetitionToDto)
                .toList();
        ThreadContext.clearAll();
        return ResponseEntity.ok(allCompetitionDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDto> findById(@PathVariable("id") String id) {
        Optional<Competition> optionalCompetition = competitionService.findById(id);

        competitionService.logger.info("Received request to retrieve competition with ID: {}", id);

        ThreadContext.put("competitionID", id);

        if (optionalCompetition.isPresent()) {
            CompetitionDto competitionDto = convertCompetitionToDto(optionalCompetition.get());
            return ResponseEntity.ok(competitionDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDto> updateById(
            @PathVariable("id") String id,
            @RequestBody @Valid CompetitionDto competitionDto
    ) {

        competitionService.logger.info("Received request to update competition with ID: {}", id);
        ThreadContext.put("competitionID", id);
        ThreadContext.put("competitionName", competitionDto.getName());

        Competition competition = convertDtoToCompetition(competitionDto);
        try {
            Competition updatedCompetition = competitionService.updateById(id, competition);
            CompetitionDto updatedCompetitionDto = convertCompetitionToDto(updatedCompetition);
            ThreadContext.clearAll();
            return ResponseEntity.ok(updatedCompetitionDto);
        } catch (EntityNotFoundException e) {
            competitionService.logger.error("Failed to update competition. Competition with ID: {} not found", id);
            ThreadContext.clearAll();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        competitionService.deleteById(id);
    }


    private Competition convertDtoToCompetition(CompetitionDto dto) {
        Competition competition = new Competition();
        competition.setName(dto.getName());
        competition.setDescription(dto.getDescription());
        competition.setBeginDate(dto.getBeginDate());
        competition.setVotingBeginDate(dto.getVotingBeginDate());
        competition.setVotingEndDate(dto.getVotingEndDate());
        competition.setHasPrizePool(dto.getHasPrizePool());
        competition.setPriceDescription(dto.getPriceDescription());
        competition.setPrizePool(dto.getPrizePool());
        competition.setImages(dto.getImages());
        competition.setProjects(dto.getProjects());
        competition.setTags(dto.getTags());
        competition.setPayments(dto.getPayments());
        return competition;
    }

    private CompetitionDto convertCompetitionToDto(Competition competition) {
        CompetitionDto dto = new CompetitionDto();
        dto.setId(competition.getId());
        dto.setName(competition.getName());
        dto.setDescription(competition.getDescription());
        dto.setBeginDate(competition.getBeginDate());
        dto.setVotingBeginDate(competition.getVotingBeginDate());
        dto.setVotingEndDate(competition.getVotingEndDate());
        dto.setHasPrizePool(competition.getHasPrizePool());
        dto.setPriceDescription(competition.getPriceDescription());
        dto.setPrizePool(competition.getPrizePool());
        dto.setImages(competition.getImages());
        dto.setProjects(competition.getProjects());
        dto.setTags(competition.getTags());
        dto.setPayments(competition.getPayments());
        return dto;
    }
}
