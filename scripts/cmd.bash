#!/usr/bin/env bash

cd $(dirname $0)

VERSION=$(cat ../version.txt)

CLIENT_JAR=greetgo.cmd.client/build/libs/greetgo.cmd.client-${VERSION}.jar

if [ "$*" == "up" ] ; then
  git pull
  exit 0
fi

if ! which gradle > /dev/null ; then
  echo gradle does not installed >&2
  echo Please install gradle 3.5+ >&2
  exit 1
fi

GRADLE=$(realpath ..)/gradlew

if ! which java > /dev/null ; then
  echo java does not installed >&2
  echo Please install java 1.8+ >&2
  exit 1
fi

if [ "$*" == "status" ] ; then
  echo User $USER
  echo CURRENT_WORKING_DIR = $CURRENT_WORKING_DIR
  echo GRADLE = $GRADLE

  exit 0
fi

SCRIPTS_DIR=$PWD

cd ..

ROOT_DIR=$PWD

if [ ! -f $CLIENT_JAR -o -n "$GREETGO_DEBUG" ] ; then
  cd greetgo.cmd.client
  $GRADLE jar
  cd $ROOT_DIR
  if ! [ -f $CLIENT_JAR ] ; then
    echo "Cannot build $CLIENT_JAR" >&2
    exit 1
  fi
fi

cd $ROOT_DIR

java -jar $CLIENT_JAR $*
