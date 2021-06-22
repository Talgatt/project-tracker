package com.personaltools.ppmtool.exceptions;

public class ProjectNotFoundExceptionResponse {

    private String ProjectNotFound;

    public String getProjectNotFound() {
        return ProjectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        ProjectNotFound = projectNotFound;
    }

    public ProjectNotFoundExceptionResponse(String projectNotFound){
        ProjectNotFound = projectNotFound;
    }
}
