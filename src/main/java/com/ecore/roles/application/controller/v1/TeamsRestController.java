package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.outcome.TeamResponse;
import com.ecore.roles.domain.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/teams")
public class TeamsRestController {

    private final TeamsService teamsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TeamResponse> get() {
        return teamsService.getTeams().stream()
                .map(TeamResponse::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse get(@PathVariable @NotNull UUID teamId) {
        return TeamResponse.fromModel(teamsService.getTeam(teamId));
    }

}
