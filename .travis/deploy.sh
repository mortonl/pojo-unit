#!/usr/bin/env bash

set -e

mvn clean deploy --settings .travis/settings.xml -DskipTests=true -B -U