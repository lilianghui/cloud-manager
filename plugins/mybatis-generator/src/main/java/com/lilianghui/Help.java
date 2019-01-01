package com.lilianghui;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "help")
public class Help extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("------------------------------------------------------------------------");
        getLog().info("MyBatis代码生成工具");
        getLog().info("------------------------------------------------------------------------");
    }

}
