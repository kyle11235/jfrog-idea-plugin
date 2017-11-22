# Overview
JFrog IntelliJ IDEA plugin adds JFrog Xray scanning of Maven project dependencies to your IntelliJ IDEA.

# Getting started

### Prerequisites
IntelliJ IDEA version 2016.2 and above.

JFrog Xray version 1.7.2.3 and above.

### Installation
#### Installing from IntelliJ IDEA:

Go to Settings (Preferences) -> Plugins -> Browse repositories -> Search for JFrog -> Install
![Alt text](docs/install.png?raw=true "Installing JFrog plugin")

#### Manual installation

Download the latest JFrog IDEA plugin from [Bintray](https://bintray.com/jfrog/jfrog-jars/download_file?file_path=org%2Fjfrog%2Fidea%2Fjfrog-idea-plugin%2F1.0.0%2FJFrog-1.0.0.zip).

Go to Settings (Preferences) -> Plugins -> Install plugin from disk...

Select the downloaded JFrog IDEA plugin file and press `OK`

![Alt text](docs/manual_install.png?raw=true "Manually Installing JFrog plugin")

### User Guide

#### Setting up JFrog Xray
Go to Settings (Preferences) -> Other Settings -> JFrog Xray Configuration

Configure JFrog Xray URL and credentials.

Test your connection to Xray using the ```Test connection``` button.

![Alt text](docs/credentials.png?raw=true "Setting up credentials")

#### View
The JFrog IntelliJ plugin displays a window tool view which, by default, is at the bottom of the screen.

The window tool can be accessed at: View -> Tool windows -> JFrog 

![Alt text](docs/enable_tool_window.png?raw=true "Enable tool window")

#### Scanning and viewing the results
JFrog Xray automatically performs a scan whenever there is a change in dependencies in the project.

To manually invoke a scan, click ```Refresh``` button in JFrog Plugin tool window.

![Alt text](docs/tool_window.png?raw=true "Scan results window")

#### Filtering Xray Scan Results
There are two ways to filter the scan results:
1. **Issue severity:** Only display issues with the specified severity.
2. **Component license:** Only display components with the specified licenses.


![Alt text](docs/filter_issues.png?raw=true "Issues filter")
![Alt text](docs/filter_licenses.png?raw=true "Licenses filter")
# Building and Testing the Sources

To buid the plugin sources, please follow these steps:
1. Clone the code from git.
2. CD to the *xray* directory located under the *jfrog-idea-plugin* directory.
3. Build and install the *xray-client-java* dependency in your local maven repository, by running the following gradle command:
```
gradle clean install
```
4. If you'd like run the *xray-client-java* integration tests, follow these steps:
* Make sure your Xray instance is up and running.
* Set the *CLIENTTESTS_XRAY_URL*, *CLIENTTESTS_XRAY_USERNAME* and *CLIENTTESTS_XRAY_PASSWORD* environment variables with your Xray URL, username and password.
* Run the following command:
```
gradle test
```
5. CD to the *plugin* directory located under the *jfrog-idea-plugin* directory.
6. Build and create the JFrog IDEA Plugin zip file by running the following gradle command.
After the build finishes, you'll find the zip file in the *plugin/build/distributions* directory, located under the *jfrog-idea-plugin* directory.
The zip file can be loaded into IntelliJ

```
gradle clean build
```

# Developing the Plugin Code
If you'd like to help us develop and enhance the plugin, this section is for you.
To build and run the plugin following your code changes, follow these steps:

1. From IntelliJ, open the plugin project, by selecting *jfrog-idea-plugin/plugin/build.gradle* file.
2. Build the sources and launch the plugin by the following these steps:
* From the *Gradle Projects* window, expand *Idea --> Tasks -->  IntelliJ*
* Run the *buildPlugin* task.
* Run the *runIdea* task.

# Code Contributions
We welcome community contribution through pull requests.

# Release Notes
The release are available on [Bintray](https://bintray.com/jfrog/jfrog-jars/jfrog-idea-plugin#release).
