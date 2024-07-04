/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * License); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  base
  // Apply one top level rat plugin to perform any required license enforcement analysis
  id("org.nosphere.apache.rat") version "0.8.1"
  // Enable gradle-based release management
  id("net.researchgate.release") version "2.8.1"
  id("org.apache.beam.module")
  id("org.sonarqube") version "5.1.0.4882"
}

/*************************************************************************************************/
// Configure the root project

tasks.rat {
  // Set input directory to that of the root project instead of the CWD. This
  // makes .gitignore rules (added below) work properly.
  inputDir.set(project.rootDir)

  val exclusions = mutableListOf(
    // Ignore files we track but do not distribute
    "**/.github/**/*",
    "**/.gitkeep",
    "gradlew",
    "gradlew.bat",
    "gradle/wrapper/gradle-wrapper.properties",

    "**/package-list",
    "**/test.avsc",
    "**/user.avsc",
    "**/test/resources/**/*.txt",
    "**/test/resources/**/*.csv",
    "**/test/**/.placeholder",

    // Default eclipse excludes neglect subprojects

    // Proto/grpc generated wrappers
    "**/apache_beam/portability/api/**/*_pb2*.py",
    "**/go/pkg/beam/**/*.pb.go",
    "**/mock-apis/**/*.pb.go",

    // Ignore go.sum files, which don't permit headers
    "**/go.sum",

    // Ignore Go test data files
    "**/go/data/**",

    // VCF test files
    "**/apache_beam/testing/data/vcf/*",

    // JDBC package config files
    "**/META-INF/services/java.sql.Driver",

    // Website build files
    "**/Gemfile.lock",
    "**/Rakefile",
    "**/.htaccess",
    "website/www/site/assets/scss/_bootstrap.scss",
    "website/www/site/assets/scss/bootstrap/**/*",
    "website/www/site/assets/js/**/*",
    "website/www/site/static/images/mascot/*.ai",
    "website/www/site/static/js/bootstrap*.js",
    "website/www/site/static/js/bootstrap/**/*",
    "website/www/site/themes",
    "website/www/yarn.lock",
    "website/www/package.json",
    "website/www/site/static/js/hero/lottie-light.min.js",
    "website/www/site/static/js/keen-slider.min.js",
    "website/www/site/assets/scss/_keen-slider.scss",

    // Release automation files
    "release/src/main/scripts/*.txt",

    // Ignore ownership files
    "ownership/**/*",
    "**/OWNERS",

    // Ignore CPython LICENSE file
    "LICENSE.python",

    // Json doesn't support comments.
    "**/*.json",

    // Katas files
    "learning/katas/**/course-info.yaml",
    "learning/katas/**/task-info.yaml",
    "learning/katas/**/course-remote-info.yaml",
    "learning/katas/**/section-remote-info.yaml",
    "learning/katas/**/lesson-remote-info.yaml",
    "learning/katas/**/task-remote-info.yaml",
    "learning/katas/**/*.txt",

    // Tour Of Beam learning-content metadata and its samples
    "learning/tour-of-beam/**/content-info.yaml",
    "learning/tour-of-beam/**/module-info.yaml",
    "learning/tour-of-beam/**/group-info.yaml",
    "learning/tour-of-beam/**/unit-info.yaml",
    "learning/tour-of-beam/backend/samples/**/*.md",

    // Tour Of Beam example logs
    "learning/tour-of-beam/learning-content/**/*.log",

    // Tour Of Beam example txt files
    "learning/tour-of-beam/learning-content/**/*.txt",

    // Tour Of Beam example csv files
    "learning/tour-of-beam/learning-content/**/*.csv",

    // Tour Of Beam backend autogenerated Datastore indexes
    "learning/tour-of-beam/backend/internal/storage/index.yaml",

    // Tour Of Beam backend autogenerated Playground GRPC API stubs and mocks
    "learning/tour-of-beam/backend/playground_api/api/v1/api.pb.go",
    "learning/tour-of-beam/backend/playground_api/api/v1/api_grpc.pb.go",
    "learning/tour-of-beam/backend/playground_api/api/v1/mock.go",

    // Playground backend autogenerated GRPC API stubs and mocks
    "playground/backend/internal/api/v1/api.pb.go",
    "playground/backend/internal/api/v1/api_grpc.pb.go",

    // Playground infrastructure autogenerated GRPC API stubs and mocks
    "playground/infrastructure/api/v1/api_pb2.py",
    "playground/infrastructure/api/v1/api_pb2.pyi",
    "playground/infrastructure/api/v1/api_pb2_grpc.py",

    // test p8 file for SnowflakeIO
    "sdks/java/io/snowflake/src/test/resources/invalid_test_rsa_key.p8",
    "sdks/java/io/snowflake/src/test/resources/valid_encrypted_test_rsa_key.p8",
    "sdks/java/io/snowflake/src/test/resources/valid_unencrypted_test_rsa_key.p8",

    // Mockito extensions
    "sdks/java/io/amazon-web-services2/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker",
    "sdks/java/io/azure/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker",
    "sdks/java/extensions/ml/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker",

    // JupyterLab extensions
    "sdks/python/apache_beam/runners/interactive/extensions/apache-beam-jupyterlab-sidepanel/yarn.lock",

    // Autogenerated apitools clients.
    "sdks/python/apache_beam/runners/dataflow/internal/clients/*/**/*.py",

    // Sample text file for Java quickstart
    "sdks/java/maven-archetypes/examples/sample.txt",

    // Ignore Flutter autogenerated files for Playground
    "playground/frontend/**/*.g.dart",
    "playground/frontend/**/*.g.yaml",
    "playground/frontend/**/*.gen.dart",
    "playground/frontend/**/*.golden.yaml",
    "playground/frontend/**/*.mocks.dart",
    "playground/frontend/.metadata",
    "playground/frontend/pubspec.lock",

    // Ignore Flutter autogenerated files for Playground Components
    "playground/frontend/**/*.pb.dart",
    "playground/frontend/**/*.pbenum.dart",
    "playground/frontend/**/*.pbgrpc.dart",
    "playground/frontend/**/*.pbjson.dart",
    "playground/frontend/playground_components/.metadata",
    "playground/frontend/playground_components/pubspec.lock",

    // Ignore Flutter autogenerated files for Tour of Beam
    "learning/tour-of-beam/frontend/**/*.g.dart",
    "learning/tour-of-beam/frontend/**/*.gen.dart",
    "learning/tour-of-beam/frontend/.metadata",
    "learning/tour-of-beam/frontend/pubspec.lock",
    "learning/tour-of-beam/frontend/lib/firebase_options.dart",

    // Ignore .gitkeep file
    "**/.gitkeep",

    // Ignore Flutter localization .arb files (doesn't support comments)
    "playground/frontend/lib/l10n/**/*.arb",

    // Ignore LICENSES copied onto containers
    "sdks/java/container/license_scripts/manual_licenses",
    "sdks/python/container/license_scripts/manual_licenses",

    // Ignore autogenrated proto files.
    "sdks/typescript/src/apache_beam/proto/**/*.ts",

    // Ignore typesciript package management.
    "sdks/typescript/package-lock.json",
    "sdks/typescript/node_modules/**/*",

    // Ignore buf autogenerated files.
    "**/buf.lock",

    // Ignore poetry autogenerated files.
    "**/poetry.lock",

    // DuetAI training prompts
    "learning/prompts/**/*.md",
  )

  // Add .gitignore excludes to the Apache Rat exclusion list. We re-create the behavior
  // of the Apache Maven Rat plugin since the Apache Ant Rat plugin doesn't do this
  // automatically.
  val gitIgnore = project(":").file(".gitignore")
  if (gitIgnore.exists()) {
    val gitIgnoreExcludes = gitIgnore.readLines().filter { it.isNotEmpty() && !it.startsWith("#") }
    exclusions.addAll(gitIgnoreExcludes)
  }

  verbose.set(true)
  failOnError.set(true)
  setExcludes(exclusions)
}
tasks.check.get().dependsOn(tasks.rat)

