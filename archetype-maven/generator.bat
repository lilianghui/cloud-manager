mvn clean archetype:create-from-project -DpackageName=com.lilianghui
cd target\generated-sources\archetype
mvn clean install
echo 'generator successful'