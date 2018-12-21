import com.example.qa.FileHelper
import com.example.qa.ComponentScmInfo
import com.example.qa.ResolveComponentFromGitScm

/**
 * ci pipeline serves as pre-commit jobs and release jobs
 */
def call(body) {
        final String DISABLED = "DISABLED"
        final String ENABLED = "ENABLED"
        def defaultConfig = [sonarCheck: ENABLED]
        def config = defaultConfig
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        // Context are mostly from env
        def final envBranchName = env.'BRANCH_NAME' ?: ''
        // this is e.g 'master' for a CI build, but 'PR-18' for a PR build
        def final envTargetBranch = env.'CHANGE_TARGET' ?: ''
        // This is empty for CI build, but e.g. 'master' for a PR build

        boolean isPR = (envBranchName.startsWith('PR-'))

        def final jobScm = config.jobScm ?: scm
        scm = jobScm

        ComponentScmInfo componentScmInfo = ResolveComponentFromGitScm.parse(jobScm)
        def final gitUrl = componentScmInfo.githubUrl // jobScm.getUserRemoteConfigs()[0].getUrl()
        def final component = config.component ?: componentScmInfo.component
        def final scmFullComponentName = componentScmInfo.fullComponentName
        def final scmOrgName = componentScmInfo.githubOrg

        baStage("Show ENV") {
                echo env.getEnvironment().toString()

                echo "Git SCM"
                echo "gitUrl=$gitUrl"
                echo "component=$component"
                echo "scmFullComponentName=$scmFullComponentName"
                echo "scmOrgName=$scmOrgName"
        }

        baStage("Checkout"){
                echo "Checkout source code..."
                node('master') {
                        dir("${component}") {
                                retry(1) {
                                        checkout jobScm
                                        bat '''echo %cd%
'''
                                }
                        }
                }
        }

        baStage("Build&Test"){
                echo "Build package..."
                node('master') {
                        dir("${component}") {
                                retry(1) {
                                        checkout jobScm
                                        def buildscript = libraryResource 'build/build.bat'
                                        bat buildscript
                                }
                        }
                }
        }

        baStage("Publish"){
                echo "Publish artifacts..."
        }
}