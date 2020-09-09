#!/usr/bin/env bash
#Arguments passed in when running on the command line.
localID=$1
browserstackUrl=$2

mvn clean verify -Pbstack \
-Dbrowser=winChrome \
-Dbrowserstack.url=$browserstackUrl \
-Dlocal.id=$localID