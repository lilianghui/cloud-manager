本项目为maven archetype项目，为创建maven项目模板而生
在当前项目pom.xml同级目录下执行以下步骤
packageName：基础包名

mvn archetype:create-from-project -DpackageName=com.lilianghui
cd target\generated-sources\archetype
mvn clean install

Idea安装 maven archetype catalogs 插件
在File | Settings | Build, Execution, Deployment | Build Tools | Maven Archetype Catalogs
下添加archetype-catalog.xml,创建项目时可以找到该archetype

或执行命令:
mvn archetype:generate -DarchetypeGroupId=com.lilianghui -DarchetypeVersion=0.0.1-SNAPSHOT -DarchetypeArtifactId=archetype-maven-archetype

生成后的项目删除generator.bat、generator.sh、ReadMe.md文件

参考
https://www.cnblogs.com/happy-coder/p/6430802.html
https://blog.csdn.net/a5518007/article/details/62885432
 