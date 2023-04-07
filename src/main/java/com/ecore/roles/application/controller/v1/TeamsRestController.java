package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.ApiVersion;
import com.ecore.roles.application.controller.v1.resources.outcome.TeamResponse;
import com.ecore.roles.domain.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/teams", headers = ApiVersion.V1)
public class TeamsRestController {

    private final TeamsService teamsService;

    @GetMapping
    public ResponseEntity<List<TeamResponse>> get() {
        return ResponseEntity
                .status(200)
                .body(teamsService.getTeams().stream()
                        .map(TeamResponse::fromModel)
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{teamId}")
    public ResponseEntity<TeamResponse> get(@PathVariable UUID teamId) {
        return ResponseEntity.status(200).body(TeamResponse.fromModel(teamsService.getTeam(teamId)));
    }

}
