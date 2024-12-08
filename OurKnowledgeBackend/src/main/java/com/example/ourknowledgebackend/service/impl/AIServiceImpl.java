package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.infrastructure.MapFormatter;
import com.example.ourknowledgebackend.infrastructure.OpenAIService;
import com.example.ourknowledgebackend.service.AIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final ObjectMapper objectMapper;
    private final OpenAIService openAIService;

    private Map<String, Object> jsonRequest;

    @PostConstruct
    public void init() throws IOException {
        jsonRequest = objectMapper.readValue(new ClassPathResource("openAIPrompts/FileTechnologies.json").getInputStream(), Map.class);
    }

    public void llamada() {
        jsonRequest = MapFormatter.formatMap(jsonRequest, Map.of("configContent", exampleConfigContent, "technologiesContent", exampleTechnologiesContent));
        Map<String, Object> result = openAIService.askGPT(jsonRequest);

        System.out.println(result);

    }

    String exampleConfigContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>3.3.0</version>
                    <relativePath/> <!-- lookup parent from repository -->
                </parent>
                <groupId>com.example</groupId>
                <artifactId>OurKnowledgeBackend</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <name>OurKnowledgeBackend</name>
                <description>Web backend for knowledge administration</description>
                <properties>
                    <java.version>22</java.version>
            
                    <!--  Data source properties -->
                    <dataSource.user>TFG</dataSource.user>
                    <dataSource.password>OurKnowledge</dataSource.password>
                    <testDataSource.user>${dataSource.user}</testDataSource.user>
                    <testDataSource.password>${dataSource.password}</testDataSource.password>
            
                    <!-- Package versions -->
                    <jjwt.version>0.11.5</jjwt.version>
                    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
            
                    <!-- Plugin versions -->
                    <sqlPlugin.version>1.5</sqlPlugin.version>
                </properties>
            
                <profiles>
                    <profile>
                        <id>mysql</id>
                        <activation>
                            <activeByDefault>true</activeByDefault>
                        </activation>
                        <properties>
                            <!-- JDBC driver properties -->
                            <jdbcDriver.groupId>com.mysql</jdbcDriver.groupId>
                            <jdbcDriver.artifactId>mysql-connector-j</jdbcDriver.artifactId>
                            <jdbcDriver.version>8.0.33</jdbcDriver.version>
                            <jdbcDriver.className>com.mysql.cj.jdbc.Driver</jdbcDriver.className>
            
                            <!--  Data source properties -->
                            <dataSource.baseUrl>jdbc:mysql://localhost/OurKnowledge</dataSource.baseUrl>
                            <dataSource.url>${dataSource.baseUrl}?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=Europe/Madrid</dataSource.url>
                            <testDataSource.url>${dataSource.baseUrl}Test?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=Europe/Madrid</testDataSource.url>
                            <dataSource.createTablesScript>1-MySQLCreateTables.sql</dataSource.createTablesScript>
                            <dataSource.createDataScript>2-MySQLCreateData.sql</dataSource.createDataScript>
                        </properties>
                    </profile>
                </profiles>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web-services</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-devtools</artifactId>
                        <scope>runtime</scope>
                        <optional>true</optional>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-test</artifactId>
                        <scope>test</scope>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-data-jpa</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
                    </dependency>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-security</artifactId>
                    </dependency>
            
                    <dependency>
                        <groupId>org.springframework.security</groupId>
                        <artifactId>spring-security-test</artifactId>
                        <scope>test</scope>
                    </dependency>
            
                    <dependency>
                        <groupId>com.fasterxml.jackson.datatype</groupId>
                        <artifactId>jackson-datatype-jsr310</artifactId>
                        <version>2.14.2</version>
                    </dependency>
                    <dependency>
                        <groupId>com.fasterxml.jackson.core</groupId>
                        <artifactId>jackson-annotations</artifactId>
                        <version>2.14.2</version>
                    </dependency>
                    <dependency>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <optional>true</optional>
                    </dependency>
            
                    <dependency>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.mapstruct.extensions.spring</groupId>
                        <artifactId>mapstruct-spring-annotations</artifactId>
                        <version>1.0.1</version>
                    </dependency>
                    <dependency>
                        <groupId>org.mapstruct.extensions.spring</groupId>
                        <artifactId>mapstruct-spring-extensions</artifactId>
                        <version>1.0.1</version>
                    </dependency>
            
                    <dependency>
                        <groupId>javax.validation</groupId>
                        <artifactId>validation-api</artifactId>
                        <version>2.0.1.Final</version>
                    </dependency>
                    <dependency>
                        <groupId>io.swagger.core.v3</groupId>
                        <artifactId>swagger-annotations</artifactId>
                        <version>2.2.15</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.28</version>
                    </dependency>
                    <dependency>
                        <groupId>com.fasterxml.jackson.core</groupId>
                        <artifactId>jackson-databind</artifactId>
                    </dependency>
                </dependencies>
            
                <build>
            
                    <testResources>
                        <testResource>
                            <directory>src/test/resources</directory>
                            <filtering>true</filtering>
                        </testResource>
                    </testResources>
            
                    <plugins>
                        <plugin>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-maven-plugin</artifactId>
                            <configuration>
                                <excludes>
                                    <exclude>
                                        <groupId>org.projectlombok</groupId>
                                        <artifactId>lombok</artifactId>
                                    </exclude>
                                </excludes>
                            </configuration>
                        </plugin>
            
                        <plugin>
                            <groupId>org.codehaus.mojo</groupId>
                            <artifactId>sql-maven-plugin</artifactId>
                            <version>${sqlPlugin.version}</version>
                            <dependencies>
                                <dependency>
                                    <groupId>${jdbcDriver.groupId}</groupId>
                                    <artifactId>${jdbcDriver.artifactId}</artifactId>
                                    <version>${jdbcDriver.version}</version>
                                </dependency>
                            </dependencies>
                            <configuration>
                                <driver>${jdbcDriver.className}</driver>
                                <url>${dataSource.url}</url>
                                <username>${dataSource.user}</username>
                                <password>${dataSource.password}</password>
                                <autocommit>true</autocommit>
                                <onError>continue</onError>
                                <orderFile>ascending</orderFile>
                                <fileset>
                                    <basedir>${basedir}</basedir>
                                    <includes>
                                        <include>
                                            src/sql/${dataSource.createTablesScript}
                                        </include>
                                        <include>
                                            src/sql/${dataSource.createDataScript}
                                        </include>
                                    </includes>
                                </fileset>
                            </configuration>
                            <executions>
                                <execution>
                                    <id>create-tables-for-testing</id>
                                    <phase>process-test-resources</phase>
                                    <goals>
                                        <goal>execute</goal>
                                    </goals>
                                    <configuration>
                                        <driver>${jdbcDriver.className}</driver>
                                        <url>${testDataSource.url}</url>
                                        <username>${testDataSource.user}</username>
                                        <password>${testDataSource.password}</password>
                                        <autocommit>true</autocommit>
                                        <onError>continue</onError>
                                        <fileset>
                                            <basedir>${basedir}</basedir>
                                            <includes>
                                                <include>
                                                    src/sql/${dataSource.createTablesScript}
                                                </include>
                                            </includes>
                                        </fileset>
                                    </configuration>
                                </execution>
                            </executions>
                        </plugin>
            
                        <plugin>
                            <groupId>com.sngular</groupId>
                            <artifactId>scs-multiapi-maven-plugin</artifactId>
                            <version>5.3.3</version>
                            <executions>
                                <execution>
                                    <id>openapi</id>
                                    <phase>generate-sources</phase>
                                    <goals>
                                        <goal>openapi-generation</goal>
                                    </goals>
                                    <configuration>
                                        <specFiles>
                                            <specFile>
                                                <filePath>${project.basedir}/src/main/resources/api/ourKnowledge.yaml</filePath>
                                                <modelNameSuffix>DTO</modelNameSuffix>
                                                <apiPackage>com.example.ourKnowledge.api</apiPackage>
                                                <modelPackage>com.example.ourKnowledge.api.model</modelPackage>
                                                <useLombokModelAnnotation>true</useLombokModelAnnotation>
                                                <useTagsGroup>true</useTagsGroup>
                                            </specFile>
                                        </specFiles>
                                    </configuration>
                                </execution>
                            </executions>
                        </plugin>
            
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-compiler-plugin</artifactId>
                            <version>3.11.0</version>
                            <configuration>
                                <source>1.8</source>
                                <target>1.8</target>
                                <annotationProcessorPaths>
                                    <path>
                                        <groupId>org.mapstruct</groupId>
                                        <artifactId>mapstruct-processor</artifactId>
                                        <version>${org.mapstruct.version}</version>
                                    </path>
                                    <path>
                                        <groupId>org.projectlombok</groupId>
                                        <artifactId>lombok</artifactId>
                                        <version>${lombok.version}</version>
                                    </path>
                                    <dependency>
                                        <groupId>org.projectlombok</groupId>
                                        <artifactId>lombok-mapstruct-binding</artifactId>
                                        <version>0.2.0</version>
                                    </dependency>
                                    <!-- other annotation processors -->
                                </annotationProcessorPaths>
                            </configuration>
                        </plugin>
                    </plugins>
                </build>
            </project>
            """;

    String exampleTechnologiesContent = """
            [
              {
                "id": 1,
                "name": "Languages"
                "parent_id": null
              },
              {
                "id": 2,
                "name": "Backend"
                "parent_id": null
              },
              {
                "id": 3,
                "name": "FrontEnd"
                "parent_id": null
              },
              {
                "id": 4,
                "name": "Java",
                "parent_id": 1
              },
              {
                "id": 5,
                "name": "C++",
                "parent_id": 1
              },
              {
                "id": 6,
                "name": "Python",
                "parent_id": 1
              },
              {
                "id": 7,
                "name": "Spring",
                "parent_id": 2
              },
              {
                "id": 8,
                "name": "SpringBoot",
                "parent_id": 7
              },
              {
                "id": 9,
                "name": "Maven",
                "parent_id": 2
              },
              {
                "id": 10,
                "name": "Database"
                "parent_id": null
              },
              {
                "id": 11,
                "name": "MySql",
                "parent_id": 10
              },
              {
                "id": 12,
                "name": "OracleSql",
                "parent_id": 10
              },
              {
                "id": 18,
                "name": "React",
                "parent_id": 3
              },
              {
                "id": 22,
                "name": "C#",
                "parent_id": 1
              },
              {
                "id": 25,
                "name": "JavaScript",
                "parent_id": 1
              },
              {
                "id": 41,
                "name": "SpringCloud",
                "parent_id": 7
              },
              {
                "id": 44,
                "name": "Angular",
                "parent_id": 3
              },
              {
                "id": 45,
                "name": "Ocaml",
                "parent_id": 1
              },
              {
                "id": 46,
                "name": "PostgreSql",
                "parent_id": 10
              },
              {
                "id": 47,
                "name": "Gradle",
                "parent_id": 2
              },
              {
                "id": 49,
                "name": "Elixir",
                "parent_id": 1
              },
              {
                "id": 53,
                "name": "tecnologia1"
                "parent_id": null
              },
              {
                "id": 54,
                "name": "tecnologia2"
                "parent_id": null
              },
              {
                "id": 55,
                "name": "tecnologia3"
                "parent_id": null
              },
              {
                "id": 125,
                "name": "PHP",
                "parent_id": 3
              },
              {
                "id": 133,
                "name": "Español",
                "parent_id": 1
              },
              {
                "id": 134,
                "name": "Historia"
                "parent_id": null
              },
              {
                "id": 138,
                "name": "Scratch",
                "parent_id": 1
              },
              {
                "id": 140,
                "name": "hija",
                "parent_id": 18
              },
              {
                "id": 147,
                "name": "MariaDB",
                "parent_id": 10
              },
              {
                "id": 150,
                "name": "MultiApi",
                "parent_id": 2
              }
            ]""";
}