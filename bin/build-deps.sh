#!/bin/sh
set -e

dir=$(mktemp -d)
echo "Building in $dir"
cd "$dir"

git clone https://github.com/ornicar/ssu
cd ssu
sbt publish-local
cd ..
