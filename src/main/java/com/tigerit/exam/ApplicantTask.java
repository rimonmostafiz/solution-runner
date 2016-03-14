package com.tigerit.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // clone applicant repo from github
        // build applicant repo
        // load applicant solution.jar dynamically
        // redirect stream
        // execute solution
        // check result
        // log result
    }
}