// Define root pre/post commit tasks simplifying what is needed
// to be specified on the commandline when executing locally.
// This indirection also makes Jenkins use the branch of the PR
// for the test definitions.
tasks.register("javaPreCommit") {
  // We need to list the model/* builds since sdks/java/core doesn't
  // depend on any of the model.
  dependsOn(":model:pipeline:build")
  dependsOn(":model:job-management:build")
  dependsOn(":model:fn-execution:build")
  dependsOn(":sdks:java:core:buildNeeded")

  // Inline :sdks:java:core:buildDependents so we can carve out pieces at a time
  dependsOn(":beam-validate-runner:build")
  dependsOn(":examples:java:build")
  dependsOn(":examples:java:preCommit")
  dependsOn(":examples:java:twitter:build")
  dependsOn(":examples:java:twitter:preCommit")
  dependsOn(":examples:multi-language:build")
  dependsOn(":model:fn-execution:build")
  dependsOn(":model:job-management:build")
  dependsOn(":model:pipeline:build")
  dependsOn(":runners:core-java:build")
  dependsOn(":runners:direct-java:build")
  dependsOn(":runners:direct-java:needsRunnerTests")
  dependsOn(":runners:extensions-java:metrics:build")
  // lowest supported flink version
  var flinkVersions = project.ext.get("allFlinkVersions") as Array<*>
  dependsOn(":runners:flink:${flinkVersions[0]}:build")
  dependsOn(":runners:flink:${flinkVersions[0]}:job-server:build")
  dependsOn(":runners:google-cloud-dataflow-java:build")
  dependsOn(":runners:google-cloud-dataflow-java:examples-streaming:build")
  dependsOn(":runners:google-cloud-dataflow-java:examples:build")
  dependsOn(":runners:google-cloud-dataflow-java:worker:build")
  dependsOn(":runners:google-cloud-dataflow-java:worker:windmill:build")
  dependsOn(":runners:java-fn-execution:build")
  dependsOn(":runners:java-job-service:build")
  dependsOn(":runners:jet:build")
  dependsOn(":runners:local-java:build")
  dependsOn(":runners:portability:java:build")
  dependsOn(":runners:samza:build")
  dependsOn(":runners:samza:job-server:build")
  dependsOn(":runners:spark:3:build")
  dependsOn(":runners:spark:3:job-server:build")
  dependsOn(":runners:twister2:build")
  dependsOn(":sdks:java:build-tools:build")
  dependsOn(":sdks:java:container:java8:docker")
  dependsOn(":sdks:java:core:build")
  dependsOn(":sdks:java:core:jmh:build")
  dependsOn(":sdks:java:expansion-service:build")
  dependsOn(":sdks:java:expansion-service:app:build")
  dependsOn(":sdks:java:extensions:arrow:build")
  dependsOn(":sdks:java:extensions:avro:build")
  dependsOn(":sdks:java:extensions:euphoria:build")
  dependsOn(":sdks:java:extensions:google-cloud-platform-core:build")
  dependsOn(":sdks:java:extensions:jackson:build")
  dependsOn(":sdks:java:extensions:join-library:build")
  dependsOn(":sdks:java:extensions:kryo:build")
  dependsOn(":sdks:java:extensions:ml:build")
  dependsOn(":sdks:java:extensions:protobuf:build")
  dependsOn(":sdks:java:extensions:python:build")
  dependsOn(":sdks:java:extensions:sbe:build")
  dependsOn(":sdks:java:extensions:schemaio-expansion-service:build")
  dependsOn(":sdks:java:extensions:sketching:build")
  dependsOn(":sdks:java:extensions:sorter:build")
  dependsOn(":sdks:java:extensions:timeseries:build")
  dependsOn(":sdks:java:extensions:zetasketch:build")
  dependsOn(":sdks:java:harness:build")
  dependsOn(":sdks:java:harness:jmh:build")
  dependsOn(":sdks:java:io:bigquery-io-perf-tests:build")
  dependsOn(":sdks:java:io:common:build")
  dependsOn(":sdks:java:io:contextualtextio:build")
  dependsOn(":sdks:java:io:expansion-service:build")
  dependsOn(":sdks:java:io:file-based-io-tests:build")
  dependsOn(":sdks:java:io:sparkreceiver:2:build")
  dependsOn(":sdks:java:io:synthetic:build")
  dependsOn(":sdks:java:io:xml:build")
  dependsOn(":sdks:java:javadoc:allJavadoc")
  dependsOn(":sdks:java:testing:expansion-service:build")
  dependsOn(":sdks:java:testing:jpms-tests:build")
  dependsOn(":sdks:java:testing:load-tests:build")
  dependsOn(":sdks:java:testing:nexmark:build")
  dependsOn(":sdks:java:testing:test-utils:build")
  dependsOn(":sdks:java:testing:tpcds:build")
  dependsOn(":sdks:java:testing:watermarks:build")
  dependsOn(":sdks:java:transform-service:build")
  dependsOn(":sdks:java:transform-service:app:build")
  dependsOn(":sdks:java:transform-service:launcher:build")
}

