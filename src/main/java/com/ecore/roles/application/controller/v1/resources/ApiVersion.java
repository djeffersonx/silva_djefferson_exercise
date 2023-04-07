package com.ecore.roles.application.controller.v1.resources;

import org.springframework.http.HttpHeaders;

public class ApiVersion {

    public static final String V1_VALUE = "application/vnd.ecore.v1+json";
    public static final String V1 = HttpHeaders.ACCEPT + "=" + V1_VALUE;

}
