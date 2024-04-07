.DEFAULT_GOAL := help
.PHONY: help run build test format install

run:
	docker run --rm --name peakquizApp -p 7878:8080 peakquiz-backend

build:
	mvn clean package
	docker build -t peakquiz-backend .

test:
	mvn clean test

format:
	mvn formatter:format

install:
	mvn clean install

help:
	@echo Available targets:
	@echo   build   : Build the application
	@echo   run     : Run the application
	@echo   test    : Test the application
	@echo   format  : Format the source code of the application
	@echo   install : Install the application
	@echo Usage: make target