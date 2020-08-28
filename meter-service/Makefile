ifeq (${VERSION},)
  VERSION=$(shell git describe --tags 2>/dev/null)
endif
ifeq (${GIT_COMMIT},)
  GIT_COMMIT=$(shell git log -1 --format='%H')
endif
ifeq (${GIT_BRANCH},)
  GIT_BRANCH=$(shell git rev-parse --abbrev-ref HEAD)
endif
ifeq (${JAR_FILE},)
  JAR_FILE=target/*.jar
endif

.PHONY: all

all: build

info: ## Show info
	@echo "  Version: ${VERSION}"
	@echo "  GitHash: ${GIT_COMMIT}"
	@echo "  Branch: ${GIT_BRANCH}"
	@echo "  App: ${JAR_FILE}"

install: ## Installs dependencies
	mvn clean install -DskipTests=true -Dspring-boot.run.profiles=local

run: ## Runs the service locally
	mvn spring-boot:run -Dspring-boot.run.profiles=local

test: ## Runs the tests
	mvn test

docker-build: ## Packages the app
	mvn -Dmaven.test.skip=true -Dgoogle-project=akenzacore -Dgit-branch=${GIT_BRANCH} -Dgit-revision=${GIT_COMMIT} package dockerfile:build

docs: install ## Exposes the docs
	./repo/monitoring/docker/check-container.sh prototype-service-docs
	docker run -d -p 8099:8080 -e SWAGGER_JSON=/tmp/prototype-service.yaml -v $(shell pwd)/target/:/tmp --name prototype-service-docs swaggerapi/swagger-ui
	open -a "Google Chrome" http://localhost:8099/index.html?prototype-service.yaml

build: install ## Build docker-compose
	docker-compose build

dc-up: ## Start docker-compose 
	docker-compose up

dc-down: ## Shutdown docker-compose 
	docker-compose down

init-repo: ## Inits base repo
	git submodule add git@bitbucket.org:hivemindag/base-repo.git repo

update-repo: ## Updates base repo
	git submodule update --init --remote

help: ## Display this help screen
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
