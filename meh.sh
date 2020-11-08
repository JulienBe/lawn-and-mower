#!/bin/bash
path=`pwd`
files="
opppsidontexist
$path/src/test/invalids/meaningless
$path/src/test/invalids/negative
$path/src/test/invalids/no_instructions
$path/src/test/invalids/no_mower
$path/src/test/invalids/nothing
$path/src/test/invalids/partial_instructions
$path/src/test/invalids/too_much_dirs
$path/src/test/invalids/twice
$path/test_inputs/huge_lawn
"
mvn clean compile quarkus:dev -Dquarkus.args="$files"
