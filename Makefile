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