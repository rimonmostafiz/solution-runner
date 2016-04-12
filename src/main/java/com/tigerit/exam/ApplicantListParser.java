package com.tigerit.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Faisal Ahmed
 */
public class ApplicantListParser {
    private static final Logger logger = LoggerFactory.getLogger(ApplicantListParser.class);

    public static Set<Applicant> parse(String csv) {
        Set<Applicant> applicants = new HashSet<>();
        logger.debug("reading applicant list from: {} csv file", csv);
        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line; int lineNumber = 1 ;
            while ((line = br.readLine()) != null) {
                if(!line.startsWith("#")) {
                    String[] each = line.split(",");
                    if(each.length == 3) {
                        applicants.add(new Applicant(each[0], each[1], each[2]));
                    } else {
                        logger.error("Error parsing applicant data at line number: {}", lineNumber);
                    }
                } else {
                    logger.debug("Line {} Skipped", lineNumber);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return applicants;
    }
}
