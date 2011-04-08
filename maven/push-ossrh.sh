#!/bin/sh
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=dist/uncommons-maths-1.2.2.jar -Psigned_release 
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=core/build/uncommons-maths-1.2.2-src.jar -Dclassifier=sources -Psigned_release
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=uncommons-maths-1.2.2.pom -Dfile=uncommons-maths-javadoc-1.2.2.jar -Dclassifier=javadoc -Psigned_release
