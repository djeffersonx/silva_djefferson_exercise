package com.ecore.roles.application.controller;

import com.ecore.roles.domain.service.TeamsService;
import com.ecore.roles.application.controller.resources.outcome.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.application.controller.resources.outcome.TeamResponse.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/teams")
public class TeamsRestController {

    private final TeamsService teamsService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<TeamResponse>> get() {
        return ResponseEntity
                .status(200)
                .body(teamsService.getTeams().stream()
                        .map(TeamResponse::fromModel)
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{teamId}", produces = {"application/json"})
    public ResponseEntity<TeamResponse> get(@PathVariable UUID teamId) {
        return ResponseEntity.status(200).body(fromModel(teamsService.getTeam(teamId)));
    }

}
