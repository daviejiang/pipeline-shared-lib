package com.example.qa


import hudson.plugins.git.GitSCM
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ResolveComponentFromGitScm {

    static ComponentScmInfo parse(scm) {
        final String gitUrl = getGitScmUrl(scm)

        ComponentScmInfo componentScmInfo = parseGitUrl(gitUrl)
        if (!componentScmInfo) {
            throw new IllegalArgumentException("${scm} cannot be parsed.")
        }
        return componentScmInfo
    }

    static ComponentScmInfo parsePr(String prUrl) {
        ComponentScmInfo componentScmInfo = parsePrUrl(prUrl)
        if (!componentScmInfo) {
            throw new IllegalArgumentException("${prUrl} cannot be parsed.")
        }
        return componentScmInfo
    }


    private static String getGitScmUrl(scm) {
        def gitUrl
        if (!scm) {
            throw new IllegalArgumentException("ResolveComponentFromGitScm expects a GitSCM instance as argument")
        }
        if (scm instanceof GitSCM) {
            gitUrl = scm.getUserRemoteConfigs()[0]?.getUrl()
        } else if (scm instanceof String) {
            gitUrl = scm  // accepts a gitUrl string as a shortcut
        } else {
            throw new IllegalArgumentException("ResolveComponentFromGitScm expects a GitSCM instance as argument")
        }
        return  gitUrl
    }

    def static final gitUrlPattern = /(http[s]?)\:\/\/([A-Za-z0-9\.\-_]*)\/([A-Za-z0-9\-_]*)\/(idl\-|au\-)?([0-9a-zA-Z\-\_]+).git/
    static final String prUrlPattern = /(http[s]?)\:\/\/([A-Za-z0-9\.\-_]*)\/([A-Za-z0-9\-_]*)\/(idl\-|au\-)?([0-9a-zA-Z\-\_]+)\/pull\/?([0-9]+)/

    private static ComponentScmInfo parseGitUrl(gitUrl) {
        def final matcher = (gitUrl =~ gitUrlPattern)

        ComponentScmInfo componentScmInfo =null
        if ( matcher.matches() && matcher[0].size() == 6 ) {
            componentScmInfo = new ComponentScmInfo()
            componentScmInfo.githubUrl = gitUrl
            componentScmInfo.githubUrlProtocol = matcher[0][1]
            componentScmInfo.githubHostname = matcher[0][2]
            componentScmInfo.githubOrg = matcher[0][3]
            componentScmInfo.componentPrefix = matcher[0][4]
            componentScmInfo.component = matcher[0][5]
            componentScmInfo.fullComponentName = "${componentScmInfo.componentPrefix ?: ''}${componentScmInfo.component}"
        }
        return componentScmInfo
    }

    /**
     * check whether a build is triggered by multiple stream line of upper project.
     * @param prUrl the PR GitHub URL (must trim the last '/' character)
     * @return ComponentScmInfo if input url can be parsed by prUrlPattern , otherwise throws Exception
     */
    static ComponentScmInfo parsePrUrl(String prUrl) throws Exception {
        LOGGER.info("PR URL -> $prUrl")
        def final matcher = (prUrl =~ prUrlPattern)
        ComponentScmInfo componentScmInfo =null
        if ( matcher.matches() && matcher[0].size() == 7 ) {
            componentScmInfo = new ComponentScmInfo()
            componentScmInfo.githubUrl = prUrl
            componentScmInfo.githubUrlProtocol = matcher[0][1]
            componentScmInfo.githubHostname = matcher[0][2]
            componentScmInfo.githubOrg = matcher[0][3]
            componentScmInfo.componentPrefix = matcher[0][4]
            componentScmInfo.component = matcher[0][5]
            componentScmInfo.pullRequestId = matcher[0][6]
            componentScmInfo.fullComponentName = "${componentScmInfo.componentPrefix ?: ''}${componentScmInfo.component}"

        }
        return componentScmInfo
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveComponentFromGitScm.class)

    static void test() {
        def validComponentNames = [ 'https://github.wdf.test.com/bizx/au-V4.git' : 'V4'
                                    ,'https://github.wdf.test.com/bizx/au-genericobject.git' : 'genericobject'
                                    ,'https://github.wdf.test.com/bizx/idl-solr5.git' : 'solr5'
                                    ,'https://github.wdf.test.com/SFSF-PLT/gdpr-reporting-service.git' : 'gdpr-reporting-service'
                                    ,'https://github.wdf.test.com/SFSF-RCM/agency2-app-service.git' : 'agency2-app-service'
                                    ,'https://github.wdf.test.com/sfcd/shared-lib.git' : 'shared-lib'
                                    ,'https://github.wdf.test.com/sfcd/cdpipeline.git' : 'cdpipeline'
        ]
        LOGGER.info('Running simple unit-test for ResolveComponentFromGitScm...')
        validComponentNames.each{ gitUrl, name ->
            LOGGER.info("Parsing ${gitUrl} ( expected: ${name})...")
            def expected = parse(gitUrl)?.fullComponentName
            if (expected != name) {
                LOGGER.info("    -> got : ${expected}")
                LOGGER.info('Test failed')
            }
        }
        LOGGER.info('Test passed!')
    }
}
