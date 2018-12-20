package com.example.qa

class ComponentScmInfo {
    String githubUrl
    String githubUrlProtocol
    String githubHostname
    String githubOrg
    String componentPrefix
    String component
    String fullComponentName
    String pullRequestId
    String getGithubRootUrl() {
        return "${githubUrlProtocol}://${githubHostname}"
    }

    String getGithubApiUrl() {
        return "${getGithubRootUrl()}/api/v3"
    }

    String getGithubOrgUrl() {
        return "${getGithubRootUrl()}/${githubOrg}"
    }

    @Override
    public String toString() {
        return """ComponentScmInfo {
,githubUrl=$githubUrl
,githubUrlProtocol=$githubUrlProtocol
,githubHostname=$githubHostname
,githubOrg=$githubOrg
,componentPrefix=$componentPrefix
,component=$component
,fullComponentName=$fullComponentName
,pullRequestId=$pullRequestId
}
"""
    }
}