// a precommit task build multiple IOs (except those splitting into single jobs)
tasks.register("javaioPreCommit") {
  dependsOn(":sdks:java:io:amqp:build")
  dependsOn(":sdks:java:io:cassandra:build")
  dependsOn(":sdks:java:io:csv:build")
  dependsOn(":sdks:java:io:cdap:build")
  dependsOn(":sdks:java:io:clickhouse:build")
  dependsOn(":sdks:java:io:debezium:expansion-service:build")
  dependsOn(":sdks:java:io:debezium:build")
  dependsOn(":sdks:java:io:elasticsearch-tests:elasticsearch-tests-5:build")
  dependsOn(":sdks:java:io:elasticsearch-tests:elasticsearch-tests-6:build")
  dependsOn(":sdks:java:io:elasticsearch-tests:elasticsearch-tests-7:build")
  dependsOn(":sdks:java:io:elasticsearch-tests:elasticsearch-tests-8:build")
  dependsOn(":sdks:java:io:elasticsearch-tests:elasticsearch-tests-common:build")
  dependsOn(":sdks:java:io:elasticsearch:build")
  dependsOn(":sdks:java:io:file-schema-transform:build")
  dependsOn(":sdks:java:io:google-ads:build")
  dependsOn(":sdks:java:io:hbase:build")
  dependsOn(":sdks:java:io:hcatalog:build")
  dependsOn(":sdks:java:io:influxdb:build")
  dependsOn(":sdks:java:io:jdbc:build")
  dependsOn(":sdks:java:io:jms:build")
  dependsOn(":sdks:java:io:kafka:build")
  dependsOn(":sdks:java:io:kafka:upgrade:build")
  dependsOn(":sdks:java:io:kudu:build")
  dependsOn(":sdks:java:io:mongodb:build")
  dependsOn(":sdks:java:io:mqtt:build")
  dependsOn(":sdks:java:io:neo4j:build")
  dependsOn(":sdks:java:io:parquet:build")
  dependsOn(":sdks:java:io:rabbitmq:build")
  dependsOn(":sdks:java:io:redis:build")
  dependsOn(":sdks:java:io:rrio:build")
  dependsOn(":sdks:java:io:singlestore:build")
  dependsOn(":sdks:java:io:solr:build")
  dependsOn(":sdks:java:io:splunk:build")
  dependsOn(":sdks:java:io:thrift:build")
  dependsOn(":sdks:java:io:tika:build")
}

