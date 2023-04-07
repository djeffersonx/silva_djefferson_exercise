package com.ecore.roles.utils;

import com.ecore.roles.application.controller.v1.resources.outcome.MembershipResponse;
import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

public class RestAssuredHelper {

    public static void setUp(int port) {
        RestAssured.reset();
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = "http://localhost:" + port;
    }

    public static EcoreValidableResponse sendRequest(ValidatableResponse validatableResponse) {
        return new EcoreValidableResponse(validatableResponse);
    }

    public static EcoreValidableResponse createRole(Role role) {
        return sendRequest(givenNullableBody(RoleResponse.fromModel(role))
                .when()
                .contentType(JSON)
                .post("/v1/roles")
                .then());
    }

    public static EcoreValidableResponse getRoles() {
        return sendRequest(when()
                .get("/v1/roles")
                .then());
    }

    public static EcoreValidableResponse getRole(UUID roleId) {
        return sendRequest(given()
                .pathParam("roleId", roleId)
                .contentType(JSON)
                .when()
                .get("/v1/roles/{roleId}")
                .then());
    }

    public static EcoreValidableResponse getMembershipRole(UUID userId, UUID teamId) {
        return sendRequest(given()
                .queryParam("userId", userId)
                .queryParam("teamId", teamId)
                .when()
                .get("/v1/memberships/roles")
                .then());
    }

    public static EcoreValidableResponse createMembership(Membership membership) {
        return sendRequest(givenNullableBody(MembershipResponse.fromModel(membership))
                .contentType(JSON)
                .when()
                .post("/v1/memberships")
                .then());
    }

    public static EcoreValidableResponse getMemberships(UUID roleId) {
        return sendRequest(given()
                .pathParam("roleId", roleId)
                .when()
                .get("/v1/memberships/roles/{roleId}")
                .then());
    }

    private static RequestSpecification givenNullableBody(Object object) {
        RequestSpecification requestSpecification = given();
        if (object != null) {
            requestSpecification = requestSpecification.body(object);
        }
        return requestSpecification;
    }

    public static class EcoreValidableResponse {

        ValidatableResponse validatableResponse;

        public EcoreValidableResponse(ValidatableResponse validatableResponse) {
            this.validatableResponse = validatableResponse;
        }

        public void validate(int status, String message) {
            this.validatableResponse
                    .statusCode(status)
                    .body("status", Matchers.equalTo(status))
                    .body("error", Matchers.equalTo(message));
        }

        public ValidatableResponse statusCode(int statusCode) {
            return this.validatableResponse
                    .statusCode(statusCode);
        }

        public ExtractableResponse<Response> extract() {
            return this.validatableResponse
                    .extract();
        }

    }
}
