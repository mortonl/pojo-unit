#!/usr/bin/env bash

set -e

mvn --settings .travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion="$TRAVIS_TAG"