// a precommit task testing additional supported flink versions not covered by
// the main Java PreCommit (lowest supported version)
tasks.register("flinkPreCommit") {
  var flinkVersions = project.ext.get("allFlinkVersions") as Array<*>
  for (version in flinkVersions.slice(1..flinkVersions.size - 1)) {
    dependsOn(":runners:flink:${version}:build")
    dependsOn(":runners:flink:${version}:job-server:build")
  }
}

tasks.register("sqlPreCommit") {
  dependsOn(":sdks:java:extensions:sql:preCommit")
  dependsOn(":sdks:java:extensions:sql:buildDependents")
  dependsOn(":sdks:java:extensions:sql:datacatalog:build")
  dependsOn(":sdks:java:extensions:sql:expansion-service:build")
  dependsOn(":sdks:java:extensions:sql:hcatalog:build")
  dependsOn(":sdks:java:extensions:sql:jdbc:build")
  dependsOn(":sdks:java:extensions:sql:jdbc:preCommit")
  dependsOn(":sdks:java:extensions:sql:perf-tests:build")
  dependsOn(":sdks:java:extensions:sql:shell:build")
  dependsOn(":sdks:java:extensions:sql:udf-test-provider:build")
  dependsOn(":sdks:java:extensions:sql:udf:build")
  dependsOn(":sdks:java:extensions:sql:zetasql:build")
}

