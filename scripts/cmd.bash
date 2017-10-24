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

if [ "$*" == "hello" ] ; then
  echo Hello $USER
  exit 0
fi

SCRIPTS_DIR=$PWD

cd ..

ROOT_DIR=$PWD

if ! [ -f $CLIENT_JAR ] ; then
  cd greetgo.cmd.client
  gradle jar
  cd $ROOT_DIR
  if ! [ -f $CLIENT_JAR ] ; then
    echo "Cannot build $CLIENT_JAR" >&2
    exit 1
  fi
fi

cd $ROOT_DIR

if ! which java > /dev/null ; then
  echo java does not installed >&2
  echo Please install java 1.8+ >&2
  exit 1
fi

java -jar $CLIENT_JAR $*
