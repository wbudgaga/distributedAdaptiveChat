#!/bin/bash
echo "Running a Chat Client : $2"
java -cp chatApp.jar  cs518.a4.distributedchat.applications.ChatClientConsoleApp "$1" "$2" faure 8000