tasks.register("javaPreCommitPortabilityApi") {
  dependsOn(":runners:google-cloud-dataflow-java:worker:build")
}

tasks.register("javaPostCommit") {
  dependsOn(":sdks:java:extensions:google-cloud-platform-core:postCommit")
  dependsOn(":sdks:java:extensions:zetasketch:postCommit")
  dependsOn(":sdks:java:extensions:ml:postCommit")
}

tasks.register("javaPostCommitSickbay") {
  dependsOn(":runners:samza:validatesRunnerSickbay")
  for (version in project.ext.get("allFlinkVersions") as Array<*>) {
    dependsOn(":runners:flink:${version}:validatesRunnerSickbay")
  }
  dependsOn(":runners:spark:3:job-server:validatesRunnerSickbay")
  dependsOn(":runners:direct-java:validatesRunnerSickbay")
  dependsOn(":runners:portability:java:validatesRunnerSickbay")
}

tasks.register("javaHadoopVersionsTest") {
  dependsOn(":sdks:java:io:hadoop-common:hadoopVersionsTest")
  dependsOn(":sdks:java:io:hadoop-file-system:hadoopVersionsTest")
  dependsOn(":sdks:java:io:hadoop-format:hadoopVersionsTest")
  dependsOn(":sdks:java:io:hcatalog:hadoopVersionsTest")
  dependsOn(":sdks:java:io:parquet:hadoopVersionsTest")
  dependsOn(":sdks:java:extensions:sorter:hadoopVersionsTest")
  dependsOn(":runners:spark:3:hadoopVersionsTest")
}

tasks.register("javaAvroVersionsTest") {
  dependsOn(":sdks:java:extensions:avro:avroVersionsTest")
}

tasks.register("sqlPostCommit") {
  dependsOn(":sdks:java:extensions:sql:postCommit")
  dependsOn(":sdks:java:extensions:sql:jdbc:postCommit")
  dependsOn(":sdks:java:extensions:sql:datacatalog:postCommit")
  dependsOn(":sdks:java:extensions:sql:hadoopVersionsTest")
}

tasks.register("goPreCommit") {
  // Ensure the Precommit builds run after the tests, in order to avoid the
  // flake described in BEAM-11918. This is done by splitting them into two
  // tasks and using "mustRunAfter" to enforce ordering.
  dependsOn(":goPrecommitTest")
  dependsOn(":goPrecommitBuild")
}

tasks.register("goPrecommitTest") {
  dependsOn(":sdks:go:goTest")
}

