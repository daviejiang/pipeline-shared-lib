import com.example.qa.FileHelper

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

        // Job configure
        properties = []

        // Context are mostly from env
        def final envBranchName = env.'BRANCH_NAME' ?: ''
        // this is e.g 'master' for a CI build, but 'PR-18' for a PR build
        def final envTargetBranch = env.'CHANGE_TARGET' ?: ''
        // This is empty for CI build, but e.g. 'master' for a PR build

        boolean isPR = (envBranchName.startsWith('PR-'))

        baStage("Show ENV") {
                echo env.getEnvironment().toString()
                echo binding.variables.toString()
        }

        baStage("Checkout"){
                echo "Checkout source code..."
        }

        baStage('Unit tests') {
                echo "Unit tests..."
        }

        baStage("Build&Test"){
                echo "Build package..."
        }

        baStage("Publish"){
                echo "Publish artifacts..."
        }
}