run:
	docker run --rm --name peakquizApp -p 7878:8080 peakquiz-backend

build:
	mvn clean package
	docker build -t peakquiz-backend .