tasks.register("goPrecommitBuild") {
  mustRunAfter(":goPrecommitTest")

  dependsOn(":sdks:go:goBuild")
  dependsOn(":sdks:go:examples:goBuild")
  dependsOn(":sdks:go:test:goBuild")

  // Ensure all container Go boot code builds as well.
  dependsOn(":sdks:java:container:goBuild")
  dependsOn(":sdks:python:container:goBuild")
  dependsOn(":sdks:go:container:goBuild")
}

tasks.register("goPortablePreCommit") {
  dependsOn(":sdks:go:test:ulrValidatesRunner")
}

tasks.register("goPrismPreCommit") {
  dependsOn(":sdks:go:test:prismValidatesRunner")
}

tasks.register("goPostCommitDataflowARM") {
  dependsOn(":sdks:go:test:dataflowValidatesRunnerARM64")
}

tasks.register("goPostCommit") {
  dependsOn(":sdks:go:test:dataflowValidatesRunner")
}

tasks.register("playgroundPreCommit") {
  dependsOn(":playground:lintProto")
  dependsOn(":playground:backend:precommit")
  dependsOn(":playground:frontend:precommit")
}

tasks.register("pythonPreCommit") {
  dependsOn(":sdks:python:test-suites:tox:pycommon:preCommitPyCommon")
  dependsOn(":sdks:python:test-suites:tox:py38:preCommitPy38")
  dependsOn(":sdks:python:test-suites:tox:py39:preCommitPy39")
  dependsOn(":sdks:python:test-suites:tox:py310:preCommitPy310")
  dependsOn(":sdks:python:test-suites:tox:py311:preCommitPy311")
}

tasks.register("pythonPreCommitIT") {
  dependsOn(":sdks:python:test-suites:tox:pycommon:preCommitPyCommon")
  dependsOn(":sdks:python:test-suites:dataflow:preCommitIT")
}

tasks.register("pythonDocsPreCommit") {
  dependsOn(":sdks:python:test-suites:tox:pycommon:docs")
}

tasks.register("pythonDockerBuildPreCommit") {
  dependsOn(":sdks:python:container:py38:docker")
  dependsOn(":sdks:python:container:py39:docker")
  dependsOn(":sdks:python:container:py310:docker")
  dependsOn(":sdks:python:container:py311:docker")
}

tasks.register("pythonLintPreCommit") {
  // TODO(https://github.com/apache/beam/issues/20209): Find a better way to specify lint and formatter tasks without hardcoding py version.
  dependsOn(":sdks:python:test-suites:tox:py38:lint")
}

tasks.register("pythonFormatterPreCommit") {
  dependsOn("sdks:python:test-suites:tox:py38:formatter")
}

tasks.register("python38PostCommit") {
  dependsOn(":sdks:python:test-suites:dataflow:py38:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py38:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py38:hdfsIntegrationTest")
  dependsOn(":sdks:python:test-suites:portable:py38:postCommitPy38")
  // TODO: https://github.com/apache/beam/issues/22651
  // The default container uses Python 3.8. The goal here is to
  // duild Docker images for TensorRT tests during run time for python versions
  // other than 3.8 and add these tests in other python postcommit suites.
  dependsOn(":sdks:python:test-suites:dataflow:py38:inferencePostCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py38:inferencePostCommitIT")
}

tasks.register("python39PostCommit") {
  dependsOn(":sdks:python:test-suites:dataflow:py39:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py39:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py39:hdfsIntegrationTest")
  dependsOn(":sdks:python:test-suites:portable:py39:postCommitPy39")
  // TODO (https://github.com/apache/beam/issues/23966)
  // Move this to Python 3.10 test suite once tfx-bsl has python 3.10 wheel.
  dependsOn(":sdks:python:test-suites:direct:py39:inferencePostCommitIT")
}

tasks.register("python310PostCommit") {
  dependsOn(":sdks:python:test-suites:dataflow:py310:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py310:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py310:hdfsIntegrationTest")
  dependsOn(":sdks:python:test-suites:portable:py310:postCommitPy310")
}

