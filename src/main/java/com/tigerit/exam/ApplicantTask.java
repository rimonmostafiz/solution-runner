package com.tigerit.exam;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        // create applicant directory with email address
        String applicantRepoLocation = configuration
                .get("repo.root.directory.name") + "/" + applicant.getEmail();
        try {
            logger.debug("Applicant repo directory created");
            Files.createDirectory(Paths.get(applicantRepoLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // clone applicant repo from github
        try {
            Git.cloneRepository().setURI(applicant.getRepo())
                    .setDirectory(new File(applicantRepoLocation)).call();
            logger.debug("{} repo cloned successfully", applicant.getRepo());
        } catch (GitAPIException e) {
            logger.error("Unable to clone repo {}", applicant.getRepo());
            applicant.setResult("git clone failed");
            return;
        }

        // build applicant repo
        try {
            String buildCommand = "gradle build -p " + applicantRepoLocation;
            logger.debug("Running gradle build: [{}]", buildCommand);
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(buildCommand);
            Thread.sleep(10000); //hack
        } catch (Exception e) {
            logger.debug("build failed for repo {}", applicant.getRepo());
            applicant.setResult("build failed");
            return;
        }

        // load applicant solution.jar dynamically and execute within sandbox
        String jarFileName = applicantRepoLocation + "/build/libs/solution.jar";
        logger.debug("Jar: {}", jarFileName);
        if(!Files.exists(Paths.get(jarFileName))) {
            logger.debug("compile error for repo {}", applicant.getRepo());
            applicant.setResult("compile error");
            return;
        }
        try {
            JarClassLoader classLoader = new JarClassLoader();
            classLoader.add(new FileInputStream(jarFileName));
            JclObjectFactory factory = JclObjectFactory.getInstance();
            Runnable solution = (Runnable) factory.create(classLoader, "com.tigerit.exam.Solution");
            String inputFileName = configuration.get("problem.input.file.name");
            String outputFileName = configuration.get("result.root.directory.name") + "/" + applicant.getEmail();
            SandBox sandBox = new SandBox(inputFileName, outputFileName);
            sandBox.run(solution);
            Thread.sleep(10000); // hack
            logger.debug("task execution completed ..");
        } catch (FileNotFoundException e) {
            logger.debug("jar file not found");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // verify applicant result with actual result.
        try {
            String probFileLoc = configuration.get("problem.output.file.name");
            String resultFileLoc = configuration.get("result.root.directory.name") + "/" + applicant.getEmail();
            String actualResult = DigestUtils.md5Hex(new FileInputStream(probFileLoc));
            String applicantResult = DigestUtils.md5Hex(new FileInputStream(resultFileLoc));
            if(!actualResult.equals(applicantResult)) {
                logger.debug("incorrect result {} - {} !!!", applicant.getName(), applicant.getEmail());
                applicant.setResult("failed");
            } else {
                logger.debug("correct result {} - {} !!!", applicant.getName(), applicant.getEmail());
                applicant.setResult("passed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
