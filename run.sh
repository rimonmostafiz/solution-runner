#!/bin/bash

#constant declaration
APPLICATION_ROOT=~/Labs/solution-runner
REPORT_FILE=/tmp/programming-test/report
DATA_INPUT_FILE=/tmp/data.input
DATA_OUTPUT_FILE=/tmp/data.output
CLASSPATH=/tmp/lib/real.jar:solution.jar
JAR_FILE=solution.jar

## create result folder in /tmp directory
printf "creating source root directory /tmp/programming-test \n"
mkdir /tmp/programming-test

printf "creating project directory /tmp/programming-test/projects \n"
mkdir /tmp/programming-test/projects

printf "creating project directory /tmp/programming-test/results \n"
mkdir /tmp/programming-test/results

declare -a repo_urls

# Load file into array.
readarray -t repo_urls < input.txt

# Explicitly report array content.
let i=0
while (( ${#repo_urls[@]} > i )); do
    IFS='/' read -a repo_url <<< "${repo_urls[i]}"
    printf "creating directory '${repo_url[3]}'\n"
    mkdir /tmp/programming-test/projects/${repo_url[3]}

    printf "changing directory to ${repo_url[3]}\n"
    cd /tmp/programming-test/projects/${repo_url[3]}

    printf "cloning url: ${repo_urls[i]}\n"
    git clone ${repo_urls[i]}

    IFS='.' read -a project_dir <<< ${repo_url[4]}
    printf "changing directory to project folder '${project_dir[0]}'\n"
    cd ${project_dir[0]}

    printf "building project ... \n"
    ./gradlew build

    printf "copying candidate program in /tmp/programming-test/results/ \n"
    mkdir /tmp/programming-test/results/${repo_url[3]}
    cp build/libs/$JAR_FILE /tmp/programming-test/results/${repo_url[3]}/

    printf "injecting sample input file \n"
    cp $DATA_INPUT_FILE /tmp/programming-test/results/${repo_url[3]}/

    printf "injecting sample output file \n"
    cp $DATA_OUTPUT_FILE /tmp/programming-test/results/${repo_url[3]}/

    printf "going to program directory \n"
    cd /tmp/programming-test/results/${repo_url[3]}
    printf "running program ... \n"
    java "${JVM_OPTS[@]}" -classpath "$CLASSPATH" com.tigerit.solution.Solution

    printf "comparing applicant program result \n"
    result=$(diff -y -W 72 $DATA_OUTPUT_FILE /tmp/programming-test/results/${repo_url[3]})

    if [ $? -eq 0 ]
    then
            echo "${repo_url[3]}  PASSED" >> $REPORT_FILE
    else
            echo "${repo_url[3]}  FAILED" >> $REPORT_FILE
    fi

    ## going back to root directory
    cd $APPLICATION_ROOT

    ## should be the last line
    i=$((i+1))
done