tasks.register("python311PostCommit") {
  dependsOn(":sdks:python:test-suites:dataflow:py311:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py311:postCommitIT")
  dependsOn(":sdks:python:test-suites:direct:py311:hdfsIntegrationTest")
  dependsOn(":sdks:python:test-suites:portable:py311:postCommitPy311")
}

tasks.register("portablePythonPreCommit") {
  dependsOn(":sdks:python:test-suites:portable:py38:preCommitPy38")
  dependsOn(":sdks:python:test-suites:portable:py311:preCommitPy311")
}

tasks.register("pythonSparkPostCommit") {
  dependsOn(":sdks:python:test-suites:portable:py38:sparkValidatesRunner")
  dependsOn(":sdks:python:test-suites:portable:py39:sparkValidatesRunner")
  dependsOn(":sdks:python:test-suites:portable:py311:sparkValidatesRunner")
}

tasks.register("websitePreCommit") {
  dependsOn(":website:preCommit")
}

tasks.register("communityMetricsPreCommit") {
  dependsOn(":beam-test-infra-metrics:preCommit")
}

tasks.register("communityMetricsProber") {
  dependsOn(":beam-test-infra-metrics:checkProber")
}

tasks.register("javaExamplesDataflowPrecommit") {
  dependsOn(":runners:google-cloud-dataflow-java:examples:preCommit")
  dependsOn(":runners:google-cloud-dataflow-java:examples-streaming:preCommit")
  dependsOn(":runners:google-cloud-dataflow-java:examplesJavaRunnerV2PreCommit")
}

tasks.register("whitespacePreCommit") {
  // TODO(https://github.com/apache/beam/issues/20209): Find a better way to specify the tasks without hardcoding py version.
  dependsOn(":sdks:python:test-suites:tox:py38:archiveFilesToLint")
  dependsOn(":sdks:python:test-suites:tox:py38:unpackFilesToLint")
  dependsOn(":sdks:python:test-suites:tox:py38:whitespacelint")
}

tasks.register("typescriptPreCommit") {
  // TODO(https://github.com/apache/beam/issues/20209): Find a better way to specify the tasks without hardcoding py version.
  dependsOn(":sdks:python:test-suites:tox:py38:eslint")
  dependsOn(":sdks:python:test-suites:tox:py38:jest")
}

tasks.register("pushAllRunnersDockerImages") {
  dependsOn(":runners:spark:3:job-server:container:docker")
  for (version in project.ext.get("allFlinkVersions") as Array<*>) {
    dependsOn(":runners:flink:${version}:job-server-container:docker")
  }

  doLast {
    if (project.hasProperty("prune-images")) {
      exec {
        executable("docker")
        args("system", "prune", "-a", "--force")
      }
    }
  }
}

tasks.register("pushAllSdkDockerImages") {
  // Enforce ordering to allow the prune step to happen between runs.
  // This will ensure we don't use up too much space (especially in CI environments)
  mustRunAfter(":pushAllRunnersDockerImages")

  dependsOn(":sdks:java:container:pushAll")
  dependsOn(":sdks:python:container:pushAll")
  dependsOn(":sdks:go:container:pushAll")
  dependsOn(":sdks:typescript:container:pushAll")

  doLast {
    if (project.hasProperty("prune-images")) {
      exec {
        executable("docker")
        args("system", "prune", "-a", "--force")
      }
    }
  }
}

tasks.register("pushAllXlangDockerImages") {
  // Enforce ordering to allow the prune step to happen between runs.
  // This will ensure we don't use up too much space (especially in CI environments)
  mustRunAfter(":pushAllSdkDockerImages")

  dependsOn(":sdks:java:expansion-service:container:docker")
  dependsOn(":sdks:java:transform-service:controller-container:docker")
  dependsOn(":sdks:python:expansion-service-container:docker")

  doLast {
    if (project.hasProperty("prune-images")) {
      exec {
        executable("docker")
        args("system", "prune", "-a", "--force")
      }
    }
  }
}

