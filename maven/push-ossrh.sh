#!/bin/sh

# This is a shell script that pushes the pieces of uncommons-math to central.
# To use it, you should read https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide.
# You will need:
# * an ID on oss.sonatype.org
# * permission to push to the org.uncommons group ID
# * a PGP key 
# * a ~/.m2/settings.xml with a profile (signed_release) and a '<server/>' for oss.sonatype.org The profile 
# selects your PGP key and provides the passphrase.
# 
# the names of the files don't matter; the real names will be assigned as part of the deployment.
# the content of the POM does matter; you must update it to the correct version # and maintain
# the list of dependencies.

mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=dist/uncommons-maths-1.2.2.jar -Psigned_release 
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=core/build/uncommons-maths-1.2.2-src.jar -Dclassifier=sources -Psigned_release
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=uncommons-maths-javadoc-1.2.2.jar -Dclassifier=javadoc -Psigned_release
