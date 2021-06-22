package com.personaltools.ppmtool.services;

import com.personaltools.ppmtool.domain.Backlog;
import com.personaltools.ppmtool.domain.Project;
import com.personaltools.ppmtool.domain.ProjectTask;
import com.personaltools.ppmtool.exceptions.ProjectIdException;
import com.personaltools.ppmtool.exceptions.ProjectNotFoundException;
import com.personaltools.ppmtool.repositories.BacklogRepository;
import com.personaltools.ppmtool.repositories.ProjectRepository;
import com.personaltools.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            // PTs to be added to a specific project, project != null, BL exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
                    //backlogRepository.findByProjectIdentifier(projectIdentifier);

            // set the backlog to PT
            projectTask.setBacklog(backlog);

            // we want our project sequence to be like this: IDPRO-1 IDPRO-2
            Integer BacklogSequence = backlog.getPTSequence();

            // Update the BL SEQUENCE
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);

            // Add Sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // INITIAL status when status is null
            if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            // INITIAL priority when priority is null
            if(projectTask.getPriority()==null || projectTask.getPriority()==0){
                projectTask.setPriority(3);
            }


            return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask>findBacklogById(String id, String username){

//        Project project = projectRepository.findByProjectIdentifier(id, username);
//
//        if(project == null){
//            throw new ProjectNotFoundException("Project with ID: '"+ id+"' does not exist");
//        }

        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){

        // make sure we are searching for an existing backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        // make sure that the backlog/projet_id is in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }



        return projectTask;
    }


    // Update project task
    // find existing project task
    // replace it with updated task
    // save update

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

        projectTaskRepository.delete(projectTask);
    }


}



