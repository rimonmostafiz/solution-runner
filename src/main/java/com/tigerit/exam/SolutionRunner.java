package com.tigerit.exam;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

/**
 * @author Faisal Ahmed
 */
public class SolutionRunner {

    private static final Logger logger = LoggerFactory.getLogger(SolutionRunner.class);

    public static void main(String... args) throws IOException {
        // loading configuration.
        Configuration configuration = Configuration.load(args);
        logger.debug("Configuration is loaded with {} resource.", configuration.type());

        // parse applicant list and store into memory
        Set<Applicant> applicants = ApplicantListParser.parse(configuration.get("applicants.file.location"));
        logger.debug("Listing all the applicants parsed from applicants.list file");
        logger.debug(">>>>");
        for(Applicant applicant : applicants) {
            logger.debug("Name: [{}] <> Email: [{}] <> Repo: [{}]",
                    applicant.getName(), applicant.getEmail(), applicant.getRepo());
        }
        logger.debug("<<<<");

        // create repo root directory, if it already exists remove it and create it again.
        String repoRootDirLocation = configuration.get("repo.root.directory.name");
        logger.debug("Repo root directory location: {}", repoRootDirLocation);
        File repoDirectory = new File(repoRootDirLocation);
        FileUtils.deleteDirectory(repoDirectory);
        logger.debug("Creating repo root directory [{}]", repoDirectory.toString());
        Files.createDirectory(Paths.get(repoDirectory.getPath()));

        // create results root directory, if it already exists remove it and create it again.
        String resultRootDirLocation = configuration.get("result.root.directory.name");
        logger.debug("Result root directory location: {}", resultRootDirLocation);
        File resultDirectory = new File(resultRootDirLocation);
        FileUtils.deleteDirectory(resultDirectory);
        logger.debug("Creating result root directory [{}]", resultDirectory.toString());
        Files.createDirectory(Paths.get(resultDirectory.getPath()));

        // iterate all the applicants, create task for each and execute that task
        for(Applicant applicant : applicants) {
            ApplicantTask task = new ApplicantTask(applicant, configuration);
            task.run();
        }

        // iterate all the applicants and print their result
        for(Applicant applicant : applicants) {
            logger.info("Name: [{}] -- Email: [{}] -- Result: [{}]",
                    applicant.getName(), applicant.getEmail(), applicant.getResult());
        }
    }

}
