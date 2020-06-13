##################################################################
#    Project Convenience Makefile Wrapper for Maven & Docker     #
##################################################################

# This makefile is just a convenience wrapper for the Maven
# program and the docker build which is used to build packaged
# microservices for entire functionality. The actual building
# rules for this project may be found in the Maven "pom.xml"
# */Dockerfile and file located in this folder.

######################### DEFINITIONS ############################

# Define the commandline invocation of Maven if necessary:
# ifeq ($(MVN))
#     MVN  := mvn
# endif

MVN = mvn
DOCKER_COMPOSE=docker-compose

######################## BUILD TARGETS ###########################

clean:
	cd ./TrashEmailService && $(MVN) clean
	cd ./EmailsToTelegramService && $(MVN) clean

build :
	echo "Building TrashEmailService ...\n"
	cd ./TrashEmailService && $(MVN) -Dmaven.test.skip=true install
	echo "Building EmailsToTelegramService ...\n"
	cd ./EmailsToTelegramService && $(MVN) -Dmaven.test.skip=true install
	echo "Building the docker-compose ...\n"
	$(DOCKER_COMPOSE) build

copy :
	echo $(ENV)
	cp ./EmailsToTelegramServiceConfig-$(ENV).yml EmailsToTelegramService/src/main/resources/application-$(ENV).yml
	cp ./TrashEmailServiceConfig-$(ENV).yml TrashEmailService/src/main/resources/application-$(ENV).yml

qa : export ENV:=qa
qa : clean copy build

dev : export ENV:=dev
dev : clean copy build

prod : export ENV:=prod
prod : clean copy build
