#!/bin/bash

#constant declaration
APPLICATION_ROOT=~/Labs/solution-runner
INPUT_PATH=/tmp/sample-input
LIB_PATH=/tmp/lib/*
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

    printf "copying candidate program in /tmp/ir \n"
    mkdir /tmp/programming-test/results/${repo_url[3]}
    cp build/libs/$JAR_FILE /tmp/programming-test/results/${repo_url[3]}/

    printf "injecting sample inputs \n"
    cp $INPUT_PATH /tmp/programming-test/results/${repo_url[3]}/

    printf "going to program directory \n"
    cd /tmp/programming-test/results/${repo_url[3]}
    printf "running program ... \n"
    java -cp "$LIB_PATH" -jar $JAR_FILE

    ## going back to root directory
    cd $APPLICATION_ROOT

    ## should be the last line
    i=$((i+1))
done
