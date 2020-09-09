#!/usr/bin/env bash
#2 arguments passed in when running through VStack
localID=$1
browserstackUrl=$2

mvn clean verify -Pbstack \
-Dbrowser=osxFF \
-Dbrowserstack.url=$browserstackUrl \
-Dlocal.id=$localID