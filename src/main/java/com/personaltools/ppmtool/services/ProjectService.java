package com.personaltools.ppmtool.services;

import com.personaltools.ppmtool.domain.Backlog;
import com.personaltools.ppmtool.domain.Project;
import com.personaltools.ppmtool.domain.User;
import com.personaltools.ppmtool.exceptions.ProjectIdException;
import com.personaltools.ppmtool.exceptions.ProjectNotFoundException;
import com.personaltools.ppmtool.repositories.BacklogRepository;
import com.personaltools.ppmtool.repositories.ProjectRepository;
import com.personaltools.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        //Logic

        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be created because it does not exist.");
            }
        }

        try{



            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            String projectId = project.getProjectIdentifier().toUpperCase();
            //System.out.println(projectId);
            project.setProjectIdentifier(projectId);

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectId);
            }

            if(project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectId));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' arleady exists");
        }



    }

    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier((projectId.toUpperCase()));

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"'does not exist");

        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }



        return project;
    }

    public Iterable<Project> findAllProjects(String username){

        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username){

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }


}