tasks.register("pushAllDockerImages") {
  dependsOn(":pushAllRunnersDockerImages")
  dependsOn(":pushAllSdkDockerImages")
  dependsOn(":pushAllXlangDockerImages")
}

// Use this task to validate the environment set up for Go, Python and Java
tasks.register("checkSetup") {
  dependsOn(":sdks:go:examples:wordCount")
  dependsOn(":sdks:python:wordCount")
  dependsOn(":examples:java:wordCount")
}

// Configure the release plugin to do only local work; the release manager determines what, if
// anything, to push. On failure, the release manager can reset the branch without pushing.
release {
  revertOnFail = false
  tagTemplate = "v${version}"
  // workaround from https://github.com/researchgate/gradle-release/issues/281#issuecomment-466876492
  release {
    with (propertyMissing("git") as net.researchgate.release.GitAdapter.GitConfig) {
      requireBranch = "release-.*|master"
      pushToRemote = ""
    }
  }
}

// Reports linkage errors across multiple Apache Beam artifact ids.
//
// To use (from the root of project):
//    ./gradlew -Ppublishing -PjavaLinkageArtifactIds=artifactId1,artifactId2,... :checkJavaLinkage
//
// For example:
//    ./gradlew -Ppublishing -PjavaLinkageArtifactIds=beam-sdks-java-core,beam-sdks-java-io-jdbc :checkJavaLinkage
//
// Note that this task publishes artifacts into your local Maven repository.
if (project.hasProperty("javaLinkageArtifactIds")) {
  if (!project.hasProperty("publishing")) {
    throw GradleException("You can only check linkage of Java artifacts if you specify -Ppublishing on the command line as well.")
  }

  val linkageCheckerJava by configurations.creating
  dependencies {
    linkageCheckerJava("com.google.cloud.tools:dependencies:1.5.6")
  }

  // We need to evaluate all the projects first so that we can find depend on all the
  // publishMavenJavaPublicationToMavenLocal tasks below.
  for (p in rootProject.subprojects) {
    if (p.path != project.path) {
      evaluationDependsOn(p.path)
    }
  }

  project.tasks.register<JavaExec>("checkJavaLinkage") {
    dependsOn(project.getTasksByName("publishMavenJavaPublicationToMavenLocal", true /* recursively */))
    classpath = linkageCheckerJava
    mainClass.value("com.google.cloud.tools.opensource.classpath.LinkageCheckerMain")
    val javaLinkageArtifactIds: String = project.property("javaLinkageArtifactIds") as String? ?: ""
    var arguments = arrayOf("-a", javaLinkageArtifactIds.split(",").joinToString(",") {
      if (it.contains(":")) {
        "${project.ext.get("mavenGroupId")}:${it}"
      } else {
        // specify the version if not provided
        "${project.ext.get("mavenGroupId")}:${it}:${project.version}"
      }
    })

    // Exclusion file filters out existing linkage errors before a change
    if (project.hasProperty("javaLinkageWriteBaseline")) {
      arguments += "--output-exclusion-file"
      arguments += project.property("javaLinkageWriteBaseline") as String
    } else if (project.hasProperty("javaLinkageReadBaseline")) {
      arguments += "--exclusion-file"
      arguments += project.property("javaLinkageReadBaseline") as String
    }
    args(*arguments)
    doLast {
      println("NOTE: This task published artifacts into your local Maven repository. You may want to remove them manually.")
    }
  }
}
if (project.hasProperty("testJavaVersion")) {
  var testVer = project.property("testJavaVersion")

  tasks.getByName("javaPreCommitPortabilityApi").dependsOn(":sdks:java:testing:test-utils:verifyJavaVersion$testVer")
  tasks.getByName("javaExamplesDataflowPrecommit").dependsOn(":sdks:java:testing:test-utils:verifyJavaVersion$testVer")
} else {
  allprojects {
    tasks.withType(Test::class).configureEach {
      exclude("**/JvmVerification.class")
    }
  }
}
