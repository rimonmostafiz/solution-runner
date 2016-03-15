package com.tigerit.exam;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Faisal Ahmed
 */
public class ApplicantTask implements Runnable {

    private Applicant applicant;

    private Configuration configuration;

    private static final Logger logger = LoggerFactory.getLogger(ApplicantTask.class);

    public ApplicantTask(Applicant applicant, Configuration configuration) {
        this.applicant = applicant;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            // create applicant directory with email address
            String applicantRepoLocation = configuration.get(
                    "repo.root.directory.name") + "/" + applicant.getEmail();
            logger.debug("Applicant repo directory created");
            Files.createDirectory(Paths.get(applicantRepoLocation));


            // clone applicant repo from github
            Git git = Git.cloneRepository().setURI(applicant.getRepo())
                    .setDirectory(new File(applicantRepoLocation)).call();
            logger.debug("{} repo cloned successfully", applicant.getRepo());

        } catch (Exception e) {
            logger.debug("Unhandled Runtime Exception");
            throw new RuntimeException(e);
        }

        // build applicant repo
        // load applicant solution.jar dynamically
        // redirect stream
        // execute solution
        // check result
        // log result
    }
}
