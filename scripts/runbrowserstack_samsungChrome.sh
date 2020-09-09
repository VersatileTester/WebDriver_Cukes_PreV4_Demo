#!/usr/bin/env bash
#Arguments passed in when running on the command line.
localID=$1
browserstackUrl=$2

mvn clean verify -Pbstack \
-Dbrowser=samsungChrome \
-Dbrowserstack.url=$browserstackUrl \
-Dlocal.id=$localID